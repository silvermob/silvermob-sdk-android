package com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original

import android.os.Bundle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.silvermob.sdk.*
import com.silvermob.sdk.addendum.AdViewUtils
import com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener
import com.silvermob.sdk.addendum.PbFindSizeError
import com.silvermob.sdk.api.original.PrebidAdUnit
import com.silvermob.sdk.api.original.PrebidRequest
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class GamOriginalApiMultiformatBannerVideoNativeStylesActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-multiformat-native-styles"
        const val CONFIG_ID_BANNER = "prebid-demo-banner-300-250"
        const val CONFIG_ID_NATIVE = "prebid-demo-banner-native-styles"
        const val CONFIG_ID_VIDEO = "prebid-demo-video-outstream-original-api"
    }

    private var prebidAdUnit: com.silvermob.sdk.api.original.PrebidAdUnit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        // Random only for test cases. For production use one config id.
        val configId = listOf(CONFIG_ID_BANNER, CONFIG_ID_VIDEO, CONFIG_ID_NATIVE).random()

        // 1. Create PrebidAdUnit with configId
        prebidAdUnit = com.silvermob.sdk.api.original.PrebidAdUnit(configId)

        // 2. Create PrebidRequest with needed multiformat parameters
        val prebidRequest = com.silvermob.sdk.api.original.PrebidRequest()
        prebidRequest.setBannerParameters(createBannerParameters())
        prebidRequest.setVideoParameters(createVideoParameters())
        prebidRequest.setNativeParameters(createNativeParameters())

        // 3. Make a bid request to Prebid Server
        val gamRequest = AdManagerAdRequest.Builder().build()
        prebidAdUnit?.fetchDemand(gamRequest, prebidRequest) {
            loadGam(gamRequest)
        }
    }

    private fun loadGam(gamRequest: AdManagerAdRequest) {
        // 4. Load GAM ad
        val gamView = AdManagerAdView(this)
        gamView.adUnitId = AD_UNIT_ID
        gamView.setAdSizes(AdSize.FLUID, AdSize.BANNER, AdSize.MEDIUM_RECTANGLE)
        gamView.adListener = createListener(gamView)
        gamView.loadAd(gamRequest)
        adWrapperView.addView(gamView)
    }

    private fun createListener(gamView: AdManagerAdView): AdListener {
        return object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                // 5. Update ad view
                com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(gamView, object : PbFindSizeListener {
                    override fun success(width: Int, height: Int) {
                        gamView.setAdSizes(AdSize(width, height))
                    }

                    override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
                })
            }
        }
    }


    private fun createBannerParameters(): com.silvermob.sdk.BannerParameters {
        val params = com.silvermob.sdk.BannerParameters()
        params.adSizes = mutableSetOf(com.silvermob.sdk.AdSize(300, 250))
        return params
    }

    private fun createVideoParameters(): com.silvermob.sdk.VideoParameters {
        val params = com.silvermob.sdk.VideoParameters(listOf("video/mp4"))
        params.adSize = com.silvermob.sdk.AdSize(320, 480)
        return params
    }

    private fun createNativeParameters(): com.silvermob.sdk.NativeParameters {
        val assets = mutableListOf<com.silvermob.sdk.NativeAsset>()

        val title = com.silvermob.sdk.NativeTitleAsset()
        title.setLength(90)
        title.isRequired = true
        assets.add(title)

        val icon = com.silvermob.sdk.NativeImageAsset(20, 20, 20, 20)
        icon.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.ICON
        icon.isRequired = true
        assets.add(icon)

        val image = com.silvermob.sdk.NativeImageAsset(200, 200, 200, 200)
        image.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.MAIN
        image.isRequired = true
        assets.add(image)

        val data = com.silvermob.sdk.NativeDataAsset()
        data.len = 90
        data.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.SPONSORED
        data.isRequired = true
        assets.add(data)

        val body = com.silvermob.sdk.NativeDataAsset()
        body.isRequired = true
        body.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.DESC
        assets.add(body)

        val cta = com.silvermob.sdk.NativeDataAsset()
        cta.isRequired = true
        cta.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.CTATEXT
        assets.add(cta)

        val nativeParameters = com.silvermob.sdk.NativeParameters(assets)
        nativeParameters.addEventTracker(
                com.silvermob.sdk.NativeEventTracker(
                        com.silvermob.sdk.NativeEventTracker.EVENT_TYPE.IMPRESSION,
                        arrayListOf(NativeEventTracker.EVENT_TRACKING_METHOD.IMAGE)
                )
        )
        nativeParameters.setContextType(com.silvermob.sdk.NativeAdUnit.CONTEXT_TYPE.SOCIAL_CENTRIC)
        nativeParameters.setPlacementType(com.silvermob.sdk.NativeAdUnit.PLACEMENTTYPE.CONTENT_FEED)
        nativeParameters.setContextSubType(com.silvermob.sdk.NativeAdUnit.CONTEXTSUBTYPE.GENERAL_SOCIAL)

        return nativeParameters
    }

    override fun onDestroy() {
        super.onDestroy()
        prebidAdUnit?.destroy()
    }

}
