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
package com.silvermob.sdk.prebidkotlindemo.activities.ads.applovin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.adapters.prebid.utils.MaxMediationBannerUtils
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdkUtils
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class AppLovinMaxDisplayBanner300x250Activity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "9c13b9b0ed8e04d8"
        const val CONFIG_ID = "13c4f9d0-6d7d-4398-8e39-f08052acbc70-UNIT-1"
        const val WIDTH = 300
        const val HEIGHT = 250
    }

    private var adView: MaxAdView? = null
    private var adUnit: com.silvermob.sdk.api.mediation.MediationBannerAdUnit? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        adView = MaxAdView(AD_UNIT_ID, MaxAdFormat.MREC, this)
        adView?.setListener(object : MaxAdViewAdListener {
            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                Log.d("AppLovinMaxBanner", "onAdLoadFailed(): ${error?.message}")
            }

            override fun onAdLoaded(ad: MaxAd?) {}
            override fun onAdHidden(ad: MaxAd?) {}
            override fun onAdClicked(ad: MaxAd?) {}
            override fun onAdExpanded(ad: MaxAd?) {}
            override fun onAdCollapsed(ad: MaxAd?) {}
            override fun onAdDisplayed(ad: MaxAd?) {}
            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {}
        })
        adView?.setBackgroundColor(Color.TRANSPARENT)
        adView?.layoutParams = FrameLayout.LayoutParams(
            AppLovinSdkUtils.dpToPx(this, WIDTH),
            AppLovinSdkUtils.dpToPx(this, HEIGHT)
        )

        adWrapperView.addView(adView)

        val mediationUtils = MaxMediationBannerUtils(adView)
        adUnit = com.silvermob.sdk.api.mediation.MediationBannerAdUnit(
                this,
                CONFIG_ID,
                com.silvermob.sdk.AdSize(WIDTH, HEIGHT),
                mediationUtils
        )
        adUnit?.setRefreshInterval(refreshTimeSeconds)
        adUnit?.fetchDemand {
            adView?.loadAd()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        adUnit?.destroy()
        adView?.destroy()
    }

}
