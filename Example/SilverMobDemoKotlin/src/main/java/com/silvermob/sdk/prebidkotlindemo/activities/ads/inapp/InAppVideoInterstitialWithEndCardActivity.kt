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
package com.silvermob.sdk.prebidkotlindemo.activities.ads.inapp

import android.os.Bundle
import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.api.exceptions.AdException
import com.silvermob.sdk.api.rendering.InterstitialAdUnit
import com.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity
import java.util.*

class InAppVideoInterstitialWithEndCardActivity : BaseAdActivity() {

    companion object {
        const val CONFIG_ID = "prebid-demo-video-interstitial-320-480-with-end-card"
    }

    private var adUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        adUnit = com.silvermob.sdk.api.rendering.InterstitialAdUnit(this, CONFIG_ID, EnumSet.of(AdUnitFormat.VIDEO))
        adUnit?.setInterstitialAdUnitListener(object : com.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener {
            override fun onAdLoaded(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
                adUnit?.show()
            }

            override fun onAdDisplayed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {}
            override fun onAdFailed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?, e: com.silvermob.sdk.api.exceptions.AdException?) {}
            override fun onAdClicked(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {}
            override fun onAdClosed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {}
        })
        adUnit?.loadAd()
    }


    override fun onDestroy() {
        super.onDestroy()
        adUnit?.destroy()
    }

}
