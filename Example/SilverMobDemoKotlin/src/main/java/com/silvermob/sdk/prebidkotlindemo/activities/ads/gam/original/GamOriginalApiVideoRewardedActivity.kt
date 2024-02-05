package com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original

import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.silvermob.sdk.RewardedVideoAdUnit
import com.silvermob.sdk.Signals
import com.silvermob.sdk.VideoParameters
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class GamOriginalApiVideoRewardedActivity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-app-original-api-video-interstitial"
        const val CONFIG_ID = "prebid-demo-video-rewarded-320-480-original-api"
    }

    private var adUnit: com.silvermob.sdk.RewardedVideoAdUnit? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        // 1. Create RewardedVideoAdUnit
        adUnit = com.silvermob.sdk.RewardedVideoAdUnit(CONFIG_ID)

        // 2. Configure Video parameters
        adUnit?.videoParameters = configureVideoParameters()

        // 3. Make a bid request to Prebid Server
        val request = AdManagerAdRequest.Builder().build()
        adUnit?.fetchDemand(request) {

            // 4. Load a GAM Rewarded Ad
            RewardedAd.load(
                this,
                AD_UNIT_ID,
                request,
                createListener()
            )
        }
    }

    private fun configureVideoParameters(): com.silvermob.sdk.VideoParameters {
        return com.silvermob.sdk.VideoParameters(listOf("video/mp4")).apply {
            protocols = listOf(com.silvermob.sdk.Signals.Protocols.VAST_2_0)
            playbackMethod = listOf(com.silvermob.sdk.Signals.PlaybackMethod.AutoPlaySoundOff)
        }
    }

    private fun createListener(): RewardedAdLoadCallback {
        return object : RewardedAdLoadCallback() {
            override fun onAdLoaded(rewardedAd: RewardedAd) {

                // 5. Display rewarded ad
                rewardedAd.show(
                    this@GamOriginalApiVideoRewardedActivity
                ) { }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.e("GAM", "Ad failed to load: $loadAdError")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        adUnit?.stopAutoRefresh()
    }

}
