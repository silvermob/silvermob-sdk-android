package com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original

import android.os.Bundle
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.silvermob.sdk.NativeEventTracker
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class GamOriginalApiNativeStylesActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-original-native-styles"
        const val CONFIG_ID = "prebid-demo-banner-native-styles"
    }

    private var nativeAdUnit: com.silvermob.sdk.NativeAdUnit? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        // 1. Create Ad unit
        nativeAdUnit = com.silvermob.sdk.NativeAdUnit(CONFIG_ID)
        nativeAdUnit?.setContextType(com.silvermob.sdk.NativeAdUnit.CONTEXT_TYPE.SOCIAL_CENTRIC)
        nativeAdUnit?.setPlacementType(com.silvermob.sdk.NativeAdUnit.PLACEMENTTYPE.CONTENT_FEED)
        nativeAdUnit?.setContextSubType(com.silvermob.sdk.NativeAdUnit.CONTEXTSUBTYPE.GENERAL_SOCIAL)

        // 2. Configure Native Assets and Trackers
        addNativeAssets(nativeAdUnit)

        // 3. Create GAM Ad View
        val gamView = AdManagerAdView(this)
        gamView.adUnitId = AD_UNIT_ID
        gamView.setAdSizes(AdSize.FLUID)
        adWrapperView.addView(gamView)

        // 4. Make a bid request to Prebid Server
        val request = AdManagerAdRequest.Builder().build()
        nativeAdUnit?.fetchDemand(request) {

            // 5. Load a GAM Ad
            gamView.loadAd(request)
        }
    }

    private fun addNativeAssets(adUnit: com.silvermob.sdk.NativeAdUnit?)  {
        // ADD ASSETS

        val title = com.silvermob.sdk.NativeTitleAsset()
        title.setLength(90)
        title.isRequired = true
        adUnit?.addAsset(title)

        val icon = com.silvermob.sdk.NativeImageAsset(20, 20, 20, 20)
        icon.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.ICON
        icon.isRequired = true
        adUnit?.addAsset(icon)

        val image = com.silvermob.sdk.NativeImageAsset(200, 200, 200, 200)
        image.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.MAIN
        image.isRequired = true
        adUnit?.addAsset(image)

        val data = com.silvermob.sdk.NativeDataAsset()
        data.len = 90
        data.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.SPONSORED
        data.isRequired = true
        adUnit?.addAsset(data)

        val body = com.silvermob.sdk.NativeDataAsset()
        body.isRequired = true
        body.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.DESC
        adUnit?.addAsset(body)

        val cta = com.silvermob.sdk.NativeDataAsset()
        cta.isRequired = true
        cta.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.CTATEXT
        adUnit?.addAsset(cta)

        // ADD EVENT TRACKERS
        val methods = ArrayList<com.silvermob.sdk.NativeEventTracker.EVENT_TRACKING_METHOD>()
        methods.add(com.silvermob.sdk.NativeEventTracker.EVENT_TRACKING_METHOD.IMAGE)

        val tracker = com.silvermob.sdk.NativeEventTracker(NativeEventTracker.EVENT_TYPE.IMPRESSION, methods)
        adUnit?.addEventTracker(tracker)
    }

    override fun onDestroy() {
        super.onDestroy()

        nativeAdUnit?.stopAutoRefresh()
    }

}
