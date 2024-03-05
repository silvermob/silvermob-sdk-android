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

package com.silvermob.sdk.renderingtestapp.plugplay.bidding.pluginrenderer

import android.os.Bundle
import android.view.View
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents
import com.silvermob.sdk.renderingtestapp.utils.CommandLineArgumentParser
import com.silvermob.sdk.renderingtestapp.utils.SampleCustomRenderer
import com.silvermob.sdk.renderingtestapp.utils.SampleCustomRendererEventListener

open class PpmBannerPluginEventListenerFragment : AdFragment(), com.silvermob.sdk.api.rendering.listeners.BannerViewListener, SampleCustomRendererEventListener {
    private val TAG = PpmBannerPluginEventListenerFragment::class.java.simpleName
    private val sampleCustomRenderer = SampleCustomRenderer()

    override val layoutRes = R.layout.fragment_bidding_banner

    protected var bannerView: com.silvermob.sdk.api.rendering.BannerView? = null

    protected val binding: FragmentBiddingBannerBinding
        get() = getBinding()

    protected lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.silvermob.sdk.SilverMob.registerPluginRenderer(sampleCustomRenderer)
    }

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
        bannerView?.setAutoRefreshDelay(refreshDelay)
        bannerView?.setBannerListener(this)
        bannerView?.setPluginEventListener(this)
        bannerView?.let { CommandLineArgumentParser.addAdUnitSpecificData(it) }
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
        resetEventButtons()
        events.failed(true)
        binding.btnLoad.isEnabled = true
    }

    override fun onAdLoaded(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        resetEventButtons()
        events.loaded(true)
        binding.btnLoad.isEnabled = true
    }

    override fun onAdClicked(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        events.clicked(true)
    }

    override fun onAdClosed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        events.closed(true)
    }

    override fun onAdDisplayed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        events.displayed(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerView?.destroy()
    }

    override fun onImpression() {
        com.silvermob.sdk.LogUtil.debug(TAG, "onImpression")
    }

    protected class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun impression(b: Boolean) = enable(R.id.btnAdImpression, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun closed(b: Boolean) = enable(R.id.btnAdClosed, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)
        fun displayed(b: Boolean) = enable(R.id.btnAdDisplayed, b)

    }

    override fun onDestroy() {
        com.silvermob.sdk.SilverMob.unregisterPluginRenderer(sampleCustomRenderer)
        super.onDestroy()
    }
}