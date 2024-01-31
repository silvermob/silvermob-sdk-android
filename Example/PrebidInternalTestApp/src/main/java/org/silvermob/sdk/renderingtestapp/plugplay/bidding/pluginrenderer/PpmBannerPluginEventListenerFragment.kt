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

package org.silvermob.sdk.renderingtestapp.plugplay.bidding.pluginrenderer

import android.os.Bundle
import android.view.View
import org.silvermob.sdk.AdSize
import org.silvermob.sdk.LogUtil
import org.silvermob.sdk.SilverMob
import org.silvermob.sdk.api.exceptions.AdException
import org.silvermob.sdk.api.rendering.BannerView
import org.silvermob.sdk.api.rendering.listeners.BannerViewListener
import org.silvermob.sdk.renderingtestapp.AdFragment
import org.silvermob.sdk.renderingtestapp.R
import org.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerBinding
import org.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import org.silvermob.sdk.renderingtestapp.utils.BaseEvents
import org.silvermob.sdk.renderingtestapp.utils.CommandLineArgumentParser
import org.silvermob.sdk.renderingtestapp.utils.SampleCustomRenderer
import org.silvermob.sdk.renderingtestapp.utils.SampleCustomRendererEventListener

open class PpmBannerPluginEventListenerFragment : AdFragment(), BannerViewListener, SampleCustomRendererEventListener {
    private val TAG = PpmBannerPluginEventListenerFragment::class.java.simpleName
    private val sampleCustomRenderer = SampleCustomRenderer()

    override val layoutRes = R.layout.fragment_bidding_banner

    protected var bannerView: BannerView? = null

    protected val binding: FragmentBiddingBannerBinding
        get() = getBinding()

    protected lateinit var events: Events

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SilverMob.registerPluginRenderer(sampleCustomRenderer)
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
        bannerView = BannerView(
            requireContext(),
            configId,
            AdSize(width, height)
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

    override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {
        resetEventButtons()
        events.failed(true)
        binding.btnLoad.isEnabled = true
    }

    override fun onAdLoaded(bannerView: BannerView?) {
        resetEventButtons()
        events.loaded(true)
        binding.btnLoad.isEnabled = true
    }

    override fun onAdClicked(bannerView: BannerView?) {
        events.clicked(true)
    }

    override fun onAdClosed(bannerView: BannerView?) {
        events.closed(true)
    }

    override fun onAdDisplayed(bannerView: BannerView?) {
        events.displayed(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerView?.destroy()
    }

    override fun onImpression() {
        LogUtil.debug(TAG, "onImpression")
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
        SilverMob.unregisterPluginRenderer(sampleCustomRenderer)
        super.onDestroy()
    }
}