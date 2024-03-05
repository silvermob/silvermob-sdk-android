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

package com.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.original

import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.silvermob.sdk.NativeEventTracker
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingMultiformatBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents
import java.lang.ref.WeakReference

open class GamOriginalMultiformatBannerVideoNativeStylesFragment : AdFragment() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-multiformat-native-styles"
        const val CONFIG_ID_BANNER = "prebid-demo-banner-320-50"
        const val CONFIG_ID_NATIVE = "prebid-demo-banner-native-styles"
        const val CONFIG_ID_VIDEO = "prebid-demo-video-outstream-original-api"
    }

    override val layoutRes = R.layout.fragment_bidding_multiformat

    private lateinit var events: Events
    private var prebidAdUnit: com.silvermob.sdk.api.original.PrebidAdUnit? = null
    private val params = arrayOf(Params.BANNER, Params.VIDEO, Params.NATIVE)
    private val binding: FragmentBiddingMultiformatBinding get() = getBinding()


    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER
    }

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)
        events = Events(view)
        initButtons()
    }

    override fun initAd() = Unit

    override fun loadAd() {}

    private fun load() {
        val configIds = mutableListOf<String>()
        val prebidRequest = com.silvermob.sdk.api.original.PrebidRequest()

        if (Params.BANNER.activated) {
            prebidRequest.setBannerParameters(createBannerParameters())
            configIds.add(CONFIG_ID_BANNER)
        }

        if (Params.VIDEO.activated) {
            prebidRequest.setVideoParameters(createVideoParameters())
            configIds.add(CONFIG_ID_VIDEO)
        }

        if (Params.NATIVE.activated) {
            prebidRequest.setNativeParameters(createNativeParameters())
            configIds.add(CONFIG_ID_NATIVE)
        }

        val configId = configIds.random()
        prebidAdUnit = com.silvermob.sdk.api.original.PrebidAdUnit(configId)

        val gamRequest = AdManagerAdRequest.Builder().build()
        prebidAdUnit?.fetchDemand(gamRequest, prebidRequest) {
            loadGam(gamRequest)
        }
    }

    private fun loadGam(gamRequest: AdManagerAdRequest) {
        val gamView = AdManagerAdView(requireContext())
        gamView.adUnitId = AD_UNIT_ID
        gamView.setAdSizes(AdSize.FLUID, AdSize.BANNER, AdSize.MEDIUM_RECTANGLE)
        gamView.loadAd(gamRequest)
        gamView.adListener = AdListenerWithToast(events, gamView) {
            binding.btnLoad.isEnabled = true
        }
        binding.viewContainer.addView(gamView)

    }


    private fun createBannerParameters(): com.silvermob.sdk.BannerParameters {
        return com.silvermob.sdk.BannerParameters()
    }

    private fun createVideoParameters(): com.silvermob.sdk.VideoParameters {
        return com.silvermob.sdk.VideoParameters(listOf("video/mp4"))
    }

    private fun createNativeParameters(): com.silvermob.sdk.NativeParameters {
        val assets = mutableListOf<com.silvermob.sdk.NativeAsset>()

        val title = com.silvermob.sdk.NativeTitleAsset()
        title.setLength(90)
        title.isRequired = true
        assets.add(title)

        val icon = com.silvermob.sdk.NativeImageAsset(20, 20, 20, 20)
        icon.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.ICON
        icon.isRequired = true
        assets.add(icon)

        val image = com.silvermob.sdk.NativeImageAsset(200, 200, 200, 200)
        image.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.MAIN
        image.isRequired = true
        assets.add(image)

        val data = com.silvermob.sdk.NativeDataAsset()
        data.len = 90
        data.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.SPONSORED
        data.isRequired = true
        assets.add(data)

        val body = com.silvermob.sdk.NativeDataAsset()
        body.isRequired = true
        body.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.DESC
        assets.add(body)

        val cta = com.silvermob.sdk.NativeDataAsset()
        cta.isRequired = true
        cta.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.CTATEXT
        assets.add(cta)

        val nativeParameters = com.silvermob.sdk.NativeParameters(assets)
        nativeParameters.addEventTracker(
                com.silvermob.sdk.NativeEventTracker(
                        com.silvermob.sdk.NativeEventTracker.EVENT_TYPE.IMPRESSION,
                        arrayListOf(NativeEventTracker.EVENT_TRACKING_METHOD.IMAGE)
                )
        )
        nativeParameters.setContextType(com.silvermob.sdk.NativeAdUnit.CONTEXT_TYPE.SOCIAL_CENTRIC)
        nativeParameters.setPlacementType(com.silvermob.sdk.NativeAdUnit.PLACEMENTTYPE.CONTENT_FEED)
        nativeParameters.setContextSubType(com.silvermob.sdk.NativeAdUnit.CONTEXTSUBTYPE.GENERAL_SOCIAL)

        return nativeParameters
    }

    private fun initButtons() {
        binding.btnLoad.isEnabled = true
        binding.btnLoad.setOnClickListener {
            binding.viewContainer.removeAllViews()
            binding.btnLoad.isEnabled = false
            resetEventButtons()
            load()
        }

        listOf(
            binding.btnBannerParams,
            binding.btnVideoParams,
            binding.btnNativeParams
        ).forEachIndexed { i, toggleButton ->
            toggleButton.isChecked = true
            toggleButton.setOnClickListener {
                val newValue = toggleButton.isChecked
                toggleButton.isChecked = newValue
                params[i].activated = newValue

                val notEnoughActivations = params.count { it.activated } < 2
                if (notEnoughActivations) {
                    binding.btnLoad.isEnabled = false
                } else {
                    binding.btnLoad.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        prebidAdUnit?.destroy()
    }

    private class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun impression(b: Boolean) = enable(R.id.btnAdImpression, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)

    }

    private enum class Params(var activated: Boolean = true) {

        BANNER, VIDEO, NATIVE

    }

    private class AdListenerWithToast(
        private val events: Events,
        adView: AdManagerAdView,
        private val onAdLoadingFinished: () -> Unit,
    ) : AdListener() {

        private val weakAdView = WeakReference(adView)

        override fun onAdLoaded() {
            super.onAdLoaded()
            events.loaded(true)
            onAdLoadingFinished()
            com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(weakAdView.get(), object : com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener {
                override fun success(width: Int, height: Int) {
                    weakAdView.get()?.setAdSizes(AdSize(width, height))
                }

                override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
            })
        }

        override fun onAdImpression() {
            super.onAdImpression()
            events.impression(true)
        }

        override fun onAdClicked() {
            super.onAdClicked()
            events.clicked(true)
        }

        override fun onAdFailedToLoad(adError: LoadAdError) {
            super.onAdFailedToLoad(adError);
            events.failed(true)
            onAdLoadingFinished()
        }

    }

}