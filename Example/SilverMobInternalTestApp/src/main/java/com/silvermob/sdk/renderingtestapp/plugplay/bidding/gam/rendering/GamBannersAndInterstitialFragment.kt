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

package com.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.rendering

import android.widget.Button
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.api.rendering.InterstitialAdUnit
import com.silvermob.sdk.eventhandlers.GamBannerEventHandler
import com.silvermob.sdk.eventhandlers.GamInterstitialEventHandler
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.plugplay.bidding.ppm.PpmBannersWithInterstitialFragment

class GamBannersAndInterstitialFragment : PpmBannersWithInterstitialFragment() {
    override fun initBannerView(configId: String, refreshIntervalSec: Int, impressionCounterButton: Button?): com.silvermob.sdk.api.rendering.BannerView {
        val adSize = com.silvermob.sdk.AdSize(BANNER_WIDTH, BANNER_HEIGHT)
        val eventHandler =
                com.silvermob.sdk.eventhandlers.GamBannerEventHandler(requireContext(), getString(R.string.adunit_gam_banner_320_50_app_event), adSize)
        val bannerView =
                com.silvermob.sdk.api.rendering.BannerView(requireContext(), configId, eventHandler)
        bannerView.setAutoRefreshDelay(refreshIntervalSec)
        bannerView.setBannerListener(getBannerAdListener(configId, refreshIntervalSec, impressionCounterButton))
        return bannerView
    }

    override fun initInterstitialAdUnit(configId: String): com.silvermob.sdk.api.rendering.InterstitialAdUnit {
        val eventHandler = com.silvermob.sdk.eventhandlers.GamInterstitialEventHandler(
                requireActivity(),
                getString(R.string.adunit_gam_interstitial_320_480_app_event)
        )
        val interstitialAdUnit = com.silvermob.sdk.api.rendering.InterstitialAdUnit(
                requireContext(),
                configId,
                eventHandler
        )
        interstitialAdUnit.setMinSizePercentage(com.silvermob.sdk.AdSize(30, 30))
        interstitialAdUnit.setInterstitialAdUnitListener(getInterstitialAdListener())
        interstitialAdUnit.loadAd()
        return interstitialAdUnit
    }
}