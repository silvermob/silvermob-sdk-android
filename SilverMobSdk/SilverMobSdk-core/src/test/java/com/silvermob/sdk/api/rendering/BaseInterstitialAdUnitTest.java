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

package com.silvermob.sdk.api.rendering;

import android.app.Activity;
import android.content.Context;

import com.silvermob.sdk.api.exceptions.AdException;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.rendering.bidding.data.bid.Bid;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.listeners.BidRequesterListener;
import com.silvermob.sdk.rendering.models.AdPosition;
import com.silvermob.sdk.test.utils.WhiteBox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class BaseInterstitialAdUnitTest {

    private BaseInterstitialAdUnit baseInterstitialAdUnit;

    @Before
    public void setUp() throws Exception {
        Context context = Robolectric.buildActivity(Activity.class).create().get();
        baseInterstitialAdUnit = new BaseInterstitialAdUnit(context) {
            @Override
            void requestAdWithBid(
                @Nullable
                    Bid bid
            ) {

            }

            @Override
            void showGamAd() {

            }

            @Override
            void notifyAdEventListener(AdListenerEvent adListenerEvent) {

            }

            @Override
            void notifyErrorListener(AdException exception) {

            }
        };
        final AdUnitConfiguration adUnitConfiguration = new AdUnitConfiguration();
        baseInterstitialAdUnit.init(adUnitConfiguration);
        assertEquals(AdPosition.FULLSCREEN.getValue(), adUnitConfiguration.getAdPositionValue());
    }

    @Test
    public void addUpdateRemoveClearContextData_EqualsGetContextDataDictionary() {
        Map<String, Set<String>> expectedMap = new HashMap<>();
        HashSet<String> value1 = new HashSet<>();
        value1.add("value1");
        HashSet<String> value2 = new HashSet<>();
        value2.add("value2");
        expectedMap.put("key1", value1);
        expectedMap.put("key2", value2);

        // add
        baseInterstitialAdUnit.addContextData("key1", "value1");
        baseInterstitialAdUnit.addContextData("key2", "value2");

        assertEquals(expectedMap, baseInterstitialAdUnit.getContextDataDictionary());

        // update
        HashSet<String> updateSet = new HashSet<>();
        updateSet.add("value3");
        baseInterstitialAdUnit.updateContextData("key1", updateSet);
        expectedMap.replace("key1", updateSet);

        assertEquals(expectedMap, baseInterstitialAdUnit.getContextDataDictionary());

        // remove
        baseInterstitialAdUnit.removeContextData("key1");
        expectedMap.remove("key1");
        assertEquals(expectedMap, baseInterstitialAdUnit.getContextDataDictionary());

        // clear
        baseInterstitialAdUnit.clearContextData();
        assertTrue(baseInterstitialAdUnit.getContextDataDictionary().isEmpty());
    }

    @Test
    public void addRemoveContextKeywords_EqualsGetContextKeyWordsSet() {
        HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("key1");
        expectedSet.add("key2");

        // add
        baseInterstitialAdUnit.addContextKeyword("key1");
        baseInterstitialAdUnit.addContextKeyword("key2");

        assertEquals(expectedSet, baseInterstitialAdUnit.getContextKeywordsSet());

        // remove
        baseInterstitialAdUnit.removeContextKeyword("key2");
        expectedSet.remove("key2");
        assertEquals(expectedSet, baseInterstitialAdUnit.getContextKeywordsSet());

        // clear
        baseInterstitialAdUnit.clearContextKeywords();
        assertTrue(baseInterstitialAdUnit.getContextKeywordsSet().isEmpty());

        // add all
        baseInterstitialAdUnit.addContextKeywords(expectedSet);
        assertEquals(expectedSet, baseInterstitialAdUnit.getContextKeywordsSet());
    }

    @Test
    public void setPbAdSlot_EqualsGetPbAdSlot() {
        final String expected = "12345";
        baseInterstitialAdUnit.setPbAdSlot(expected);
        assertEquals(expected, baseInterstitialAdUnit.getPbAdSlot());
    }

    @Test
    public void loadAd_BidResponseIsInitialized() {
        BidResponse bidResponse = baseInterstitialAdUnit.getBidResponse();
        assertNull(bidResponse);

        final BidResponse mockBidResponse = mock(BidResponse.class);
        final Bid mockBid = mock(Bid.class);
        when(mockBidResponse.getWinningBid()).thenReturn(mockBid);
        BidRequesterListener listener = getBidRequesterListener();
        listener.onFetchCompleted(mockBidResponse);

        baseInterstitialAdUnit.loadAd();

        BidResponse actualBidResponse = baseInterstitialAdUnit.getBidResponse();
        assertEquals(mockBidResponse, actualBidResponse);
    }

    @Test
    public void loadAdWithError_BidResponseIsNull() {
        BidResponse bidResponse = baseInterstitialAdUnit.getBidResponse();
        assertNull(bidResponse);

        final AdException adException = mock(AdException.class);
        BidRequesterListener listener = getBidRequesterListener();
        listener.onError(adException);
        baseInterstitialAdUnit.loadAd();

        bidResponse = baseInterstitialAdUnit.getBidResponse();
        assertNull(bidResponse);
    }

    private BidRequesterListener getBidRequesterListener() {
        try {
            return (BidRequesterListener) WhiteBox.field(BaseInterstitialAdUnit.class, "bidRequesterListener")
                .get(baseInterstitialAdUnit);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}