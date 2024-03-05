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
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.plugplay.bidding.base.BaseBidInterstitialFragment

open class GamOriginalMultiformatRewardedFragment : BaseBidInterstitialFragment() {

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
        prebidAdUnit = com.silvermob.sdk.api.original.PrebidAdUnit(configId)

        val gamRequest = AdManagerAdRequest.Builder().build()
        prebidAdUnit?.fetchDemand(gamRequest, createPrebidRequest()) {
            RewardedAd.load(
                requireContext(),
                adUnitId,
                gamRequest,
                createAdLoadCallback()
            )
        }
    }

    protected open fun createPrebidRequest(): com.silvermob.sdk.api.original.PrebidRequest {
        val videoParameters = com.silvermob.sdk.VideoParameters(listOf("video/mp4")).apply {
            adSize = com.silvermob.sdk.AdSize(320, 480)
            protocols = listOf(com.silvermob.sdk.Signals.Protocols.VAST_2_0)
            playbackMethod = listOf(com.silvermob.sdk.Signals.PlaybackMethod.AutoPlaySoundOff)
        }

        val prebidRequest = com.silvermob.sdk.api.original.PrebidRequest()
        prebidRequest.setRewarded(true)
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

    private fun createAdLoadCallback(): RewardedAdLoadCallback {
        return object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                super.onAdLoaded(ad)
                events.loaded(true)
                displayAdCallback = {
                    ad.show(requireActivity()) {}
                }
                binding.btnLoad.setText(R.string.text_show)
                binding.btnLoad.isEnabled = true
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                events.failed(true)
                binding.btnLoad.isEnabled = true
            }
        }
    }

}