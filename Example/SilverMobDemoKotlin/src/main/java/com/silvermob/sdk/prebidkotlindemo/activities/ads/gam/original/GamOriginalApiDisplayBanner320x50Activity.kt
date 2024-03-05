/*
 *    Copyright 2018-2019 Prebid.org, Inc.
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
package com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original

import android.os.Bundle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.silvermob.sdk.BannerAdUnit
import com.silvermob.sdk.BannerParameters
import com.silvermob.sdk.addendum.PbFindSizeError
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class GamOriginalApiDisplayBanner320x50Activity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid_demo_app_original_api_banner"
        const val CONFIG_ID = "13c4f9d0-6d7d-4398-8e39-f08052acbc70-UNIT-1"
        const val WIDTH = 320
        const val HEIGHT = 50
    }

    private var adUnit: com.silvermob.sdk.BannerAdUnit? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        val adView = AdManagerAdView(this)
        adView.adUnitId = AD_UNIT_ID
        adView.setAdSizes(AdSize(WIDTH, HEIGHT))
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(adView, object : com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener {
                    override fun success(width: Int, height: Int) {
                        adView.setAdSizes(AdSize(width, height))
                    }

                    override fun failure(error: PbFindSizeError) {}
                })

            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
            }
        }
        adWrapperView.addView(adView)

        val request = AdManagerAdRequest.Builder().build()
        adUnit = BannerAdUnit(CONFIG_ID, WIDTH, HEIGHT)

        val parameters = BannerParameters()
        parameters.api = listOf(com.silvermob.sdk.Signals.Api.MRAID_3, com.silvermob.sdk.Signals.Api.OMID_1)
        adUnit?.bannerParameters = parameters

        adUnit?.setAutoRefreshInterval(refreshTimeSeconds)
        adUnit?.fetchDemand(request) {
            adView.loadAd(request)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        adUnit?.stopAutoRefresh()
    }

}
