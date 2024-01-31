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

package com.silvermob.sdk.renderingtestapp.plugplay.bidding.ppm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.silvermob.sdk.PrebidNativeAd
import com.silvermob.sdk.PrebidNativeAdEventListener
import com.silvermob.sdk.api.data.FetchDemandResult
import com.silvermob.sdk.api.mediation.MediationNativeAdUnit
import com.silvermob.sdk.rendering.utils.ntv.NativeAdProvider
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentNativeBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents
import com.silvermob.sdk.renderingtestapp.utils.loadImage
import java.lang.ref.WeakReference

open class PpmNativeFragment : AdFragment() {

    private val TAG = PpmNativeFragment::class.java.simpleName

    override val layoutRes = R.layout.fragment_native
    protected var nativeAdUnit: com.silvermob.sdk.api.mediation.MediationNativeAdUnit? = null
    protected var extras = Bundle()

    protected val binding: FragmentNativeBinding
        get() = getBinding()
    protected lateinit var events: Events

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        events = Events(view)

        if (layoutRes == R.layout.fragment_native) {
            layoutInflater.inflate(
                getEventButtonViewId(),
                binding.contentFragmentNative,
                true
            )
        }
    }

    override fun initAd(): Any? {
        configureOriginalPrebid()

        nativeAdUnit = com.silvermob.sdk.api.mediation.MediationNativeAdUnit(configId, extras)
        configureNativeAdUnit(nativeAdUnit!!)
        return nativeAdUnit
    }

    override fun loadAd() {
        nativeAdUnit?.fetchDemand {
            if (it != com.silvermob.sdk.api.data.FetchDemandResult.SUCCESS) {
                events.fetchDemandFailure(true)
                return@fetchDemand
            }
            events.fetchDemandSuccess(true)

            val nativeAd = com.silvermob.sdk.rendering.utils.ntv.NativeAdProvider.getNativeAd(extras)
            if (nativeAd == null) {
                events.getNativeAdResultFailure(true)
                return@fetchDemand
            }

            events.getNativeAdResultSuccess(true)
            inflateViewContent(nativeAd)
        }
    }

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode? = null

    override fun onDestroyView() {
        super.onDestroyView()
        nativeAdUnit?.destroy()
    }

    protected open fun getEventButtonViewId(): Int = R.layout.lyt_native_in_app_events

    protected open fun inflateViewContent(nativeAd: com.silvermob.sdk.PrebidNativeAd) {
        nativeAd.registerView(
            binding.adContainer,
            listOf(
                binding.tvNativeTitle,
                binding.tvNativeBody,
                binding.tvNativeBrand,
                binding.btnNativeAction,
                binding.ivNativeMain,
                binding.ivNativeIcon
            ),
            SafeNativeListener(events)
        )

        binding.tvNativeTitle.text = nativeAd.title
        binding.tvNativeBody.text = nativeAd.description
        binding.tvNativeBrand.text = nativeAd.sponsoredBy
        binding.btnNativeAction.text = nativeAd.callToAction

        if (nativeAd.imageUrl.isNotBlank()) {
            loadImage(binding.ivNativeMain, nativeAd.imageUrl)
        }
        if (nativeAd.iconUrl.isNotBlank()) {
            loadImage(binding.ivNativeIcon, nativeAd.iconUrl)
        }
    }

    protected class SafeNativeListener(events: Events) : com.silvermob.sdk.PrebidNativeAdEventListener {

        private val eventsReference = WeakReference(events)

        override fun onAdClicked() {
            eventsReference.get()?.clicked(true)
        }

        override fun onAdImpression() {
            Handler(Looper.getMainLooper()).post {
                eventsReference.get()?.impression(true)
            }
        }

        override fun onAdExpired() {
            eventsReference.get()?.expired(true)
        }

    }

    protected class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun impression(b: Boolean) = enable(R.id.btnAdImpression, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)

        fun displayed(b: Boolean) = enable(R.id.btnAdDismissed, b)

        fun nativeAdLoaded(b: Boolean) = enable(R.id.btnNativeAdLoaded, b)
        fun expired(b: Boolean) = enable(R.id.btnAdExpired, b)
        fun fetchDemandFailure(b: Boolean) = enable(R.id.btnFetchDemandResultFailure, b)
        fun fetchDemandSuccess(b: Boolean) = enable(R.id.btnFetchDemandResultSuccess, b)
        fun getNativeAdResultFailure(b: Boolean) = enable(R.id.btnGetNativeAdResultFailure, b)
        fun getNativeAdResultSuccess(b: Boolean) = enable(R.id.btnGetNativeAdResultSuccess, b)
        fun customAdRequestSuccess(b: Boolean) = enable(R.id.btnCustomAdRequestSuccess, b)
        fun unifiedRequestSuccess(b: Boolean) = enable(R.id.btnUnifiedRequestSuccess, b)
        fun primaryAdWinUnified(b: Boolean) = enable(R.id.btnPrimaryAdWinUnified, b)
        fun primaryAdWinCustom(b: Boolean) = enable(R.id.btnPrimaryAdWinCustom, b)
        fun primaryAdRequestFailure(b: Boolean) = enable(R.id.btnPrimaryAdRequestFailure, b)

    }

}