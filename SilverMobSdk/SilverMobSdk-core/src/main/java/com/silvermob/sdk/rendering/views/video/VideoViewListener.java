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

package com.silvermob.sdk.rendering.views.video;

import com.silvermob.sdk.api.exceptions.AdException;
import com.silvermob.sdk.api.rendering.VideoView;
import com.silvermob.sdk.rendering.models.AdDetails;

import androidx.annotation.NonNull;

public abstract class VideoViewListener {
    public void onLoaded(
        @NonNull
            VideoView videoAdView, AdDetails adDetails) {}

    public void onLoadFailed(
        @NonNull
            VideoView videoAdView, AdException error) {}

    public void onDisplayed(
        @NonNull
            VideoView videoAdView) {}

    public void onPlayBackCompleted(
        @NonNull
            VideoView videoAdView) {}

    public void onClickThroughOpened(
        @NonNull
            VideoView videoAdView) {}

    public void onClickThroughClosed(
        @NonNull
            VideoView videoAdView) {}

    public void onPlaybackPaused() {}

    public void onPlaybackResumed() {}

    public void onVideoUnMuted() {}

    public void onVideoMuted() {}
}
