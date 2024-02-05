package com.silvermob.sdk.renderingtestapp.plugplay.bidding.ppm

import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.api.rendering.InterstitialAdUnit
import java.util.*

class PpmInterstitialSoundButtonFragment : PpmInterstitialFragment() {

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
        interstitialAdUnit?.apply {
            setInterstitialAdUnitListener(this@PpmInterstitialSoundButtonFragment)
            setIsMuted(false)
            setIsSoundButtonVisible(true)
        }

    }

}