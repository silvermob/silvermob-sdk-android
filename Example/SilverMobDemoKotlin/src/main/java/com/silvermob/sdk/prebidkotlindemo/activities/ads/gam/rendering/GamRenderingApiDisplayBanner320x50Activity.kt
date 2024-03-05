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
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class GamRenderingApiDisplayBanner320x50Activity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "13c4f9d0-6d7d-4398-8e39-f08052acbc70-UNIT-1"
        const val CONFIG_ID = "13c4f9d0-6d7d-4398-8e39-f08052acbc70-UNIT-1"
        const val WIDTH = 320
        const val HEIGHT = 50
    }

    private var adView: BannerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        val eventHandler = com.silvermob.sdk.eventhandlers.GamBannerEventHandler(this, AD_UNIT_ID, com.silvermob.sdk.AdSize(WIDTH, HEIGHT))
        adView = com.silvermob.sdk.api.rendering.BannerView(this, CONFIG_ID, eventHandler)
        adWrapperView.addView(adView)
        adView?.setAutoRefreshDelay(refreshTimeSeconds)
        adView?.loadAd()
    }


    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

}
