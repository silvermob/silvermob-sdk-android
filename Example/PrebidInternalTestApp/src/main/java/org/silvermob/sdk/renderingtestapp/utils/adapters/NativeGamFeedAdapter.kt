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

package org.silvermob.sdk.renderingtestapp.utils.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.nativead.NativeCustomFormatAd
import org.silvermob.sdk.NativeData
import org.silvermob.sdk.PrebidNativeAd
import org.silvermob.sdk.PrebidNativeAdEventListener
import org.silvermob.sdk.api.data.FetchDemandResult
import org.silvermob.sdk.api.mediation.MediationNativeAdUnit
import org.silvermob.sdk.api.mediation.listeners.OnFetchCompleteListener
import org.silvermob.sdk.eventhandlers.utils.GamUtils
import org.silvermob.sdk.rendering.utils.ntv.NativeAdProvider
import org.silvermob.sdk.renderingtestapp.R
import org.silvermob.sdk.renderingtestapp.utils.loadImage

class NativeGamFeedAdapter(
    context: Context,
    private val nativeAdUnit: MediationNativeAdUnit,
    private val gamAdLoader: AdLoader,
    private val extras: Bundle
) : BaseFeedAdapter(context) {

    private val TAG = NativeFeedAdapter::class.java.simpleName

    private var nativeAdLayout: ConstraintLayout? = null

    private val fetchCompleteListener =
        OnFetchCompleteListener {
            val builder = AdManagerAdRequest.Builder()
            val publisherAdRequest = builder.build()

            nativeAdUnit.fetchDemand { result ->
                if (result != FetchDemandResult.SUCCESS) {
                    gamAdLoader.loadAd(publisherAdRequest)
                    return@fetchDemand
                }

                GamUtils.prepare(publisherAdRequest, extras)
                gamAdLoader.loadAd(publisherAdRequest)
            }
        }

    override fun destroy() {
        nativeAdUnit.destroy()
    }

    override fun initAndLoadAdView(parent: ViewGroup?, container: FrameLayout): View? {
        if (nativeAdLayout == null) {
            nativeAdLayout = layoutInflater.inflate(R.layout.lyt_native_ad, parent, false) as ConstraintLayout
        }
        nativeAdUnit.fetchDemand(fetchCompleteListener)
        return nativeAdLayout
    }

    fun handleCustomFormatAd(customFormatAd: NativeCustomFormatAd?) {
        customFormatAd ?: return

        if (GamUtils.didPrebidWin(customFormatAd)) {
            val prebidNativeAd = NativeAdProvider.getNativeAd(extras)
            if (prebidNativeAd != null) {
                inflateViewContent(prebidNativeAd)
            }
        } else {
            Log.d(TAG, "handleCustomFormatAd: prebid lost")
        }
    }

    private fun inflateViewContent(nativeAd: PrebidNativeAd) {
        nativeAd.registerView(
            nativeAdLayout,
            listOf(nativeAdLayout?.findViewById(R.id.btnNativeAction)),
            SafeNativeListener()
        )

        nativeAdLayout?.apply {
            this.findViewById<TextView>(R.id.tvNativeTitle)?.text = nativeAd.title
            this.findViewById<TextView>(R.id.tvNativeBody)?.text = nativeAd.description
            this.findViewById<TextView>(R.id.tvNativeBrand)?.text =
                nativeAd.dataList.find { it.type == NativeData.Type.SPONSORED_BY }?.value
            this.findViewById<Button>(R.id.btnNativeAction)?.text = nativeAd.callToAction

            loadImage(this.findViewById(R.id.ivNativeMain), nativeAd.imageUrl)
            loadImage(this.findViewById(R.id.ivNativeIcon), nativeAd.iconUrl)

        }
    }

    private class SafeNativeListener : PrebidNativeAdEventListener {
            override fun onAdClicked() {}
            override fun onAdImpression() {}
            override fun onAdExpired() {}
    }

}