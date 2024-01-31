package org.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.original

import org.silvermob.sdk.AdUnit
import org.silvermob.sdk.InterstitialAdUnit
import org.silvermob.sdk.VideoParameters
import org.silvermob.sdk.api.data.AdUnitFormat
import org.silvermob.sdk.renderingtestapp.R
import java.util.*
import kotlin.random.Random

class GamOriginalInterstitialRandomMultiformatFragment : GamOriginalInterstitialFragment() {

    override fun createAdUnit(adUnitFormat: AdUnitFormat): AdUnit {
        val configId =
            if (Random.nextBoolean()) getString(R.string.imp_prebid_id_interstitial_320_480) else getString(R.string.imp_prebid_id_video_interstitial_320_480_original_api)
        val adUnit = InterstitialAdUnit(configId, EnumSet.of(AdUnitFormat.DISPLAY, AdUnitFormat.VIDEO))
        adUnit.videoParameters = VideoParameters(listOf("video/mp4"))
        return adUnit
    }

}