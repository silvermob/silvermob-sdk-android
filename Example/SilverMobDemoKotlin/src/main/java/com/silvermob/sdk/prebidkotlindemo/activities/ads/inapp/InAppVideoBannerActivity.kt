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
import android.util.Log
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.api.data.VideoPlacementType
import com.silvermob.sdk.api.exceptions.AdException
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.api.rendering.listeners.BannerViewListener
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class InAppVideoBannerActivity : BaseAdActivity() {

    companion object {
        const val CONFIG_ID = "prebid-demo-video-outstream"
        const val WIDTH = 300
        const val HEIGHT = 250
    }

    private var bannerView: com.silvermob.sdk.api.rendering.BannerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        bannerView = com.silvermob.sdk.api.rendering.BannerView(
                this,
                CONFIG_ID,
                com.silvermob.sdk.AdSize(WIDTH, HEIGHT)
        )

        bannerView?.videoPlacementType = com.silvermob.sdk.api.data.VideoPlacementType.IN_BANNER
        bannerView?.setBannerListener(object : com.silvermob.sdk.api.rendering.listeners.BannerViewListener {
            override fun onAdFailed(bannerView: com.silvermob.sdk.api.rendering.BannerView?, exception: com.silvermob.sdk.api.exceptions.AdException?) {
                Log.e("InAppVideoBanner", "Ad failed: ${exception?.message}")
            }

            override fun onAdLoaded(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}
            override fun onAdClicked(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}
            override fun onAdDisplayed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}
            override fun onAdClosed(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {}
        })
        bannerView?.loadAd()

        adWrapperView.addView(bannerView)
    }


    override fun onDestroy() {
        super.onDestroy()
        bannerView?.destroy()
    }

}
