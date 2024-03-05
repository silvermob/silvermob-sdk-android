package com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original

import android.os.Bundle
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity
import java.util.EnumSet

class GamOriginalApiVideoBannerActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-original-api-video-banner"
        const val CONFIG_ID = "prebid-demo-video-outstream-original-api"
        const val WIDTH = 300
        const val HEIGHT = 250
    }

    private var adUnit: com.silvermob.sdk.BannerAdUnit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        // 1. Create BannerAdUnit
        adUnit = com.silvermob.sdk.BannerAdUnit(CONFIG_ID, WIDTH, HEIGHT, EnumSet.of(com.silvermob.sdk.api.data.AdUnitFormat.VIDEO))

        // 2. Configure video ad unit
        adUnit?.videoParameters = configureVideoParameters()

        // 3. Create AdManagerAdView
        val gamView = AdManagerAdView(this)
        gamView.adUnitId = AD_UNIT_ID
        gamView.setAdSizes(AdSize(WIDTH, HEIGHT))
        gamView.adListener = createListener(gamView)

        adWrapperView.addView(gamView)

        // 4. Make an ad request
        val request = AdManagerAdRequest.Builder().build()
        adUnit?.fetchDemand(request) {

            // 5. Load an GAM ad
            gamView.loadAd(request)
        }
    }

    private fun configureVideoParameters(): com.silvermob.sdk.VideoParameters {
        return com.silvermob.sdk.VideoParameters(listOf("video/x-flv", "video/mp4")).apply {

            api = listOf(
                com.silvermob.sdk.Signals.Api.VPAID_1,
                com.silvermob.sdk.Signals.Api.VPAID_2
            )

            maxBitrate = 1500
            minBitrate = 300
            maxDuration = 30
            minDuration = 5
            playbackMethod = listOf(com.silvermob.sdk.Signals.PlaybackMethod.AutoPlaySoundOn)
            protocols = listOf(
                com.silvermob.sdk.Signals.Protocols.VAST_2_0
            )
        }
    }

    private fun createListener(gamView: AdManagerAdView): AdListener {
        return object : AdListener() {
            override fun onAdLoaded() {

                // 6. Adjust ad view size
                com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(gamView, object : PbFindSizeListener {
                    override fun success(width: Int, height: Int) {
                        gamView.setAdSizes(AdSize(width, height))
                    }

                    override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adUnit?.stopAutoRefresh()
    }

}
