package org.silvermob.sdk.renderingtestapp.plugplay.bidding.testing

import android.os.Bundle
import android.util.Log
import android.view.View
import org.silvermob.sdk.AdSize
import org.silvermob.sdk.api.exceptions.AdException
import org.silvermob.sdk.api.rendering.BannerView
import org.silvermob.sdk.api.rendering.listeners.BannerViewListener
import org.silvermob.sdk.renderingtestapp.AdFragment
import org.silvermob.sdk.renderingtestapp.R
import org.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerBinding
import org.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment

/**
 * Example for testing memory leaks with rendering API ad units.
 * It doesn't use Google ad view because it causes another memory leak after loadAd().
 */
open class MemoryLeakTestingRenderingApiBannerFragment : AdFragment() {

    private var bannerView: BannerView? = null

    override val layoutRes = R.layout.fragment_bidding_banner

    private val binding: FragmentBiddingBannerBinding
        get() = getBinding()

    override fun configuratorMode() = AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)
        binding.adIdLabel.text = getString(R.string.label_auid, configId)
        binding.btnLoad.setOnClickListener {
            resetEventButtons()
            loadAd()
        }
    }

    override fun initAd(): Any {
        bannerView = BannerView(
            requireContext(),
            configId,
            AdSize(width, height)
        )
        bannerView?.setAutoRefreshDelay(refreshDelay)

        // Anonymous listener
        bannerView?.setBannerListener(object : BannerViewListener {

            override fun onAdLoaded(bannerView: BannerView?) {
                Log.d("TEST", "Static class listener")
            }

            override fun onAdDisplayed(bannerView: BannerView?) {}
            override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {}
            override fun onAdClicked(bannerView: BannerView?) {}
            override fun onAdClosed(bannerView: BannerView?) {}

        })

        // Static listener
//        bannerView?.setBannerListener(MyBannerViewListener())

        binding.viewContainer.addView(bannerView)
        return "Testing"
    }

    override fun loadAd() {
        bannerView?.loadAd()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 1) Call only stop auto-refresh
//        bannerView?.stopRefresh()

        // 2) Call destroy
        bannerView?.destroy()
    }

    private class MyBannerViewListener : BannerViewListener {

        override fun onAdLoaded(bannerView: BannerView?) {
            Log.d("TEST", "Static class listener")
        }

        override fun onAdDisplayed(bannerView: BannerView?) {}
        override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {}
        override fun onAdClicked(bannerView: BannerView?) {}
        override fun onAdClosed(bannerView: BannerView?) {}

    }

}