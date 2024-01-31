package com.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.original

import com.silvermob.sdk.BannerAdUnit
import com.silvermob.sdk.VideoParameters
import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.renderingtestapp.R
import java.util.*
import kotlin.random.Random

class GamOriginalBannerRandomMultiformatFragment : GamOriginalBannerFragment() {

    override fun createAdUnit(): com.silvermob.sdk.BannerAdUnit {
        val configId =
            if (Random.nextBoolean()) getString(R.string.imp_prebid_id_banner_300x250) else getString(R.string.imp_prebid_id_video_outstream_original_api)
        val adUnit = com.silvermob.sdk.BannerAdUnit(configId, width, height, EnumSet.of(com.silvermob.sdk.api.data.AdUnitFormat.DISPLAY, com.silvermob.sdk.api.data.AdUnitFormat.VIDEO))
        adUnit.videoParameters = com.silvermob.sdk.VideoParameters(listOf("video/mp4"))
        return adUnit
    }

}