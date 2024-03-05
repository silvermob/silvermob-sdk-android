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

package com.silvermob.sdk.rendering.bidding.interfaces;

import com.silvermob.sdk.AdSize;
import com.silvermob.sdk.rendering.bidding.data.bid.Bid;
import com.silvermob.sdk.rendering.bidding.listeners.BannerEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface BannerEventHandler {
    AdSize[] getAdSizeArray();

    void setBannerEventListener(@NonNull
                                        BannerEventListener bannerViewListener);

    void requestAdWithBid(@Nullable Bid bid);

    void trackImpression();

    void destroy();
}
