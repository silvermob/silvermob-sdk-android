package com.silvermob.sdk.renderingtestapp.plugplay.bidding.admob

import android.os.Bundle
import com.silvermob.sdk.renderingtestapp.R
import java.util.EnumSet

open class AdMobInterstitialMultiformatFragment : AdMobInterstitialFragment() {

    override fun initAd(): Any? {
        val context = requireContext()

        extras = Bundle()
        val mediationUtils = com.silvermob.sdk.admob.AdMobMediationInterstitialUtils(extras)
        val adUnitFormats = EnumSet.of(com.silvermob.sdk.api.data.AdUnitFormat.DISPLAY, com.silvermob.sdk.api.data.AdUnitFormat.VIDEO)
        adUnit = com.silvermob.sdk.api.mediation.MediationInterstitialAdUnit(
                activity,
                listOf(
                        context.getString(R.string.imp_prebid_id_interstitial_320_480),
                        context.getString(R.string.imp_prebid_id_video_interstitial_320_480)
                ).shuffled().first(),
                adUnitFormats,
                mediationUtils
        )
        adUnit?.setMinSizePercentage(30, 30)
        return adUnit
    }

}