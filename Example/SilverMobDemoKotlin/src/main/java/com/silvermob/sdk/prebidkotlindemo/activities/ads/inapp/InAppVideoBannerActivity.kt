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
import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.api.data.VideoPlacementType
import com.silvermob.sdk.api.exceptions.AdException
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.api.rendering.listeners.BannerViewListener
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity
import java.util.EnumSet

class InAppVideoBannerActivity : BaseAdActivity() {

    companion object {
        const val CONFIG_ID = "13c4f9d0-6d7d-4398-8e39-f08052acbc70-UNIT-1"
        const val WIDTH = 300
        const val HEIGHT = 250
    }

    private var bannerView: BannerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        bannerView = BannerView(
                this,
                CONFIG_ID,
                AdSize(WIDTH, HEIGHT),
        )

        //bannerView?.videoPlacementType = VideoPlacementType.IN_BANNER
        bannerView?.setBannerListener(object : BannerViewListener {
            override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {
                Log.e("InAppVideoBanner", "Ad failed: ${exception?.message}")
            }

            override fun onAdLoaded(bannerView: BannerView?) {}
            override fun onAdClicked(bannerView: BannerView?) {}
            override fun onAdDisplayed(bannerView: BannerView?) {}
            override fun onAdClosed(bannerView: BannerView?) {}
        })
        bannerView?.loadAd()

        adWrapperView.addView(bannerView)
    }


    override fun onDestroy() {
        super.onDestroy()
        bannerView?.destroy()
    }

}
