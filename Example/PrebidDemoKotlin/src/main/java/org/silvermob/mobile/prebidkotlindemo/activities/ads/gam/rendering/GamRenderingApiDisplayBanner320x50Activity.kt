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
package org.silvermob.mobile.prebidkotlindemo.activities.ads.gam.rendering

import android.os.Bundle
import org.silvermob.mobile.AdSize
import org.silvermob.mobile.api.rendering.BannerView
import org.silvermob.mobile.eventhandlers.GamBannerEventHandler
import org.silvermob.mobile.prebidkotlindemo.activities.BaseAdActivity

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
        val eventHandler = GamBannerEventHandler(this, AD_UNIT_ID, AdSize(WIDTH, HEIGHT))
        adView = BannerView(this, CONFIG_ID, eventHandler)
        adWrapperView.addView(adView)
        adView?.setAutoRefreshDelay(refreshTimeSeconds)
        adView?.loadAd()
    }


    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

}
