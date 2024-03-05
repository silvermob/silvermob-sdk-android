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
import com.silvermob.sdk.renderingtestapp.plugplay.bidding.base.BaseFeedFragment
import com.silvermob.sdk.renderingtestapp.utils.adapters.BaseFeedAdapter
import com.silvermob.sdk.renderingtestapp.utils.adapters.NativeFeedAdapter

class PpmNativeFeedFragment : BaseFeedFragment() {

    private var extras = Bundle()

    override fun initAd() {
        super.initAd()
        configureOriginalPrebid()
    }

    override fun initFeedAdapter(): BaseFeedAdapter {
        val nativeAdUnit = com.silvermob.sdk.api.mediation.MediationNativeAdUnit(configId, extras)
        configureNativeAdUnit(nativeAdUnit)
        return NativeFeedAdapter(requireContext(), nativeAdUnit, extras)
    }

}