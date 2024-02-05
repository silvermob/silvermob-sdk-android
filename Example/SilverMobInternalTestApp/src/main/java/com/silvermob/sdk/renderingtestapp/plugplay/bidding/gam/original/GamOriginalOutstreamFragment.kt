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

import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.silvermob.sdk.VideoAdUnit
import com.silvermob.sdk.addendum.AdViewUtils
import com.silvermob.sdk.addendum.PbFindSizeError
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerVideoBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents

class GamOriginalOutstreamFragment : AdFragment() {
    companion object {
        private const val TAG = "GamOriginalOutstream"
    }

    private var adUnit: com.silvermob.sdk.VideoAdUnit? = null
    private var gamView: AdManagerAdView? = null

    private val binding: FragmentBiddingBannerVideoBinding
        get() = getBinding()
    private lateinit var events: Events

    override fun initAd(): Any? {
        events = Events(view!!)
        adUnit = com.silvermob.sdk.VideoAdUnit(
                configId,
                width,
                height
        )

        gamView = AdManagerAdView(requireContext())
        gamView?.adUnitId = adUnitId
        gamView?.setAdSizes(AdSize(width, height))
        gamView?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(gamView, object :
                    com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener {
                    override fun success(
                        width: Int,
                        height: Int
                    ) {
                        gamView?.setAdSizes(AdSize(width, height))
                    }

                    override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
                })
                Log.d(TAG, "onAdLoaded() called")
                resetEventButtons()
                events.loaded(true)
                binding.btnLoad.isEnabled = true
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "onAdClicked() called")
                events.clicked(true)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d(TAG, "onAdFailed() called with throwable = [${p0.message}]")
                resetEventButtons()
                events.failed(true)
                binding.btnLoad.isEnabled = true
            }
        }
        binding.viewContainer.addView(gamView)
        adUnit?.setAutoRefreshInterval(refreshDelay)
        return gamView
    }

    override fun loadAd() {
        val builder = AdManagerAdRequest.Builder()
        adUnit?.fetchDemand(builder) {
            val request = builder.build()
            gamView?.loadAd(request)
        }
    }

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER
    }

    override val layoutRes: Int = R.layout.fragment_bidding_banner_video


    private class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)

    }

}