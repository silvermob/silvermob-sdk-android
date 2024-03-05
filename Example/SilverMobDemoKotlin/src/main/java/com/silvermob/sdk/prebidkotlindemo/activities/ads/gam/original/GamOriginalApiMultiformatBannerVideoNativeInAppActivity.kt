package com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.formats.AdManagerAdViewOptions
import com.google.android.gms.ads.formats.OnAdManagerAdViewLoadedListener
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import com.google.android.gms.ads.nativead.NativeCustomFormatAd.OnCustomFormatAdLoadedListener
import com.google.common.collect.Lists
import com.silvermob.sdk.NativeEventTracker
import com.silvermob.sdk.prebidkotlindemo.R
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity
import com.silvermob.sdk.prebidkotlindemo.utils.ImageUtils

class GamOriginalApiMultiformatBannerVideoNativeInAppActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-multiformat"
        const val CONFIG_ID_BANNER = "prebid-demo-banner-300-250"
        const val CONFIG_ID_NATIVE = "prebid-demo-banner-native-styles"
        const val CONFIG_ID_VIDEO = "prebid-demo-video-outstream-original-api"
        const val CUSTOM_FORMAT_ID = "12304464"
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
        val onBannerLoaded = OnAdManagerAdViewLoadedListener { adView ->
            showBannerAd(adView)
        }

        val onNativeLoaded = OnNativeAdLoadedListener { nativeAd ->
            showNativeAd(nativeAd, adWrapperView)
        }

        val onPrebidNativeAdLoaded = OnCustomFormatAdLoadedListener { customNativeAd ->
            showPrebidNativeAd(customNativeAd)
        }

        val adLoader = AdLoader.Builder(this, AD_UNIT_ID)
            .forAdManagerAdView(onBannerLoaded, AdSize.BANNER, AdSize.MEDIUM_RECTANGLE)
            .forNativeAd(onNativeLoaded)
            .forCustomFormatAd(CUSTOM_FORMAT_ID, onPrebidNativeAdLoaded, null)
            .withAdListener(AdListenerWithToast(this))
            .withAdManagerAdViewOptions(AdManagerAdViewOptions.Builder().build())
            .build()

        adLoader.loadAd(gamRequest)
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

    private fun showBannerAd(adView: AdManagerAdView) {
        // 5.1. Show banner
        adWrapperView.addView(adView)
        com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(adView, object : com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener {
            override fun success(width: Int, height: Int) {
                adView.setAdSizes(AdSize(width, height))
            }

            override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
        })
    }

    private fun showNativeAd(ad: NativeAd, wrapper: ViewGroup) {
        // 5.2. Show GAM native
        val nativeContainer = View.inflate(wrapper.context, R.layout.layout_native, null)

        val icon = nativeContainer.findViewById<ImageView>(R.id.imgIcon)
        val iconUrl = ad.icon?.uri?.toString()
        if (iconUrl != null) {
            ImageUtils.download(iconUrl, icon)
        }

        val title = nativeContainer.findViewById<TextView>(R.id.tvTitle)
        title.text = ad.headline

        val image = nativeContainer.findViewById<ImageView>(R.id.imgImage)
        val imageUrl = ad.images.getOrNull(0)?.uri?.toString()
        if (imageUrl != null) {
            ImageUtils.download(imageUrl, image)
        }

        val description = nativeContainer.findViewById<TextView>(R.id.tvDesc)
        description.text = ad.body

        val cta = nativeContainer.findViewById<Button>(R.id.btnCta)
        cta.text = ad.callToAction

        wrapper.addView(nativeContainer)
    }

    private fun showPrebidNativeAd(customNativeAd: NativeCustomFormatAd) {
        // 5.3. Show Prebid native
        com.silvermob.sdk.addendum.AdViewUtils.findNative(customNativeAd, object : com.silvermob.sdk.PrebidNativeAdListener {
            override fun onPrebidNativeLoaded(ad: com.silvermob.sdk.PrebidNativeAd) {
                inflatePrebidNativeAd(ad)
            }

            override fun onPrebidNativeNotFound() {
                Log.e("PrebidAdViewUtils", "Find native failed: native not found")
            }

            override fun onPrebidNativeNotValid() {
                Log.e("PrebidAdViewUtils", "Find native failed: native not valid")
            }
        })
    }

    private fun inflatePrebidNativeAd(ad: com.silvermob.sdk.PrebidNativeAd) {
        val nativeContainer = View.inflate(this, R.layout.layout_native, null)

        val icon = nativeContainer.findViewById<ImageView>(R.id.imgIcon)
        ImageUtils.download(ad.iconUrl, icon)

        val title = nativeContainer.findViewById<TextView>(R.id.tvTitle)
        title.text = ad.title

        val image = nativeContainer.findViewById<ImageView>(R.id.imgImage)
        ImageUtils.download(ad.imageUrl, image)

        val description = nativeContainer.findViewById<TextView>(R.id.tvDesc)
        description.text = ad.description

        val cta = nativeContainer.findViewById<Button>(R.id.btnCta)
        cta.text = ad.callToAction

        ad.registerView(nativeContainer, Lists.newArrayList(icon, title, image, description, cta), null)

        adWrapperView.addView(nativeContainer)
    }


    override fun onDestroy() {
        super.onDestroy()
        prebidAdUnit?.destroy()
    }


    private class AdListenerWithToast(
        context: Context,
    ) : AdListener() {

        private val applicationContext = context.applicationContext

        override fun onAdFailedToLoad(adError: LoadAdError) {
            Toast.makeText(
                applicationContext,
                "Ad failed to load!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
