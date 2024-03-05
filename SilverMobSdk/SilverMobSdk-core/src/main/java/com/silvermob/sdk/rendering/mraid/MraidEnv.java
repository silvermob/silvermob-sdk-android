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

package com.silvermob.sdk.rendering.mraid;

import com.silvermob.sdk.SilverMob;
import com.silvermob.sdk.rendering.utils.helpers.AdIdManager;
import com.silvermob.sdk.rendering.utils.helpers.AppInfoManager;

import androidx.annotation.NonNull;

public class MraidEnv {

    private MraidEnv() {

    }

    @NonNull
    public static String getWindowMraidEnv() {
        return "window.MRAID_ENV = {"
                + getStringPropertyWithSeparator("version", SilverMob.MRAID_VERSION)
                + getStringPropertyWithSeparator("sdk", SilverMob.SDK_NAME)
                + getStringPropertyWithSeparator("sdkVersion", SilverMob.SDK_VERSION)
                + getStringPropertyWithSeparator("appId", AppInfoManager.getPackageName())
                + getStringPropertyWithSeparator("ifa", AdIdManager.getAdId())
                + getBooleanPropertyWithSeparator("limitAdTracking", AdIdManager.isLimitAdTrackingEnabled(), ",")
                + getBooleanPropertyWithSeparator("coppa", SilverMob.isCoppaEnabled, "")
                + "};";
    }

    static String getStringPropertyWithSeparator(String propertyName, String propertyValue) {
        String separator = ",";
        return String.format("%s: \"%s\"%s", propertyName, propertyValue, separator);
    }

    static String getBooleanPropertyWithSeparator(String propertyName, boolean propertyValue, String separator) {
        return String.format("%s: %s%s", propertyName, propertyValue, separator);
    }
}
