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
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingInterstitialBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents

abstract class BaseBidRewardedFragment : AdFragment() {

    private val TAG = BaseBidRewardedFragment::class.java.simpleName

    override val layoutRes = R.layout.fragment_bidding_interstitial
    protected var rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit? = null

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

    abstract fun initRewardedAd(adUnitId: String?, configId: String?)

    override fun initAd(): Any? {
        initRewardedAd(adUnitId, configId)
        return rewardedAdUnit
    }

    override fun loadAd() {
        rewardedAdUnit?.loadAd()
    }

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode? {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.INTERSTITIAL
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rewardedAdUnit?.destroy()
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
                rewardedAdUnit?.show()
            }
        }
    }

    protected fun createRewardedAdUnitListener() = object :
            com.silvermob.sdk.api.rendering.listeners.RewardedAdUnitListener {

        override fun onAdLoaded(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {
            Log.d(TAG, "onAdLoaded() called with: reward = [${rewardedAdUnit?.userReward}]")
            events.loaded(true)
            binding.btnLoad.setText(R.string.text_show)
            binding.btnLoad.isEnabled = true
        }

        override fun onAdDisplayed(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {
            Log.d(TAG, "onAdDisplayed() called with: rewardedAdUnit = [$rewardedAdUnit]")
            events.displayed(true)
        }

        override fun onAdFailed(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?, exception: com.silvermob.sdk.api.exceptions.AdException?) {
            Log.d(TAG, "onAdFailed() called with: rewardedAdUnit = [$rewardedAdUnit], exception = [$exception]")
            events.failed(true)
            binding.btnLoad.isEnabled = true
        }

        override fun onAdClicked(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {
            Log.d(TAG, "onAdClicked() called with: rewardedAdUnit = [$rewardedAdUnit]")
            events.clicked(true)
        }

        override fun onAdClosed(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {
            Log.d(TAG, "onAdClosed() called with: rewardedAdUnit = [$rewardedAdUnit]")
            events.closed(true)
        }

        override fun onUserEarnedReward(rewardedAdUnit: com.silvermob.sdk.api.rendering.RewardedAdUnit?) {
            Log.d(TAG, "onUserEarnedReward() called with: reward = [${rewardedAdUnit?.userReward}]")
        }

    }

    protected class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun impression(b: Boolean) = enable(R.id.btnAdImpression, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun closed(b: Boolean) = enable(R.id.btnAdClosed, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)

        fun displayed(b: Boolean) = enable(R.id.btnAdDisplayed, b)

    }

}