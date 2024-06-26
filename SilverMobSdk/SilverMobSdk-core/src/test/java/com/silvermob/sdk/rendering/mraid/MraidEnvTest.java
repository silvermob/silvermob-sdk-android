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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class MraidEnvTest {

    @Test
    public void getWindowMraidEnv_ReturnProperlyFormedMraid() {
        SilverMob.isCoppaEnabled = true;
        String expectedValue = "window.MRAID_ENV = {"
                + "version: \"" + SilverMob.MRAID_VERSION + "\","
                + "sdk: \"" + SilverMob.SDK_NAME + "\","
                + "sdkVersion: \"" + SilverMob.SDK_VERSION + "\","
                + "appId: \"null\","
                + "ifa: \"null\","
                + "limitAdTracking: false,"
                + "coppa: true"
                + "};";

        assertEquals(expectedValue, MraidEnv.getWindowMraidEnv());
    }
}
