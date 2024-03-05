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
package com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.rendering

import android.os.Bundle
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class GamRenderingApiVideoRewardedActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-app-original-api-video-interstitial"
        const val CONFIG_ID = "prebid-demo-video-rewarded-320-480"
    }

    private var adUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        val eventHandler = com.silvermob.sdk.eventhandlers.GamRewardedEventHandler(this, AD_UNIT_ID)
        adUnit = com.silvermob.sdk.api.rendering.RewardedAdUnit(this, CONFIG_ID, eventHandler)
        adUnit?.setRewardedAdUnitListener(object : com.silvermob.sdk.api.rendering.listeners.RewardedAdUnitListener {
            override fun onAdLoaded(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {
                adUnit?.show()
            }

            override fun onAdDisplayed(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {}
            override fun onAdFailed(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?, exception: com.silvermob.sdk.api.exceptions.AdException?) {}
            override fun onAdClicked(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {}
            override fun onAdClosed(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {}
            override fun onUserEarnedReward(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {}
        })
        adUnit?.loadAd()
    }


    override fun onDestroy() {
        super.onDestroy()
        adUnit?.destroy()
    }

}
