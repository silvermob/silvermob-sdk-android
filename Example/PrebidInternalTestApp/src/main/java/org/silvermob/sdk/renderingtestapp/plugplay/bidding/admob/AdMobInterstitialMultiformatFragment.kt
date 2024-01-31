package org.silvermob.sdk.renderingtestapp.plugplay.bidding.admob

import android.os.Bundle
import org.silvermob.sdk.admob.AdMobMediationInterstitialUtils
import org.silvermob.sdk.api.data.AdUnitFormat
import org.silvermob.sdk.api.mediation.MediationInterstitialAdUnit
import org.silvermob.sdk.renderingtestapp.R
import java.util.*

open class AdMobInterstitialMultiformatFragment : AdMobInterstitialFragment() {

    override fun initAd(): Any? {
        val context = requireContext()

        extras = Bundle()
        val mediationUtils = AdMobMediationInterstitialUtils(extras)
        val adUnitFormats = EnumSet.of(AdUnitFormat.DISPLAY, AdUnitFormat.VIDEO)
        adUnit = MediationInterstitialAdUnit(
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