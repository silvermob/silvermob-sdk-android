package org.silvermob.sdk.renderingtestapp.plugplay.bidding.max

import com.applovin.mediation.adapters.prebid.utils.MaxMediationInterstitialUtils
import com.applovin.mediation.ads.MaxInterstitialAd
import org.silvermob.sdk.api.data.AdUnitFormat
import org.silvermob.sdk.api.mediation.MediationInterstitialAdUnit
import org.silvermob.sdk.renderingtestapp.R
import java.util.*

class MaxInterstitialFragmentMultiformat : MaxInterstitialFragment() {

    override fun initAd(): Any? {
        maxInterstitialAd = MaxInterstitialAd(adUnitId, activity)
        maxInterstitialAd?.setListener(createListener())

        val context = requireContext()
        val mediationUtils =
            MaxMediationInterstitialUtils(
                maxInterstitialAd
            )
        adUnit = MediationInterstitialAdUnit(
            activity,
            listOf(
                context.getString(R.string.imp_prebid_id_interstitial_320_480),
                context.getString(R.string.imp_prebid_id_video_interstitial_320_480)
            ).shuffled().first(),
            EnumSet.of(AdUnitFormat.DISPLAY, AdUnitFormat.VIDEO),
            mediationUtils
        )
        adUnit?.setMinSizePercentage(30, 30)
        return adUnit
    }

}