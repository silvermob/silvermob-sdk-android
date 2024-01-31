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

import android.annotation.SuppressLint
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.api.rendering.BannerView
import com.silvermob.sdk.rendering.utils.helpers.AppInfoManager
import com.silvermob.sdk.renderingtestapp.utils.CommandLineArgumentParser

class PpmBannerSpecialSymbolsFragment : PpmBannerFragment() {

    private val previousAppName = com.silvermob.sdk.rendering.utils.helpers.AppInfoManager.getAppName()

    @SuppressLint("VisibleForTests")
    override fun initAd(): Any? {
        bannerView = com.silvermob.sdk.api.rendering.BannerView(
                requireContext(),
                configId,
                com.silvermob.sdk.AdSize(width, height)
        )
        bannerView?.setAutoRefreshDelay(refreshDelay)
        bannerView?.setBannerListener(this)
        bannerView?.let { CommandLineArgumentParser.addAdUnitSpecificData(it) }
        binding.viewContainer.addView(bannerView)

        com.silvermob.sdk.rendering.utils.helpers.AppInfoManager.setAppName("天気")

        return bannerView
    }

    @SuppressLint("VisibleForTests")
    override fun onDestroyView() {
        super.onDestroyView()
        com.silvermob.sdk.rendering.utils.helpers.AppInfoManager.setAppName(previousAppName)
    }

}