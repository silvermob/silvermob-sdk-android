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

package com.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.original

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.silvermob.sdk.BannerAdUnit
import com.silvermob.sdk.addendum.AdViewUtils
import com.silvermob.sdk.addendum.PbFindSizeError
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents
import com.silvermob.sdk.renderingtestapp.utils.CommandLineArgumentParser

open class GamOriginalBannerFragment : AdFragment() {
    companion object {
        private const val TAG = "GamOriginalBanner"
    }

    private var adUnit: com.silvermob.sdk.BannerAdUnit? = null
    private var adView: AdManagerAdView? = null

    override val layoutRes = R.layout.fragment_bidding_banner

    private val binding: FragmentBiddingBannerBinding
        get() = getBinding()
    private lateinit var events: Events

    open fun createAdUnit(): com.silvermob.sdk.BannerAdUnit {
        val adUnit = com.silvermob.sdk.BannerAdUnit(configId, width, height)
        if (configId.contains("multisize")) {
            adUnit?.addAdditionalSize(728, 90)
        }
        return adUnit
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)
        events = Events(view)
        binding.adIdLabel.text = getString(R.string.label_auid, configId)
        binding.btnLoad.setOnClickListener {
            resetEventButtons()
            loadAd()
        }
    }

    override fun initAd(): Any {
        val adView = AdManagerAdView(requireContext())
        adView.adUnitId = adUnitId
        adView.setAdSizes(AdSize(width, height))
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(adView, object : com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener {
                    override fun success(width: Int, height: Int) {
                        adView.setAdSizes(AdSize(width, height))
                    }

                    override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
                })
                Log.d(TAG, "onAdLoaded() called")
                resetEventButtons()
                events.loaded(true)
                binding.btnLoad.isEnabled = true
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d(TAG, "onAdFailed() called with throwable = [${p0.message}]")
                resetEventButtons()
                events.failed(true)
                binding.btnLoad.isEnabled = true
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "onAdClicked() called")
                events.clicked(true)
            }

        }
        this.adView = adView
        binding.viewContainer.addView(adView)

        adUnit = createAdUnit()
        adUnit?.setAutoRefreshInterval(refreshDelay)
        CommandLineArgumentParser.addAdUnitSpecificData(adUnit!!)

        return adView
    }

    override fun loadAd() {
        val request = AdManagerAdRequest.Builder().build()
        adUnit?.fetchDemand(request) {
            adView?.loadAd(request)
        }
    }

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adView?.destroy()
        adUnit?.stopAutoRefresh()
    }

    private class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)

    }

}