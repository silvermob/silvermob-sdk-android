/*
 *    Copyright 2018-2021 Prebid.org, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.silvermob.sdk.renderingtestapp.plugplay.bidding.ppm

import android.os.Bundle
import android.view.View
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.LogUtil
import com.silvermob.sdk.api.data.VideoPlacementType
import com.silvermob.sdk.api.exceptions.AdException
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.api.rendering.listeners.BannerVideoListener
import com.silvermob.sdk.api.rendering.listeners.BannerViewListener
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerVideoBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents

open class PpmVideoFragment : AdFragment(), com.silvermob.sdk.api.rendering.listeners.BannerViewListener, com.silvermob.sdk.api.rendering.listeners.BannerVideoListener {

    private val TAG = PpmVideoFragment::class.java.simpleName

    override val layoutRes = R.layout.fragment_bidding_banner_video

    protected var bannerView: com.silvermob.sdk.api.rendering.BannerView? = null

    protected val binding: FragmentBiddingBannerVideoBinding
        get() = getBinding()

    protected lateinit var events: Events


    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)
        events = Events(view)
        binding.adIdLabel.text = getString(R.string.label_auid, configId)
        binding.btnLoad.setOnClickListener {
            resetEventButtons()
            it.isEnabled = false
            loadAd()
        }

        binding.btnStopRefresh.setOnClickListener {
            bannerView?.stopRefresh()
            resetEventButtons()
            binding.btnLoad.isEnabled = true
        }
    }

    override fun initAd(): Any? {
        bannerView = com.silvermob.sdk.api.rendering.BannerView(
                requireContext(),
                configId,
                com.silvermob.sdk.AdSize(width, height)
        )
        bannerView?.videoPlacementType = com.silvermob.sdk.api.data.VideoPlacementType.IN_BANNER
        bannerView?.setBannerListener(this)
        bannerView?.setBannerVideoListener(this)
        binding.viewContainer.addView(bannerView)
        return bannerView
    }

    override fun loadAd() {
        bannerView?.loadAd()
    }

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode? {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER
    }

    override fun onAdFailed(bannerView: com.silvermob.sdk.api.rendering.BannerView?, exception: com.silvermob.sdk.api.exceptions.AdException?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onAdFailed")
        resetEventButtons()
        events.failed(true)
        binding.btnLoad.isEnabled = true
    }

    override fun onAdLoaded(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onAdLoaded")
        resetEventButtons()
        events.loaded(true)
        binding.btnLoad.isEnabled = true
    }

    override fun onAdClicked(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onAdClicked")
        events.clicked(true)
    }

    override fun onAdClosed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onAdClosed")
        events.closed(true)
    }

    override fun onAdDisplayed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onAdDisplayed")
        events.displayed(true)
    }

    override fun onVideoCompleted(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onVideoCompleted")
    }

    override fun onVideoPaused(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onVideoPaused")
    }

    override fun onVideoResumed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onVideoResumed")
    }

    override fun onVideoUnMuted(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onVideoUnMuted")
    }

    override fun onVideoMuted(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        com.silvermob.sdk.LogUtil.debug(TAG, "onVideoMuted")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerView?.destroy()
    }

    protected class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun impression(b: Boolean) = enable(R.id.btnAdImpression, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun closed(b: Boolean) = enable(R.id.btnAdClosed, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)
        fun displayed(b: Boolean) = enable(R.id.btnAdDisplayed, b)

    }
}