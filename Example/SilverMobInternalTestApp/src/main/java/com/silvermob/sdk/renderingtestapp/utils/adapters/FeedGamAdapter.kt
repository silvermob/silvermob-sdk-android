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

package com.silvermob.sdk.renderingtestapp.utils.adapters

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.silvermob.sdk.renderingtestapp.utils.OpenRtbConfigs

class FeedGamAdapter(context: Context,
                     width: Int,
                     height: Int,
                     configId: String,
                     val adUnitId: String) : FeedAdapter(context, width, height, configId) {

    override fun initAndLoadAdView(parent: ViewGroup?, container: FrameLayout): View? {
        val eventHandler = com.silvermob.sdk.eventhandlers.GamBannerEventHandler(container.context, adUnitId,
                *getGamAdSizeArray(com.silvermob.sdk.AdSize(width, height)))

        if (videoView == null) {
            videoView = com.silvermob.sdk.api.rendering.BannerView(
                    container.context,
                    configId,
                    eventHandler
            )
            videoView?.videoPlacementType = com.silvermob.sdk.api.data.VideoPlacementType.IN_FEED
            val layoutParams =
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.gravity = Gravity.CENTER
            videoView?.layoutParams = layoutParams

            OpenRtbConfigs.setImpExtDataTo(videoView)
        }
        videoView?.loadAd()
        return videoView
    }

    private fun getGamAdSizeArray(initialSize: com.silvermob.sdk.AdSize) = arrayOf(initialSize)

}