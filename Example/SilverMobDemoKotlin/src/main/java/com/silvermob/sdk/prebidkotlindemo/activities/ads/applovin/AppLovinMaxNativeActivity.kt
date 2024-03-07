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
import android.util.Log
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.silvermob.sdk.NativeEventTracker
import com.silvermob.sdk.prebidkotlindemo.R
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class AppLovinMaxNativeActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "f3bdfa9dd8da1c4d"
        const val CONFIG_ID = "prebid-demo-banner-native-styles"
    }

    private var nativeAdLoader: MaxNativeAdLoader? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        nativeAdLoader = MaxNativeAdLoader(AD_UNIT_ID, this)
        nativeAdLoader?.setRevenueListener {}
        nativeAdLoader?.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, nativeAd: MaxAd) {
                adWrapperView.removeAllViews()
                adWrapperView.addView(nativeAdView)
            }

            override fun onNativeAdLoadFailed(p0: String, p1: MaxError) {
                Log.e("AppLovinMaxNative", "Failed to load: $p0")
            }

            override fun onNativeAdClicked(p0: MaxAd) {}
        })

        val nativeAdUnit = configureNativeAdUnit()
        nativeAdUnit.fetchDemand(nativeAdLoader ?: return) {
            nativeAdLoader?.loadAd(createNativeAdView())
        }
    }


    private fun configureNativeAdUnit(): com.silvermob.sdk.NativeAdUnit {
        val nativeAdUnit = com.silvermob.sdk.NativeAdUnit(CONFIG_ID)

        nativeAdUnit.setContextType(com.silvermob.sdk.NativeAdUnit.CONTEXT_TYPE.SOCIAL_CENTRIC)
        nativeAdUnit.setPlacementType(com.silvermob.sdk.NativeAdUnit.PLACEMENTTYPE.CONTENT_FEED)
        nativeAdUnit.setContextSubType(com.silvermob.sdk.NativeAdUnit.CONTEXTSUBTYPE.GENERAL_SOCIAL)

        val methods: ArrayList<com.silvermob.sdk.NativeEventTracker.EVENT_TRACKING_METHOD> = ArrayList()
        methods.add(com.silvermob.sdk.NativeEventTracker.EVENT_TRACKING_METHOD.IMAGE)
        methods.add(com.silvermob.sdk.NativeEventTracker.EVENT_TRACKING_METHOD.JS)
        try {
            val tracker = com.silvermob.sdk.NativeEventTracker(NativeEventTracker.EVENT_TYPE.IMPRESSION, methods)
            nativeAdUnit.addEventTracker(tracker)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val title = com.silvermob.sdk.NativeTitleAsset()
        title.setLength(90)
        title.isRequired = true
        nativeAdUnit.addAsset(title)

        val icon = com.silvermob.sdk.NativeImageAsset(20, 20, 20, 20)
        icon.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.ICON
        icon.isRequired = true
        nativeAdUnit.addAsset(icon)

        val image = com.silvermob.sdk.NativeImageAsset(200, 200, 200, 200)
        image.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.MAIN
        image.isRequired = true
        nativeAdUnit.addAsset(image)

        val data = com.silvermob.sdk.NativeDataAsset()
        data.len = 90
        data.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.SPONSORED
        data.isRequired = true
        nativeAdUnit.addAsset(data)

        val body = com.silvermob.sdk.NativeDataAsset()
        body.isRequired = true
        body.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.DESC
        nativeAdUnit.addAsset(body)

        val cta = com.silvermob.sdk.NativeDataAsset()
        cta.isRequired = true
        cta.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.CTATEXT
        nativeAdUnit.addAsset(cta)

        return nativeAdUnit
    }

    private fun createNativeAdView(): MaxNativeAdView {
        val binder = MaxNativeAdViewBinder.Builder(R.layout.view_native_ad_max)
            .setTitleTextViewId(R.id.tvHeadline)
            .setBodyTextViewId(R.id.tvBody)
            .setIconImageViewId(R.id.imgIco)
            .setMediaContentViewGroupId(R.id.frameMedia)
            .setCallToActionButtonId(R.id.btnCallToAction)
            .build()
        return MaxNativeAdView(binder, this)
    }


    override fun onDestroy() {
        super.onDestroy()
        nativeAdLoader?.destroy()
    }

}
