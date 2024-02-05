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
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.silvermob.sdk.BannerAdUnit
import com.silvermob.sdk.BannerParameters
import com.silvermob.sdk.Signals
import com.silvermob.sdk.addendum.AdViewUtils
import com.silvermob.sdk.addendum.PbFindSizeError
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class GamOriginalApiDisplayBannerMultiSizeActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid_demo_app_original_api_banner_multisize"
        const val CONFIG_ID = "prebid-demo-banner-multisize"
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

                    override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
                })

            }
        }
        adWrapperView.addView(adView)

        val request = AdManagerAdRequest.Builder().build()
        adUnit = com.silvermob.sdk.BannerAdUnit(CONFIG_ID, WIDTH, HEIGHT)
        adUnit?.setAutoRefreshInterval(refreshTimeSeconds)

        val parameters = com.silvermob.sdk.BannerParameters()
        parameters.api = listOf(com.silvermob.sdk.Signals.Api.MRAID_3, com.silvermob.sdk.Signals.Api.OMID_1)
        adUnit?.bannerParameters = parameters

        // For multi-size request
        adUnit?.addAdditionalSize(728, 90)

        adUnit?.fetchDemand(request) {
            adView.loadAd(request)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        adUnit?.stopAutoRefresh()
    }

}
