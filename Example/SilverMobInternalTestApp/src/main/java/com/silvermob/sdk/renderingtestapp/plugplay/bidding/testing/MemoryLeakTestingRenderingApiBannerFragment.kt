package com.silvermob.sdk.renderingtestapp.plugplay.bidding.testing

import android.os.Bundle
import android.util.Log
import android.view.View
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.api.exceptions.AdException
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.api.rendering.listeners.BannerViewListener
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment

/**
 * Example for testing memory leaks with rendering API ad units.
 * It doesn't use Google ad view because it causes another memory leak after loadAd().
 */
open class MemoryLeakTestingRenderingApiBannerFragment : AdFragment() {

    private var bannerView: com.silvermob.sdk.api.rendering.BannerView? = null

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
        bannerView = com.silvermob.sdk.api.rendering.BannerView(
                requireContext(),
                configId,
                com.silvermob.sdk.AdSize(width, height)
        )
        bannerView?.setAutoRefreshDelay(refreshDelay)

        // Anonymous listener
        bannerView?.setBannerListener(object : com.silvermob.sdk.api.rendering.listeners.BannerViewListener {

            override fun onAdLoaded(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
                Log.d("TEST", "Static class listener")
            }

            override fun onAdDisplayed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}
            override fun onAdFailed(bannerView: com.silvermob.sdk.api.rendering.BannerView?, exception: com.silvermob.sdk.api.exceptions.AdException?) {}
            override fun onAdClicked(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}
            override fun onAdClosed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}

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

    private class MyBannerViewListener : com.silvermob.sdk.api.rendering.listeners.BannerViewListener {

        override fun onAdLoaded(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
            Log.d("TEST", "Static class listener")
        }

        override fun onAdDisplayed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}
        override fun onAdFailed(bannerView: com.silvermob.sdk.api.rendering.BannerView?, exception: com.silvermob.sdk.api.exceptions.AdException?) {}
        override fun onAdClicked(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}
        override fun onAdClosed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}

    }

}