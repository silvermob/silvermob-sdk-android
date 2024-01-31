package org.silvermob.sdk.renderingtestapp.plugplay.bidding.ppm

import org.silvermob.sdk.api.data.AdUnitFormat
import org.silvermob.sdk.api.rendering.InterstitialAdUnit
import org.silvermob.sdk.renderingtestapp.plugplay.bidding.base.BaseBidInterstitialFragment
import org.silvermob.sdk.api.data.Position
import java.util.*

class PpmInterstitialCloseButtonAreaFragment : BaseBidInterstitialFragment() {

    override fun initInterstitialAd(
        adUnitFormat: AdUnitFormat,
        adUnitId: String?,
        configId: String?,
        width: Int,
        height: Int
    ) {
        interstitialAdUnit = if (adUnitFormat == AdUnitFormat.VIDEO) {
            InterstitialAdUnit(
                requireContext(),
                configId,
                EnumSet.of(adUnitFormat)
            )
        } else {
            InterstitialAdUnit(requireContext(), configId)
        }
        interstitialAdUnit?.setInterstitialAdUnitListener(this)
        interstitialAdUnit?.setCloseButtonArea(0.40)
        interstitialAdUnit?.setCloseButtonPosition(Position.TOP_LEFT)
    }

}