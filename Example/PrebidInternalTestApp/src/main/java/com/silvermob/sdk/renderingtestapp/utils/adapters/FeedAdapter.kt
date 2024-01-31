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
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.silvermob.sdk.AdSize
import com.silvermob.sdk.api.data.VideoPlacementType
import com.silvermob.sdk.api.rendering.BannerView

private const val TAG = "FeedAdapter"

open class FeedAdapter(context: Context,
                       val width: Int,
                       val height: Int,
                       val configId: String) : BaseFeedAdapter(context) {

    protected var videoView: com.silvermob.sdk.api.rendering.BannerView? = null

    override fun destroy() {
        Log.d(TAG, "Destroying adapter")
        videoView?.destroy()
    }

    override fun initAndLoadAdView(parent: ViewGroup?, container: FrameLayout): View? {
        if (videoView == null) {
            videoView = com.silvermob.sdk.api.rendering.BannerView(
                    container.context,
                    configId,
                    com.silvermob.sdk.AdSize(width, height)
            )
            videoView?.videoPlacementType = com.silvermob.sdk.api.data.VideoPlacementType.IN_FEED
            val layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            layoutParams.gravity = Gravity.CENTER
            videoView?.layoutParams = layoutParams
        }
        videoView?.loadAd()
        return videoView
    }
}