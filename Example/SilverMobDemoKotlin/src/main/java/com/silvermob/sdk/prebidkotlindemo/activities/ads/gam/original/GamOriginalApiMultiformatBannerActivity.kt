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
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity
import java.util.EnumSet
import java.util.Random


class GamOriginalApiMultiformatBannerActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-original-banner-multiformat"
        const val CONFIG_ID_BANNER = "prebid-demo-banner-300-250"
        const val CONFIG_ID_VIDEO = "prebid-demo-video-outstream-original-api"
        const val WIDTH = 300
        const val HEIGHT = 250
    }

    private var adUnit: com.silvermob.sdk.BannerAdUnit? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        val configId = if (Random().nextBoolean()) {
            CONFIG_ID_BANNER
        } else {
            CONFIG_ID_VIDEO
        }

        // 1. Create BannerAdUnit
        adUnit = com.silvermob.sdk.BannerAdUnit(configId, WIDTH, HEIGHT, EnumSet.of(com.silvermob.sdk.api.data.AdUnitFormat.BANNER, com.silvermob.sdk.api.data.AdUnitFormat.VIDEO))
        adUnit?.setAutoRefreshInterval(refreshTimeSeconds)

        // 2. Configure parameters
        val parameters = com.silvermob.sdk.BannerParameters()
        parameters.api = listOf(com.silvermob.sdk.Signals.Api.MRAID_3, com.silvermob.sdk.Signals.Api.OMID_1)
        adUnit?.bannerParameters = parameters
        adUnit?.videoParameters = com.silvermob.sdk.VideoParameters(listOf("video/mp4"))

        // 3. Create AdManagerAdView
        val adView = AdManagerAdView(this)
        adView.adUnitId = AD_UNIT_ID
        adView.setAdSizes(AdSize(WIDTH, HEIGHT))
        adView.adListener = createGAMListener(adView)

        // Add GMA SDK banner view to the app UI
        adWrapperView.addView(adView)

        // 4. Make a bid request to Prebid Server
        val request = AdManagerAdRequest.Builder().build()
        adUnit?.fetchDemand(request) {

            // 5. Load GAM Ad
            adView.loadAd(request)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adUnit?.stopAutoRefresh()
    }

    private fun createGAMListener(adView: AdManagerAdView): AdListener {
        return object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()

                // 6. Update ad view
                com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(adView, object : com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener {
                    override fun success(width: Int, height: Int) {
                        adView.setAdSizes(AdSize(width, height))
                    }

                    override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
                })
            }
        }
    }

}
