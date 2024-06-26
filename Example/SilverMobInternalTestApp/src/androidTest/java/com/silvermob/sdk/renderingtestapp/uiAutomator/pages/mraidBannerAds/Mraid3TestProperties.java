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

package com.silvermob.sdk.renderingtestapp.uiAutomator.pages.mraidBannerAds;

import android.view.KeyEvent;

import com.silvermob.sdk.SilverMob;
import com.silvermob.sdk.rendering.utils.helpers.AdIdManager;
import com.silvermob.sdk.rendering.utils.helpers.AppInfoManager;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class Mraid3TestProperties extends MraidBasicPage {

    public Mraid3TestProperties(UiDevice device) {
        super(device);
    }

    public Mraid3TestProperties bannerShouldLoad() {
        assertNotNull("Banner is not displayed",
                      device.wait(Until.findObject(Locators.bannerCreative), TIMEOUT * 2));
        return this;
    }

    public Mraid3TestProperties clickBanner() {
        clickOnView(Locators.bannerCreative, TIMEOUT);
        return this;
    }

    public Mraid3TestProperties checkMraidEnv() throws JSONException {
        JSONObject envJson = new JSONObject();
        envJson.put("version", SilverMob.MRAID_VERSION);
        envJson.put("sdk", SilverMob.SDK_NAME);
        envJson.put("sdkVersion", SilverMob.SDK_VERSION);
        envJson.put("appId", AppInfoManager.getPackageName());
        envJson.put("ifa", AdIdManager.getAdId());
        envJson.put("limitAdTracking", AdIdManager.isLimitAdTrackingEnabled());
        envJson.put("coppa", SilverMob.isCoppaEnabled);

        String viewText = clearStringFromWhitespace(device.wait(Until.findObject(Locators.envContainer), TIMEOUT).getText());
        assertEquals(envJson.toString(), viewText);
        return this;
    }

    public Mraid3TestProperties checkGetLocationFinishedWithoutError() {
        String viewText = clearStringFromWhitespace(device.wait(Until.findObject(Locators.locationContainer), TIMEOUT).getText());
        assertFalse(viewText.isEmpty());
        return this;
    }

    public Mraid3TestProperties checkGetCurrentAppOrientation() throws JSONException {
        JSONObject appOrientationJson = new JSONObject();
        appOrientationJson.put("orientation", getDeviceOrientation());
        appOrientationJson.put("locked", false); // activity is not locked.

        String viewText = clearStringFromWhitespace(device.wait(Until.findObject(Locators.orientationContainer), TIMEOUT).getText());

        assertEquals(appOrientationJson.toString(), viewText);
        return this;
    }

    private String clearStringFromWhitespace(String text) {
        return text.replace("\n", "")
                   .replace(" ", "");
    }

    public Mraid3TestProperties adShouldBeExpanded() {
        assertNotNull("Ad is not expanded",
                      device.wait(Until.findObject(Mraid3TestProperties.Locators.expandedAdName), TIMEOUT));
        return this;
    }

    public Mraid3TestProperties pressPlayButton() {
        clickOnView(Mraid3TestProperties.Locators.playButton, TIMEOUT);
        return this;
    }

    public Mraid3TestProperties closeExpandedAd() {
        clickOnView(Mraid3TestProperties.Locators.closeButton, TIMEOUT);
        return this;
    }

    public Mraid3TestProperties turnVolumeDown() {
        int counter = 0;
        while (getVolumeLevel() != 0) {
            volumeDown();
            counter++;
            if (counter > 10) break;
        }
        return this;
    }

    public Mraid3TestProperties pressVolumeUpAndCheckVolume(int times) {
        for (int i = 0; i < times; i++) {
            volumeUp();
        }

        double volumeChangeStep = 100.0 / 15.0;
        assertEquals("Expected volume level doesn't match actual.", volumeChangeStep * (double) times, getVolumeLevel(), 0);

        return this;
    }

    private void volumeUp() {
        device.pressKeyCode(KeyEvent.KEYCODE_VOLUME_UP);
    }

    private void volumeDown() {
        device.pressKeyCode(KeyEvent.KEYCODE_VOLUME_DOWN);
    }

    private Double getVolumeLevel() {
        String volumeString = device.wait(Until.findObject(Mraid3TestProperties.Locators.volumeIndicator), TIMEOUT)
                                    .getText()
                                    .replace("%", "");
        return Double.valueOf(volumeString);
    }

    private static class Locators {
        static BySelector bannerCreative = getWebViewSelectorVersionDepends("Expand!");
        static BySelector envContainer = By.res("mraidEnvNode");
        static BySelector locationContainer = By.res("locationNode");
        static BySelector orientationContainer = By.res("currentAppOrientationNode");
        static BySelector expandedAdName = By.res("posdiv");
        static BySelector closeButton = By.res("org.prebid.mobile.renderingtestapp:id/iv_close_interstitial");
        static BySelector volumeIndicator = By.res("indicator");
        static BySelector playButton = getWebViewSelectorVersionDepends("play");
    }
}
