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
package com.silvermob.sdk.prebidkotlindemo.activities.ads.admob

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.silvermob.sdk.*
import com.silvermob.sdk.admob.PrebidNativeAdapter
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity
import com.silvermob.sdk.prebidkotlindemo.databinding.ViewNativeAdAdMobBinding

class AdMobNativeActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "ca-app-pub-1875909575462531/9720985924"
        const val CONFIG_ID = "prebid-demo-banner-native-styles"
    }

    private var nativeAd: NativeAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        val nativeAdOptions = NativeAdOptions
            .Builder()
            .build()
        val adLoader = AdLoader
            .Builder(this, AD_UNIT_ID)
            .forNativeAd { ad: NativeAd ->
                nativeAd = ad
                createCustomView(adWrapperView, nativeAd!!)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("AdMobNative", "Error: ${adError.message}")
                }
            })
            .withNativeAdOptions(nativeAdOptions)
            .build()

        val extras = Bundle()
        val adRequest = AdRequest
            .Builder()
            .addNetworkExtrasBundle(com.silvermob.sdk.admob.PrebidNativeAdapter::class.java, extras)
            .build()

        val nativeAdUnit = com.silvermob.sdk.NativeAdUnit(CONFIG_ID)
        configureNativeAdUnit(nativeAdUnit)
        nativeAdUnit.fetchDemand(extras) { resultCode ->
            Log.d("AdMobNative", "Fetch demand result: $resultCode")

            /** For mediation use loadAd() not loadAds() */
            adLoader.loadAd(adRequest)
        }
    }

    private fun createCustomView(wrapper: ViewGroup, nativeAd: NativeAd) {
        wrapper.removeAllViews()
        val binding = ViewNativeAdAdMobBinding.inflate(LayoutInflater.from(wrapper.context))

        binding.apply {
            tvHeadline.text = nativeAd.headline
            tvBody.text = nativeAd.body
            imgIco.setImageDrawable(nativeAd.icon?.drawable)
            imgMedia.mediaContent = nativeAd.mediaContent
        }

        binding.viewNativeWrapper.apply {
            headlineView = binding.tvHeadline
            bodyView = binding.tvBody
            iconView = binding.imgIco
            mediaView = binding.imgMedia
            setNativeAd(nativeAd)
        }

        wrapper.addView(binding.root)
    }

    private fun configureNativeAdUnit(nativeAdUnit: com.silvermob.sdk.NativeAdUnit) {
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
    }


    override fun onDestroy() {
        super.onDestroy()
        nativeAd?.destroy()
    }

}
