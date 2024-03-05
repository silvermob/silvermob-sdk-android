package com.silvermob.sdk.renderingtestapp.plugplay.bidding.testing

import android.os.Bundle
import android.view.View
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingInterstitialBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import java.lang.ref.WeakReference
import java.util.EnumSet

/**
 * Example for testing memory leaks with rendering API ad units.
 * It doesn't use Google ad view because it causes another memory leak after loadAd().
 */
open class MemoryLeakTestingRenderingApiInterstitialFragment : AdFragment() {

    private var interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit? = null

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
        interstitialAdUnit = if (adUnitFormat == com.silvermob.sdk.api.data.AdUnitFormat.VIDEO) {
            com.silvermob.sdk.api.rendering.InterstitialAdUnit(
                    requireContext(),
                    configId,
                    EnumSet.of(adUnitFormat)
            )
        } else {
            com.silvermob.sdk.api.rendering.InterstitialAdUnit(requireContext(), configId)
        }
        interstitialAdUnit?.setMinSizePercentage(com.silvermob.sdk.AdSize(30, 30))

        // Static listener
        interstitialAdUnit?.setInterstitialAdUnitListener(object : com.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener {
            override fun onAdLoaded(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
                interstitialAdUnit?.show()
            }

            override fun onAdDisplayed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {}
            override fun onAdFailed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?, exception: com.silvermob.sdk.api.exceptions.AdException?) {}
            override fun onAdClicked(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {}
            override fun onAdClosed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {}
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

    private fun getAdUnitIdentifierTypeBasedOnTitle(title: String): com.silvermob.sdk.api.data.AdUnitFormat {
        return if (title.contains("Video Interstitial", ignoreCase = true) && !title.contains(
                "MRAID 2.0",
                ignoreCase = true
            )
        ) {
            com.silvermob.sdk.api.data.AdUnitFormat.VIDEO
        } else {
            com.silvermob.sdk.api.data.AdUnitFormat.BANNER
        }
    }

    private class MyInterstitialAdUnitListener(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) : com.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener {

        private val interstitialAdUnitReference = WeakReference(interstitialAdUnit)

        override fun onAdLoaded(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
            val interstitial = interstitialAdUnitReference.get()
            interstitial?.show()
        }

        override fun onAdDisplayed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {}
        override fun onAdFailed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?, exception: com.silvermob.sdk.api.exceptions.AdException?) {}
        override fun onAdClicked(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {}
        override fun onAdClosed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {}
    }

}