package com.silvermob.sdk.renderingtestapp.plugplay.bidding.admob

import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.admob.AdMobMediationBannerUtils
import com.silvermob.sdk.admob.PrebidBannerAdapter
import com.silvermob.sdk.api.mediation.MediationBannerAdUnit
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
            .addNetworkExtrasBundle(com.silvermob.sdk.admob.PrebidBannerAdapter::class.java, adRequestExtras!!)
            .build()
        val mediationUtils =
                com.silvermob.sdk.admob.AdMobMediationBannerUtils(adRequestExtras, bannerView)


        adUnit = com.silvermob.sdk.api.mediation.MediationBannerAdUnit(
                requireContext(),
                configId,
                com.silvermob.sdk.AdSize(width, height),
                mediationUtils
        )
        adUnit?.addAdditionalSizes(com.silvermob.sdk.AdSize(728, 90))
        adUnit?.setRefreshInterval(refreshDelay)
        return adUnit
    }

}