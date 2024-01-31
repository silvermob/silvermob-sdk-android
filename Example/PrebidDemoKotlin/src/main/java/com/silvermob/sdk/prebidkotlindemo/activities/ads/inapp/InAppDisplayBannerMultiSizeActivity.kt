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
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class InAppDisplayBannerMultiSizeActivity : BaseAdActivity() {

    companion object {
        const val CONFIG_ID = "prebid-demo-banner-multisize"
        const val WIDTH = 728
        const val HEIGHT = 90
    }

    private var adView: com.silvermob.sdk.api.rendering.BannerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        adView = com.silvermob.sdk.api.rendering.BannerView(
                this,
                CONFIG_ID,
                com.silvermob.sdk.AdSize(WIDTH, HEIGHT)
        )

        adWrapperView.addView(adView)
        adView?.setAutoRefreshDelay(refreshTimeSeconds)
        adView?.loadAd()
    }


    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

}
