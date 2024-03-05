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
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.formats.AdManagerAdViewOptions
import com.google.android.gms.ads.formats.OnAdManagerAdViewLoadedListener
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import com.google.android.gms.ads.nativead.NativeCustomFormatAd.OnCustomFormatAdLoadedListener
import com.google.common.collect.Lists
import com.silvermob.sdk.NativeEventTracker
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingMultiformatBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents
import com.silvermob.sdk.renderingtestapp.utils.loadImage

open class GamOriginalMultiformatBannerVideoNativeFragment : AdFragment() {

    companion object {
        const val AD_UNIT_ID = "/21808260008/prebid-demo-multiformat"
        const val CONFIG_ID_BANNER = "prebid-demo-banner-320-50"
        const val CONFIG_ID_NATIVE = "prebid-demo-banner-native-styles"
        const val CONFIG_ID_VIDEO = "prebid-demo-video-outstream-original-api"
        const val CUSTOM_FORMAT_ID = "12304464"
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
        val onBannerLoaded = OnAdManagerAdViewLoadedListener { adView ->
            showBannerAd(adView)
        }

        val onNativeLoaded = OnNativeAdLoadedListener { nativeAd ->
            showNativeAd(nativeAd, binding.viewContainer)
        }

        val onCustomAdLoaded = OnCustomFormatAdLoadedListener { customNativeAd ->
            showCustomNativeAd(customNativeAd)
        }

        val adLoader = AdLoader.Builder(requireContext(), AD_UNIT_ID)
            .forAdManagerAdView(onBannerLoaded, AdSize.BANNER, AdSize.MEDIUM_RECTANGLE)
            .forNativeAd(onNativeLoaded)
            .forCustomFormatAd(CUSTOM_FORMAT_ID, onCustomAdLoaded, null)
            .withAdListener(AdListenerWithToast(events, onAdLoadingFinished = { binding.btnLoad.isEnabled = true }))
            .withAdManagerAdViewOptions(AdManagerAdViewOptions.Builder().build())
            .build()

        adLoader.loadAd(gamRequest)
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

    private fun showBannerAd(adView: AdManagerAdView) {
        binding.viewContainer.addView(adView)
        com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(adView, object : com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener {
            override fun success(width: Int, height: Int) {
                adView.setAdSizes(AdSize(width, height))
            }

            override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
        })
    }

    private fun showNativeAd(ad: NativeAd, wrapper: ViewGroup) {
        val nativeContainer = View.inflate(wrapper.context, R.layout.layout_native, null)

        val icon = nativeContainer.findViewById<ImageView>(R.id.imgIcon)
        val iconUrl = ad.icon?.uri?.toString()
        if (iconUrl != null) {
            loadImage(icon, iconUrl)
        }

        val title = nativeContainer.findViewById<TextView>(R.id.tvTitle)
        title.text = ad.headline

        val image = nativeContainer.findViewById<ImageView>(R.id.imgImage)
        val imageUrl = ad.images.getOrNull(0)?.uri?.toString()
        if (imageUrl != null) {
            loadImage(image, imageUrl)
        }

        val description = nativeContainer.findViewById<TextView>(R.id.tvDesc)
        description.text = ad.body

        val cta = nativeContainer.findViewById<Button>(R.id.btnCta)
        cta.text = ad.callToAction

        wrapper.addView(nativeContainer)
    }

    private fun showCustomNativeAd(customNativeAd: NativeCustomFormatAd) {
        com.silvermob.sdk.addendum.AdViewUtils.findNative(customNativeAd, object : com.silvermob.sdk.PrebidNativeAdListener {
            override fun onPrebidNativeLoaded(ad: com.silvermob.sdk.PrebidNativeAd) {
                inflatePrebidNativeAd(ad)
            }

            override fun onPrebidNativeNotFound() {
                Log.e("PrebidAdViewUtils", "Find native failed: native not found")
            }

            override fun onPrebidNativeNotValid() {
                Log.e("PrebidAdViewUtils", "Find native failed: native not valid")
            }
        })
    }

    private fun inflatePrebidNativeAd(ad: com.silvermob.sdk.PrebidNativeAd) {
        val nativeContainer = View.inflate(requireContext(), R.layout.layout_native, null)

        val icon = nativeContainer.findViewById<ImageView>(R.id.imgIcon)
        loadImage(icon, ad.iconUrl)

        val title = nativeContainer.findViewById<TextView>(R.id.tvTitle)
        title.text = ad.title

        val image = nativeContainer.findViewById<ImageView>(R.id.imgImage)
        loadImage(image, ad.imageUrl)

        val description = nativeContainer.findViewById<TextView>(R.id.tvDesc)
        description.text = ad.description

        val cta = nativeContainer.findViewById<Button>(R.id.btnCta)
        cta.text = ad.callToAction

        ad.registerView(nativeContainer, Lists.newArrayList(icon, title, image, description, cta), null)

        binding.viewContainer.addView(nativeContainer)
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
        private val onAdLoadingFinished: () -> Unit
    ) : AdListener() {

        override fun onAdLoaded() {
            super.onAdLoaded()
            events.loaded(true)
            onAdLoadingFinished()
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