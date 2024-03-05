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

import android.os.Bundle
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.adapters.prebid.utils.MaxMediationInterstitialUtils
import com.applovin.mediation.ads.MaxInterstitialAd
import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.api.mediation.MediationInterstitialAdUnit
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity
import java.util.EnumSet

class AppLovinMaxDisplayInterstitialActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "df7ef64649dec96c"
    }

    private var maxInterstitialAd: MaxInterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        maxInterstitialAd = MaxInterstitialAd(AD_UNIT_ID, this)
        maxInterstitialAd?.setListener(object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd?) {
                maxInterstitialAd?.showAd()
            }

            override fun onAdDisplayed(ad: MaxAd?) {}
            override fun onAdHidden(ad: MaxAd?) {}
            override fun onAdClicked(ad: MaxAd?) {}
            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {}
            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {}
        })
        maxInterstitialAd?.loadAd()
    }


    override fun onDestroy() {
        super.onDestroy()
        maxInterstitialAd?.destroy()
    }

}
