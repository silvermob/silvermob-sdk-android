package org.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.original

import org.silvermob.sdk.BannerAdUnit
import org.silvermob.sdk.VideoParameters
import org.silvermob.sdk.api.data.AdUnitFormat
import org.silvermob.sdk.renderingtestapp.R
import java.util.*
import kotlin.random.Random

class GamOriginalBannerRandomMultiformatFragment : GamOriginalBannerFragment() {

    override fun createAdUnit(): BannerAdUnit {
        val configId =
            if (Random.nextBoolean()) getString(R.string.imp_prebid_id_banner_300x250) else getString(R.string.imp_prebid_id_video_outstream_original_api)
        val adUnit = BannerAdUnit(configId, width, height, EnumSet.of(AdUnitFormat.DISPLAY, AdUnitFormat.VIDEO))
        adUnit.videoParameters = VideoParameters(listOf("video/mp4"))
        return adUnit
    }

}