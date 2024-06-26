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

package com.silvermob.sdk;

import com.silvermob.sdk.api.data.AdFormat;

import java.util.EnumSet;

import androidx.annotation.NonNull;

/**
 * @deprecated - For outstream video ads use {@link BannerAdUnit} with adUnitFormat parameter:
 * {@code EnumSet.of(AdUnitFormat.VIDEO); }
 * <br>
 * - For instream video ads use {@link InStreamVideoAdUnit}.
 */
@Deprecated
public class VideoAdUnit extends VideoBaseAdUnit {

    public VideoAdUnit(@NonNull String configId, int width, int height) {
        super(configId, EnumSet.of(AdFormat.VAST));
        configuration.addSize(new AdSize(width, height));
    }

}
