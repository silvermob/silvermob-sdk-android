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

import com.silvermob.sdk.api.rendering.pluginrenderer.PrebidMobilePluginRenderer;
import com.silvermob.sdk.configuration.PBSConfig;
import com.silvermob.sdk.reflection.Reflection;
import com.silvermob.sdk.reflection.sdk.PrebidMobileReflection;
import com.silvermob.sdk.testutils.BaseSetup;
import com.silvermob.sdk.testutils.FakePrebidMobilePluginRenderer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;

import static com.silvermob.sdk.api.rendering.pluginrenderer.PrebidMobilePluginRegister.PREBID_MOBILE_RENDERER_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = BaseSetup.testSDK)
public class SilverMobTest extends BaseSetup {

    @Before
    public void clean() {
        Reflection.setStaticVariableTo(SilverMob.class, "customStatusEndpoint", null);
    }


    @Test
    public void testPrebidMobileSettings() {
        SilverMob.setServerAccountId("123456");
        assertEquals("123456", SilverMob.getPrebidServerAccountId());
        SilverMob.setTimeoutMillis(2500);
        assertEquals(2500, SilverMob.getTimeoutMillis());
        SilverMob.initializeSdk(activity.getApplicationContext(), null);
        assertNotNull(SilverMob.getApplicationContext());
        SilverMob.setShareGeoLocation(true);
        assertTrue(SilverMob.isShareGeoLocation());
        //PrebidMobile.setPrebidServerHost(Host.RUBICON);
        //assertEquals(Host.RUBICON, PrebidMobile.getPrebidServerHost());
        SilverMob.setStoredAuctionResponse("111122223333");
        assertEquals("111122223333", SilverMob.getStoredAuctionResponse());
        SilverMob.addStoredBidResponse("appnexus", "221144");
        SilverMob.addStoredBidResponse("rubicon", "221155");
        assertFalse(SilverMob.getStoredBidResponses().isEmpty());
        SilverMob.clearStoredBidResponses();
        assertTrue(SilverMob.getStoredBidResponses().isEmpty());
        SilverMob.setPbsDebug(true);
        assertTrue(SilverMob.getPbsDebug());
        SilverMob.setCreativeFactoryTimeout(7000);
        assertEquals(7000, SilverMob.getCreativeFactoryTimeout());
        SilverMob.setCreativeFactoryTimeoutPreRenderContent(25000);
        assertEquals(25000, SilverMob.getCreativeFactoryTimeoutPreRenderContent());
    }

    @Test
    public void testSetCustomHeaders() {
        HashMap<String, String> customHeaders = new HashMap<>();
        customHeaders.put("key1", "value1");
        customHeaders.put("key2", "value2");
        SilverMob.setCustomHeaders(customHeaders);

        assertFalse(SilverMob.getCustomHeaders().isEmpty());
        assertEquals(2, SilverMob.getCustomHeaders().size());
    }

    @Test
    public void setCustomStatusEndpoint_nullValue() {
        SilverMob.setCustomStatusEndpoint(null);

        assertNull(getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_ipAddress() {
        SilverMob.setCustomStatusEndpoint("192.168.0.106");

        assertEquals("https://192.168.0.106/", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_valueWithoutHttp() {
        SilverMob.setCustomStatusEndpoint("site.com");

        assertEquals("https://site.com/", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_valueWithoutHttpWithThreeW() {
        SilverMob.setCustomStatusEndpoint("www.site.com");

        assertEquals("https://www.site.com/", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_valueWithoutHttpWithThreeWAndPath() {
        SilverMob.setCustomStatusEndpoint("www.site.com/status");

        assertEquals("https://www.site.com/status", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_goodValue() {
        SilverMob.setCustomStatusEndpoint("http://site.com/status");

        assertEquals("http://site.com/status", getInnerCustomEndpointValue());
    }

    @Test
    public void setCustomStatusEndpoint_goodValueSecure() {
        SilverMob.setCustomStatusEndpoint("https://site.com/status?test=1");

        assertEquals("https://site.com/status?test=1", getInnerCustomEndpointValue());
    }

    private String getInnerCustomEndpointValue() {
        return PrebidMobileReflection.getCustomStatusEndpoint();
    }

    @Test
    public void registerPluginRenderer_registerProperly() {
        // Given
        PrebidMobilePluginRenderer fakePrebidMobilePluginRenderer = FakePrebidMobilePluginRenderer.getFakePrebidRenderer(
                null,
                null,
                true,
                PREBID_MOBILE_RENDERER_NAME,
                "1.0"
        );

        // When
        SilverMob.registerPluginRenderer(fakePrebidMobilePluginRenderer);

        // Then
        assertTrue(SilverMob.containsPluginRenderer(fakePrebidMobilePluginRenderer));
    }

    @Test
    public void registerPluginRenderer_unregisterProperly() {
        // Given
        PrebidMobilePluginRenderer fakePrebidMobilePluginRenderer = FakePrebidMobilePluginRenderer.getFakePrebidRenderer(
                null,
                null,
                true,
                PREBID_MOBILE_RENDERER_NAME,
                "1.0"
        );

        // When
        SilverMob.registerPluginRenderer(fakePrebidMobilePluginRenderer);

        // Then
        assertTrue(SilverMob.containsPluginRenderer(fakePrebidMobilePluginRenderer));

        // When
        SilverMob.unregisterPluginRenderer(fakePrebidMobilePluginRenderer);

        // Then
        assertFalse(SilverMob.containsPluginRenderer(fakePrebidMobilePluginRenderer));
    }

    @Test
    public void getCreativeFactoryTimeouts_usePbsConfig() {
        SilverMob.setPbsConfig(new PBSConfig(9000, 20000));
        SilverMob.setCreativeFactoryTimeout(8000);
        SilverMob.setCreativeFactoryTimeoutPreRenderContent(21000);
        assertEquals(9000, SilverMob.getCreativeFactoryTimeout());
        assertEquals(20000, SilverMob.getCreativeFactoryTimeoutPreRenderContent());
    }

    @Test
    public void getCreativeFactoryTimeouts_useSdk() {
        SilverMob.setCreativeFactoryTimeout(8000);
        SilverMob.setCreativeFactoryTimeoutPreRenderContent(21000);
        assertEquals(8000, SilverMob.getCreativeFactoryTimeout());
        assertEquals(21000, SilverMob.getCreativeFactoryTimeoutPreRenderContent());
    }

}
