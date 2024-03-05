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

package com.silvermob.sdk.renderingtestapp.plugplay.bidding.pluginrenderer

import android.os.Bundle
import com.silvermob.sdk.renderingtestapp.plugplay.bidding.base.BaseBidInterstitialFragment
import com.silvermob.sdk.renderingtestapp.utils.CommandLineArgumentParser
import com.silvermob.sdk.renderingtestapp.utils.SampleCustomRenderer
import com.silvermob.sdk.renderingtestapp.utils.SampleCustomRendererEventListener
import java.util.EnumSet

open class PpmInterstitialPluginEventListenerFragment : BaseBidInterstitialFragment(), SampleCustomRendererEventListener {
    private val TAG = PpmInterstitialPluginEventListenerFragment::class.java.simpleName
    private val samplePluginRenderer = SampleCustomRenderer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.silvermob.sdk.SilverMob.registerPluginRenderer(samplePluginRenderer)
    }

    override fun initInterstitialAd(adUnitFormat: com.silvermob.sdk.api.data.AdUnitFormat, adUnitId: String?,
                                    configId: String?, width: Int, height: Int) {
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
        interstitialAdUnit?.setPluginEventListener(this)
        interstitialAdUnit?.setMinSizePercentage(com.silvermob.sdk.AdSize(30, 30))
        interstitialAdUnit?.let {
            CommandLineArgumentParser.addAdUnitSpecificData(it)
        }
    }

    override fun onImpression() {
        com.silvermob.sdk.LogUtil.debug(TAG, "onImpression")
    }

    override fun onDestroy() {
        com.silvermob.sdk.SilverMob.unregisterPluginRenderer(samplePluginRenderer)
        super.onDestroy()
    }
}