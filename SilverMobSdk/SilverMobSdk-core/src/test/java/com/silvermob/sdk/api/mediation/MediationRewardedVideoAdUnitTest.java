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

package com.silvermob.sdk.api.mediation;

import android.app.Activity;
import android.content.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.silvermob.sdk.SilverMob;
import com.silvermob.sdk.api.data.AdFormat;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.rendering.bidding.config.MockMediationUtils;
import com.silvermob.sdk.rendering.bidding.loader.BidLoader;
import com.silvermob.sdk.test.utils.WhiteBox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class MediationRewardedVideoAdUnitTest {

    private Context context;
    private MediationRewardedVideoAdUnit mopubRewardedAdUnit;

    @Before
    public void setUp() throws Exception {
        context = Robolectric.buildActivity(Activity.class).create().get();
        SilverMob.setServerAccountId("id");
        mopubRewardedAdUnit = new MediationRewardedVideoAdUnit(context, "config", new MockMediationUtils());
        WhiteBox.setInternalState(mopubRewardedAdUnit, "bidLoader", mock(BidLoader.class));
    }

    @After
    public void cleanup() {
        SilverMob.setServerAccountId(null);
    }

    @Test
    public void whenInitAdConfig_PrepareAdConfigForInterstitial() {
        mopubRewardedAdUnit.initAdConfig("config", null);
        AdUnitConfiguration adConfiguration = mopubRewardedAdUnit.adUnitConfig;
        assertEquals("config", adConfiguration.getConfigId());
        assertEquals(EnumSet.of(AdFormat.VAST), adConfiguration.getAdFormats());
        assertTrue(adConfiguration.isRewarded());
    }

}