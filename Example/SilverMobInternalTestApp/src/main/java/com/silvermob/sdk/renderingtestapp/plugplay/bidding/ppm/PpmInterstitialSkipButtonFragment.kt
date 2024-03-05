package com.silvermob.sdk.renderingtestapp.plugplay.bidding.ppm

import com.silvermob.sdk.renderingtestapp.plugplay.bidding.base.BaseBidInterstitialFragment
import java.util.EnumSet

class PpmInterstitialSkipButtonFragment : BaseBidInterstitialFragment() {

    override fun initInterstitialAd(
            adUnitFormat: com.silvermob.sdk.api.data.AdUnitFormat,
            adUnitId: String?,
            configId: String?,
            width: Int,
            height: Int
    ) {
        interstitialAdUnit = if (adUnitFormat == com.silvermob.sdk.api.data.AdUnitFormat.VIDEO) {
            com.silvermob.sdk.api.rendering.InterstitialAdUnit(
                    requireContext(),
                    configId,
                    EnumSet.of(adUnitFormat)
            )
        } else {
            com.silvermob.sdk.api.rendering.InterstitialAdUnit(requireContext(), configId)
        }
        interstitialAdUnit?.setInterstitialAdUnitListener(this)
        interstitialAdUnit?.setSkipDelay(5)
        interstitialAdUnit?.setSkipButtonArea(0.30)
        interstitialAdUnit?.setSkipButtonPosition(com.silvermob.sdk.api.data.Position.TOP_LEFT)
    }

}