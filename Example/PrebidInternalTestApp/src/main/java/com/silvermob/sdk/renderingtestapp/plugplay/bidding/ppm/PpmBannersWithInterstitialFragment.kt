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

import android.util.Log
import android.widget.Button
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.api.exceptions.AdException
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.api.rendering.InterstitialAdUnit
import com.silvermob.sdk.api.rendering.listeners.BannerViewListener
import com.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentInterstitialHtmlWithBannersBinding
import com.silvermob.sdk.renderingtestapp.plugplay.bidding.base.BaseBannersWithInterstitialFragment
import com.silvermob.sdk.renderingtestapp.utils.getAdDescription

open class PpmBannersWithInterstitialFragment : BaseBannersWithInterstitialFragment() {

    protected lateinit var interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit
    protected lateinit var bannerViewTop: com.silvermob.sdk.api.rendering.BannerView
    protected lateinit var bannerViewBottom: com.silvermob.sdk.api.rendering.BannerView

    private val binding: FragmentInterstitialHtmlWithBannersBinding
        get() = getBinding()

    override fun loadInterstitial() {
        binding.tvInterstitialAdUnitDescription.text = "Interstitial Config ID: $interstitialConfigId"
        interstitialAdUnit = initInterstitialAdUnit(interstitialConfigId)
        interstitialAdUnit.loadAd()
    }

    override fun loadBanners() {
        bannerViewTop =
            initBannerView(bannerConfigId, REFRESH_BANNER_TOP_SEC, binding.btnTopBannerAdShown)
        bannerViewBottom =
            initBannerView(bannerConfigId, REFRESH_BANNER_BOTTOM_SEC, binding.btnBottomBannerAdShown)

        bannerViewTop.loadAd()
        bannerViewBottom.loadAd()

        binding.viewContainerTop.addView(bannerViewTop)
        binding.viewContainerBottom.addView(bannerViewBottom)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        interstitialAdUnit.destroy()
        bannerViewBottom.destroy()
        bannerViewTop.destroy()
    }

    open fun initBannerView(configId: String, refreshIntervalSec: Int, impressionCounterButton: Button?): com.silvermob.sdk.api.rendering.BannerView {
        val bannerView = com.silvermob.sdk.api.rendering.BannerView(
                requireContext(),
                configId,
                com.silvermob.sdk.AdSize(BANNER_WIDTH, BANNER_HEIGHT)
        )
        bannerView.setAutoRefreshDelay(refreshIntervalSec)
        bannerView.setBannerListener(getBannerAdListener(configId, refreshIntervalSec, impressionCounterButton))
        return bannerView
    }

    open fun initInterstitialAdUnit(configId: String): com.silvermob.sdk.api.rendering.InterstitialAdUnit {
        val interstitialAdUnit =
                com.silvermob.sdk.api.rendering.InterstitialAdUnit(requireContext(), configId)
        interstitialAdUnit.setMinSizePercentage(com.silvermob.sdk.AdSize(30, 30))
        interstitialAdUnit.setInterstitialAdUnitListener(getInterstitialAdListener())
        return interstitialAdUnit
    }

    protected fun getBannerAdListener(configId: String, refreshIntervalSec: Int, impressionCounterButton: Button?): com.silvermob.sdk.api.rendering.listeners.BannerViewListener {
        return object : com.silvermob.sdk.api.rendering.listeners.BannerViewListener {
            private var impressionCount: Int = 0

            init {
                impressionCounterButton?.text = configId.getAdDescription(refreshIntervalSec, impressionCount)
            }

            override fun onAdLoaded(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
                Log.d(TAG, "Banner: onAdLoaded()")
                impressionCount++
                impressionCounterButton?.text = configId.getAdDescription(refreshIntervalSec, impressionCount)
            }

            override fun onAdDisplayed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
                Log.d(TAG, "Banner: onAdDisplayed()")
            }

            override fun onAdFailed(bannerView: com.silvermob.sdk.api.rendering.BannerView?, exception: com.silvermob.sdk.api.exceptions.AdException?) {
                Log.d(TAG, "Banner: onAdFailed()")
            }

            override fun onAdClicked(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
                Log.d(TAG, "Banner: onAdClicked()")
            }

            override fun onAdClosed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
                Log.d(TAG, "Banner: onAdClosed()")
            }
        }
    }

    protected fun getInterstitialAdListener(): com.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener {
        return object : com.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener {
            override fun onAdLoaded(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
                Log.d(TAG, "Interstitial: onAdLoaded()")
                binding.btnLoad.isEnabled = true
                binding.btnLoad.text = getString(R.string.text_show)
                binding.btnLoad.setOnClickListener { interstitialAdUnit?.show() }
            }

            override fun onAdDisplayed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
                Log.d(TAG, "Interstitial: onAdDisplayed()")
            }

            override fun onAdFailed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?, exception: com.silvermob.sdk.api.exceptions.AdException?) {
                Log.d(TAG, "Interstitial: onAdFailed()")
            }

            override fun onAdClicked(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
                Log.d(TAG, "Interstitial: onAdClicked()")
            }

            override fun onAdClosed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
                Log.d(TAG, "Interstitial: onAdClosed()")
                interstitialAdUnit?.loadAd()
            }

        }
    }
}