package com.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.original

import com.silvermob.sdk.AdUnit
import com.silvermob.sdk.InterstitialAdUnit
import com.silvermob.sdk.VideoParameters
import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.renderingtestapp.R
import java.util.*
import kotlin.random.Random

class GamOriginalInterstitialRandomMultiformatFragment : GamOriginalInterstitialFragment() {

    override fun createAdUnit(adUnitFormat: com.silvermob.sdk.api.data.AdUnitFormat): com.silvermob.sdk.AdUnit {
        val configId =
            if (Random.nextBoolean()) getString(R.string.imp_prebid_id_interstitial_320_480) else getString(R.string.imp_prebid_id_video_interstitial_320_480_original_api)
        val adUnit = com.silvermob.sdk.InterstitialAdUnit(configId, EnumSet.of(com.silvermob.sdk.api.data.AdUnitFormat.DISPLAY, com.silvermob.sdk.api.data.AdUnitFormat.VIDEO))
        adUnit.videoParameters = com.silvermob.sdk.VideoParameters(listOf("video/mp4"))
        return adUnit
    }

}