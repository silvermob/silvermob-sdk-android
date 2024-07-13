/*
 *    Copyright 2018-2019 Prebid.org, Inc.
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
package com.silvermob.sdk.prebidkotlindemo.activities.ads.applovin

import android.os.Bundle
import android.widget.FrameLayout
import com.applovin.mediation.ads.MaxAdView
import com.applovin.sdk.AppLovinSdkUtils
import com.silvermob.sdk.prebidkotlindemo.activities.BaseAdActivity

class AppLovinMaxDisplayBanner728x90Activity : BaseAdActivity() {

    companion object {
        const val AD_UNIT_ID = "b55547d91e0ba5a4"
    }

    private var adView: MaxAdView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createAd()
    }

    private fun createAd() {
        //AppLovinSdk.getInstance( this ).showMediationDebugger()
        adView = MaxAdView(AD_UNIT_ID, this)

        adView?.layoutParams = FrameLayout.LayoutParams(
            AppLovinSdkUtils.dpToPx(this, 728),
            AppLovinSdkUtils.dpToPx(this, 90)
        )

        adWrapperView.addView(adView)

        adView?.loadAd()
    }


    override fun onDestroy() {
        super.onDestroy()
        adView?.destroy()
    }

}
