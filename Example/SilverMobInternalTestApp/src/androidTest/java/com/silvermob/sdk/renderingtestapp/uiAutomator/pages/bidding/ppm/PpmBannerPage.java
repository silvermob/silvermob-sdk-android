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

package com.silvermob.sdk.renderingtestapp.uiAutomator.pages.bidding.ppm;

import com.silvermob.sdk.renderingtestapp.uiAutomator.pages.AdBasePage;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import static org.junit.Assert.assertNotNull;

public class PpmBannerPage extends AdBasePage<PpmBannerPage> {
    private static final int TIMEOUT = 15000;

    private static class Locators {
        static BySelector bannerCreative = By.res(TAG, "viewContainer").hasChild(
            By.clazz("android.widget.FrameLayout").hasChild(
                By.clazz("android.widget.FrameLayout")));
    }

    public PpmBannerPage(UiDevice device) {
        super(device);
    }

    public PpmBannerPage bannerShouldLoad() {
        assertNotNull("Banner is not displayed",
                      device.wait(Until.findObject(Locators.bannerCreative), TIMEOUT));
        return this;
    }

    public PpmBannerPage clickBanner() {
        clickOnView(Locators.bannerCreative, TIMEOUT);
        return this;
    }
}
