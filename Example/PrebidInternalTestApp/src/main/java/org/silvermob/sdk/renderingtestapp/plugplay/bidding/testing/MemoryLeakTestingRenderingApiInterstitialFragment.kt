package org.silvermob.sdk.renderingtestapp.plugplay.bidding.testing

import android.os.Bundle
import android.view.View
import org.silvermob.sdk.AdSize
import org.silvermob.sdk.api.data.AdUnitFormat
import org.silvermob.sdk.api.exceptions.AdException
import org.silvermob.sdk.api.rendering.InterstitialAdUnit
import org.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener
import org.silvermob.sdk.renderingtestapp.AdFragment
import org.silvermob.sdk.renderingtestapp.R
import org.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingInterstitialBinding
import org.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import java.lang.ref.WeakReference
import java.util.*

/**
 * Example for testing memory leaks with rendering API ad units.
 * It doesn't use Google ad view because it causes another memory leak after loadAd().
 */
open class MemoryLeakTestingRenderingApiInterstitialFragment : AdFragment() {

    private var interstitialAdUnit: InterstitialAdUnit? = null

    override val layoutRes = R.layout.fragment_bidding_interstitial

    private val binding: FragmentBiddingInterstitialBinding
        get() = getBinding()

    override fun configuratorMode() = AdConfiguratorDialogFragment.AdConfiguratorMode.INTERSTITIAL

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)
        binding.adIdLabel.text = getString(R.string.label_auid, configId)
    }

    override fun initAd(): Any {
        val adUnitFormat = getAdUnitIdentifierTypeBasedOnTitle(getTitle())
        interstitialAdUnit = if (adUnitFormat == AdUnitFormat.VIDEO) {
            InterstitialAdUnit(
                requireContext(),
                configId,
                EnumSet.of(adUnitFormat)
            )
        } else {
            InterstitialAdUnit(requireContext(), configId)
        }
        interstitialAdUnit?.setMinSizePercentage(AdSize(30, 30))

        // Static listener
        interstitialAdUnit?.setInterstitialAdUnitListener(object : InterstitialAdUnitListener {
            override fun onAdLoaded(interstitialAdUnit: InterstitialAdUnit?) {
                interstitialAdUnit?.show()
            }

            override fun onAdDisplayed(interstitialAdUnit: InterstitialAdUnit?) {}
            override fun onAdFailed(interstitialAdUnit: InterstitialAdUnit?, exception: AdException?) {}
            override fun onAdClicked(interstitialAdUnit: InterstitialAdUnit?) {}
            override fun onAdClosed(interstitialAdUnit: InterstitialAdUnit?) {}
        })

        // Anonymous listener
//        interstitialAdUnit?.setInterstitialAdUnitListener(MyInterstitialAdUnitListener(interstitialAdUnit))

        return "Testing"
    }

    override fun loadAd() {
        interstitialAdUnit?.loadAd()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Destroy
        interstitialAdUnit?.destroy()
    }

    private fun getAdUnitIdentifierTypeBasedOnTitle(title: String): AdUnitFormat {
        return if (title.contains("Video Interstitial", ignoreCase = true) && !title.contains(
                "MRAID 2.0",
                ignoreCase = true
            )
        ) {
            AdUnitFormat.VIDEO
        } else {
            AdUnitFormat.BANNER
        }
    }

    private class MyInterstitialAdUnitListener(interstitialAdUnit: InterstitialAdUnit?) : InterstitialAdUnitListener {

        private val interstitialAdUnitReference = WeakReference(interstitialAdUnit)

        override fun onAdLoaded(interstitialAdUnit: InterstitialAdUnit?) {
            val interstitial = interstitialAdUnitReference.get()
            interstitial?.show()
        }

        override fun onAdDisplayed(interstitialAdUnit: InterstitialAdUnit?) {}
        override fun onAdFailed(interstitialAdUnit: InterstitialAdUnit?, exception: AdException?) {}
        override fun onAdClicked(interstitialAdUnit: InterstitialAdUnit?) {}
        override fun onAdClosed(interstitialAdUnit: InterstitialAdUnit?) {}
    }

}