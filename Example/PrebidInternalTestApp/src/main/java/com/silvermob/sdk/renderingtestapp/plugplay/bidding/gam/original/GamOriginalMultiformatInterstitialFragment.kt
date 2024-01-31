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
import android.view.View
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.BannerParameters
import com.silvermob.sdk.VideoParameters
import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.api.original.PrebidAdUnit
import com.silvermob.sdk.api.original.PrebidRequest
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.plugplay.bidding.base.BaseBidInterstitialFragment

open class GamOriginalMultiformatInterstitialFragment : BaseBidInterstitialFragment() {

    companion object {
        private const val CONFIG_ID_BANNER = "prebid-demo-display-interstitial-320-480"
        private const val CONFIG_ID_VIDEO = "prebid-demo-video-interstitial-320-480-original-api"
    }

    private var prebidAdUnit: com.silvermob.sdk.api.original.PrebidAdUnit? = null
    private var displayAdCallback: (() -> Unit)? = null

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)
        binding.btnLoad.setOnClickListener {
            handleOriginalInterstitialClick()
        }
    }

    override fun initInterstitialAd(
            adUnitFormat: com.silvermob.sdk.api.data.AdUnitFormat,
            adUnitId: String?,
            configId: String?,
            width: Int,
            height: Int
    ) {
        createAd()
    }

    private fun createAd() {
        prebidAdUnit = com.silvermob.sdk.api.original.PrebidAdUnit(getCurrentConfigId())

        val gamRequest = AdManagerAdRequest.Builder().build()
        prebidAdUnit?.fetchDemand(gamRequest, createPrebidRequest()) {
            AdManagerInterstitialAd.load(
                requireContext(),
                adUnitId,
                gamRequest,
                createAdLoadCallback()
            )
        }
    }

    protected open fun getCurrentConfigId(): String {
        return listOf(CONFIG_ID_BANNER, CONFIG_ID_VIDEO).random()
    }

    protected open fun createPrebidRequest(): com.silvermob.sdk.api.original.PrebidRequest {
        val bannerParameters = com.silvermob.sdk.BannerParameters().apply {
            interstitialMinWidthPercentage = 80
            interstitialMinHeightPercentage = 80
        }

        val videoParameters = com.silvermob.sdk.VideoParameters(listOf("video/mp4")).apply {
            adSize = com.silvermob.sdk.AdSize(320, 480)
        }

        val prebidRequest = com.silvermob.sdk.api.original.PrebidRequest()
        prebidRequest.setInterstitial(true)
        prebidRequest.setBannerParameters(bannerParameters)
        prebidRequest.setVideoParameters(videoParameters)
        return prebidRequest
    }

    private fun handleOriginalInterstitialClick() {
        when (binding.btnLoad.text) {
            getString(R.string.text_load) -> {
                binding.btnLoad.isEnabled = false
                resetEventButtons()
                createAd()
            }

            getString(R.string.text_show) -> {
                binding.btnLoad.text = getString(R.string.text_load)
                displayAdCallback?.invoke()
            }
        }
    }

    private fun createAdLoadCallback(): AdManagerInterstitialAdLoadCallback {
        return object : AdManagerInterstitialAdLoadCallback() {
            override fun onAdLoaded(adManagerInterstitialAd: AdManagerInterstitialAd) {
                super.onAdLoaded(adManagerInterstitialAd)
                events.loaded(true)
                displayAdCallback = {
                    adManagerInterstitialAd.show(requireActivity())
                }
                binding.btnLoad.setText(R.string.text_show)
                binding.btnLoad.isEnabled = true
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                events.failed(true)
                binding.btnLoad.isEnabled = true
            }
        }
    }

}