/*
 *    Copyright 2018-2021 Prebid.org, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.rendering

import com.silvermob.sdk.AdSize
import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.api.rendering.InterstitialAdUnit
import com.silvermob.sdk.eventhandlers.GamInterstitialEventHandler
import com.silvermob.sdk.renderingtestapp.plugplay.bidding.base.BaseBidInterstitialFragment
import com.silvermob.sdk.renderingtestapp.utils.CommandLineArgumentParser
import java.util.*

open class GamInterstitialFragment : BaseBidInterstitialFragment() {
    override fun initInterstitialAd(adUnitFormat: com.silvermob.sdk.api.data.AdUnitFormat, adUnitId: String?, configId: String?, width: Int, height: Int) {
        val interstitialEventHandler = com.silvermob.sdk.eventhandlers.GamInterstitialEventHandler(requireActivity(), adUnitId)
        interstitialAdUnit = if (adUnitFormat == com.silvermob.sdk.api.data.AdUnitFormat.VIDEO) {
            com.silvermob.sdk.api.rendering.InterstitialAdUnit(
                    requireContext(),
                    configId,
                    EnumSet.of(adUnitFormat),
                    interstitialEventHandler
            )
        }
        else {
            com.silvermob.sdk.api.rendering.InterstitialAdUnit(
                    requireContext(),
                    configId,
                    interstitialEventHandler
            ).apply {
                setMinSizePercentage(com.silvermob.sdk.AdSize(width, height))
            }
        }

        interstitialAdUnit?.setInterstitialAdUnitListener(this)
        interstitialAdUnit?.let {
            CommandLineArgumentParser.addAdUnitSpecificData(it)
        }
    }
}