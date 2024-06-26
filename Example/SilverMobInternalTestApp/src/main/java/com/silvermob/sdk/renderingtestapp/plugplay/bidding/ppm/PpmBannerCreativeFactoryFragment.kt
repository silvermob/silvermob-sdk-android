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
import com.silvermob.sdk.renderingtestapp.R

class PpmBannerCreativeFactoryFragment : PpmBannerFragment() {

    private val DEFAULT_CREATIVE_FACTORY_TIMEOUT = 6 * 1000
    private val DEFAULT_CREATIVE_FACTORY_TIMEOUT_PRERENDER_CONTENT = 30 * 1000

    override fun onAdLoaded(bannerView: com.silvermob.sdk.api.rendering.BannerView?) {
        if (com.silvermob.sdk.SilverMob.getCreativeFactoryTimeout() == DEFAULT_CREATIVE_FACTORY_TIMEOUT ||
            com.silvermob.sdk.SilverMob.getCreativeFactoryTimeoutPreRenderContent() == DEFAULT_CREATIVE_FACTORY_TIMEOUT_PRERENDER_CONTENT) {
            events.failed(true)
        } else {
            super.onAdLoaded(bannerView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        com.silvermob.sdk.SilverMob.setServerAccountId(getString(R.string.prebid_account_id_prod))
    }
}