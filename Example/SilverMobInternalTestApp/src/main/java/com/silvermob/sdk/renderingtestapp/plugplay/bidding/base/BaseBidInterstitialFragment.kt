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

package com.silvermob.sdk.renderingtestapp.plugplay.bidding.base

import android.os.Bundle
import android.util.Log
import android.view.View
import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.api.exceptions.AdException
import com.silvermob.sdk.api.rendering.InterstitialAdUnit
import com.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingInterstitialBinding
import com.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.rendering.GamInterstitialFragment
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents

abstract class BaseBidInterstitialFragment : AdFragment(),
        com.silvermob.sdk.api.rendering.listeners.InterstitialAdUnitListener {
    private val TAG = GamInterstitialFragment::class.java.simpleName

    override val layoutRes = R.layout.fragment_bidding_interstitial
    protected var interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit? = null

    protected val binding: FragmentBiddingInterstitialBinding
        get() = getBinding()
    protected lateinit var events: Events

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)

        events = Events(view)
        binding.btnLoad.setOnClickListener {
            handleLoadInterstitialClick()
        }
    }

    abstract fun initInterstitialAd(adUnitFormat: com.silvermob.sdk.api.data.AdUnitFormat,
                                    adUnitId: String?, configId: String?,
                                    width: Int, height: Int)

    override fun initAd(): Any? {
        initInterstitialAd(getAdUnitIdentifierTypeBasedOnTitle(getTitle()), adUnitId, configId, width, height)
        return interstitialAdUnit
    }

    override fun loadAd() {
        interstitialAdUnit?.loadAd()
    }

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode? {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.INTERSTITIAL
    }

    override fun onDestroyView() {
        super.onDestroyView()
        interstitialAdUnit?.destroy()
    }

    override fun onAdFailed(interstitial: com.silvermob.sdk.api.rendering.InterstitialAdUnit?, exception: com.silvermob.sdk.api.exceptions.AdException?) {
        Log.d(TAG, "onAdFailed() called with: interstitial = [$interstitial], exception = [$exception]")
        events.failed(true)
        binding.btnLoad.isEnabled = true
    }

    override fun onAdDisplayed(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
        Log.d(TAG, "onAdDisplayed() called with: interstitialAdUnit = [$interstitialAdUnit]")
        events.displayed(true)
    }

    override fun onAdClosed(interstitial: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
        Log.d(TAG, "onAdClosed() called with: interstitial = [$interstitial]")
        events.closed(true)
    }

    override fun onAdClicked(interstitial: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
        Log.d(TAG, "onAdClicked() called with: interstitial = [$interstitial]")
        events.clicked(true)
    }

    override fun onAdLoaded(interstitialAdUnit: com.silvermob.sdk.api.rendering.InterstitialAdUnit?) {
        Log.d(TAG, "onAdLoaded() called with: interstitialAdUnit = [$interstitialAdUnit]")
        events.loaded(true)
        binding.btnLoad.setText(R.string.text_show)
        binding.btnLoad.isEnabled = true
    }

    private fun handleLoadInterstitialClick() {
        when (binding.btnLoad.text) {
            getString(R.string.text_load) -> {
                binding.btnLoad.isEnabled = false
                resetEventButtons()
                loadAd()
            }

            getString(R.string.text_show) -> {
                binding.btnLoad.text = getString(R.string.text_load)
                interstitialAdUnit?.show()
            }
        }
    }

    private fun getAdUnitIdentifierTypeBasedOnTitle(title: String): com.silvermob.sdk.api.data.AdUnitFormat {
        return if (title.contains("Video Interstitial", ignoreCase = true) && !title.contains(
                "MRAID 2.0",
                ignoreCase = true
            )
        ) {
            com.silvermob.sdk.api.data.AdUnitFormat.VIDEO
        } else {
            com.silvermob.sdk.api.data.AdUnitFormat.DISPLAY
        }
    }

    protected class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun closed(b: Boolean) = enable(R.id.btnAdClosed, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)
        fun displayed(b: Boolean) = enable(R.id.btnAdDisplayed, b)

    }

}