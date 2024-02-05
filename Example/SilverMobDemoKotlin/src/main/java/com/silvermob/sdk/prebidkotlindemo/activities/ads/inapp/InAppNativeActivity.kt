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
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.silvermob.sdk.*
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity
import com.silvermob.sdk.prebidkotlindemo.utils.ImageUtils
import com.silvermob.sdk.rendering.utils.ntv.NativeAdProvider

class InAppNativeActivity : BaseAdActivity() {

    companion object {
        const val CONFIG_ID = "prebid-demo-banner-native-styles"
    }

    private var nativeAdUnit: com.silvermob.sdk.NativeAdUnit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        val extras = Bundle()
        nativeAdUnit = configureNativeAdUnit()
        nativeAdUnit?.fetchDemand(extras) {
            inflatePrebidNativeAd(com.silvermob.sdk.rendering.utils.ntv.NativeAdProvider.getNativeAd(extras)!!)
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

    private fun inflatePrebidNativeAd(ad: com.silvermob.sdk.PrebidNativeAd) {
        val nativeContainer = LinearLayout(this)
        nativeContainer.orientation = LinearLayout.VERTICAL
        val iconAndTitle = LinearLayout(this)
        iconAndTitle.orientation = LinearLayout.HORIZONTAL

        val icon = ImageView(this)
        icon.layoutParams = LinearLayout.LayoutParams(160, 160)
        ImageUtils.download(ad.iconUrl, icon)
        iconAndTitle.addView(icon)

        val title = TextView(this)
        title.textSize = 20f
        title.text = ad.title
        iconAndTitle.addView(title)
        nativeContainer.addView(iconAndTitle)

        val image = ImageView(this)
        image.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        ImageUtils.download(ad.imageUrl, image)
        nativeContainer.addView(image)

        val description = TextView(this)
        description.textSize = 18f
        description.text = ad.description
        nativeContainer.addView(description)

        val cta = Button(this)
        cta.text = ad.callToAction
        nativeContainer.addView(cta)

        adWrapperView.addView(nativeContainer)

        ad.registerView(
            adWrapperView,
            listOf(icon, title, image, description, cta),
            null
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeAdUnit?.destroy()
    }

}
