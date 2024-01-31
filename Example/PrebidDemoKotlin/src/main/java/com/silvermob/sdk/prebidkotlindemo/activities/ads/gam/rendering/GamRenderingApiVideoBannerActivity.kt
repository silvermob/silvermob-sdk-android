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
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.api.data.VideoPlacementType
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.eventhandlers.GamBannerEventHandler
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity


class GamRenderingApiVideoBannerActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid_oxb_300x250_banner"
        const val CONFIG_ID = "prebid-demo-video-outstream"
        const val WIDTH = 300
        const val HEIGHT = 250
    }

    private var adView: com.silvermob.sdk.api.rendering.BannerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        val eventHandler = com.silvermob.sdk.eventhandlers.GamBannerEventHandler(this, AD_UNIT_ID, com.silvermob.sdk.AdSize(WIDTH, HEIGHT))
        adView = com.silvermob.sdk.api.rendering.BannerView(this, CONFIG_ID, eventHandler)
        adView?.setAutoRefreshDelay(refreshTimeSeconds)

        // For Video
        adView?.videoPlacementType = com.silvermob.sdk.api.data.VideoPlacementType.IN_BANNER

        adWrapperView.addView(adView)
        adView?.loadAd()
    }


    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

}
