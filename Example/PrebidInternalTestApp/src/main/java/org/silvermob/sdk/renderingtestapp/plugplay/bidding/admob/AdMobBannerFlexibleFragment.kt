package org.silvermob.sdk.renderingtestapp.plugplay.bidding.admob

import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import org.silvermob.sdk.AdSize
import org.silvermob.sdk.admob.AdMobMediationBannerUtils
import org.silvermob.sdk.admob.PrebidBannerAdapter
import org.silvermob.sdk.api.mediation.MediationBannerAdUnit
import com.google.android.gms.ads.AdSize as GamAdSize


class AdMobBannerFlexibleFragment : AdMobBannerFragment() {

    companion object {
        private const val TAG = "FlexibleAdMobBanner"
    }

    override fun initAd(): Any? {
        MobileAds.initialize(requireContext()) {
            Log.d("MobileAds", "Initialization complete.")
        }

        bannerView = AdView(requireActivity())
        bannerView?.setAdSize(GamAdSize.getLandscapeInlineAdaptiveBannerAdSize(requireContext(), GamAdSize.FULL_WIDTH))
        bannerView?.adUnitId = adUnitId
        bannerView?.adListener = getListener()
        binding.viewContainer.addView(bannerView)

        adRequestExtras = Bundle()
        adRequest = AdRequest
            .Builder()
            .addNetworkExtrasBundle(PrebidBannerAdapter::class.java, adRequestExtras!!)
            .build()
        val mediationUtils =
            AdMobMediationBannerUtils(adRequestExtras, bannerView)


        adUnit = MediationBannerAdUnit(
            requireContext(),
            configId,
            AdSize(width, height),
            mediationUtils
        )
        adUnit?.addAdditionalSizes(AdSize(728, 90))
        adUnit?.setRefreshInterval(refreshDelay)
        return adUnit
    }

}