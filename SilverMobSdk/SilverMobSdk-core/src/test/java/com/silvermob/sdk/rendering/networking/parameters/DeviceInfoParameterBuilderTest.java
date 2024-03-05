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

package com.silvermob.sdk.rendering.networking.parameters;

import android.app.Activity;

import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.reflection.sdk.ManagersResolverReflection;
import com.silvermob.sdk.rendering.models.openrtb.BidRequest;
import com.silvermob.sdk.rendering.models.openrtb.bidRequests.Device;
import com.silvermob.sdk.rendering.sdk.ManagersResolver;
import com.silvermob.sdk.rendering.utils.helpers.AdIdManager;
import com.silvermob.sdk.rendering.utils.helpers.AppInfoManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static com.silvermob.sdk.test.utils.ResourceUtils.assertJsonEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19, qualifiers = "w1920dp-h1080dp")
public class DeviceInfoParameterBuilderTest {

    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;

    @Before
    public void setUp() throws Exception {
        ManagersResolver resolver = ManagersResolver.getInstance();
        ManagersResolverReflection.resetManagers(resolver);
        resolver.prepare(Robolectric.buildActivity(Activity.class).create().get());
    }

    @Test
    public void testAppendBuilderParameters() throws Exception {
        BidRequest expectedBidRequest = new BidRequest();
        final Device expectedBidRequestDevice = expectedBidRequest.getDevice();
        final String ipAddress = "192.168.0.1";
        final String carrier = "carrier";

        AdUnitConfiguration adConfiguration = new AdUnitConfiguration();

        ParameterBuilder builder = new DeviceInfoParameterBuilder(adConfiguration);
        AdRequestInput adRequestInput = new AdRequestInput();
        builder.appendBuilderParameters(adRequestInput);

        expectedBidRequestDevice.os = DeviceInfoParameterBuilder.PLATFORM_VALUE;
        expectedBidRequestDevice.w = SCREEN_WIDTH;
        expectedBidRequestDevice.h = SCREEN_HEIGHT;
        expectedBidRequestDevice.language = Locale.getDefault().getLanguage();
        expectedBidRequestDevice.osv = "4.4";
        expectedBidRequestDevice.os = "Android";
        expectedBidRequestDevice.devicetype = Device.DeviceType.TABLET.value;
        expectedBidRequestDevice.model = "robolectric";
        expectedBidRequestDevice.make = "unknown";
        expectedBidRequestDevice.pxratio = 1f;
        expectedBidRequestDevice.ua = AppInfoManager.getUserAgent();
        expectedBidRequestDevice.ifa = AdIdManager.getAdId();
        expectedBidRequestDevice.lmt = AdIdManager.isLimitAdTrackingEnabled() ? 1 : 0;

        assertJsonEquals(expectedBidRequest.getJsonObject(),
                     adRequestInput.getBidRequest().getJsonObject());
    }
}