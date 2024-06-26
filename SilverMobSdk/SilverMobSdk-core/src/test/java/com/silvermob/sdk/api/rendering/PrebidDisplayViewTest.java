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
import android.view.View;

import com.silvermob.sdk.api.data.AdFormat;
import com.silvermob.sdk.api.exceptions.AdException;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.rendering.bidding.data.bid.Bid;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.listeners.DisplayVideoListener;
import com.silvermob.sdk.rendering.bidding.listeners.DisplayViewListener;
import com.silvermob.sdk.rendering.models.AdDetails;
import com.silvermob.sdk.rendering.views.AdViewManager;
import com.silvermob.sdk.rendering.views.AdViewManagerListener;
import com.silvermob.sdk.test.utils.WhiteBox;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class PrebidDisplayViewTest {

    private PrebidDisplayView prebidDisplayView;
    private Context context;
    private AdUnitConfiguration adUnitConfiguration;
    @Mock private BidResponse bidResponse;
    @Mock private DisplayViewListener mockDisplayViewListener;
    @Mock private DisplayVideoListener mockDisplayVideoListener;
    @Mock private AdViewManager mockAdViewManager;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(PrebidDisplayViewTest.this);

        context = Robolectric.buildActivity(Activity.class).create().get();

        adUnitConfiguration = new AdUnitConfiguration();
        adUnitConfiguration.setAdFormat(AdFormat.BANNER);

        BidResponse mockResponse = mock(BidResponse.class);
        Bid mockBid = mock(Bid.class);
        when(mockBid.getAdm()).thenReturn("adm");
        when(mockResponse.getWinningBid()).thenReturn(mockBid);

        prebidDisplayView = new PrebidDisplayView(context, mockDisplayViewListener, mockDisplayVideoListener, adUnitConfiguration, mockResponse);
        reset(mockDisplayViewListener);
    }

    @Test
    public void whenDisplayAd_LoadBidTransaction() {
        Assert.assertNotNull(WhiteBox.getInternalState(prebidDisplayView, "adViewManager"));
    }

    @Test
    public void whenAdViewManagerListenerAdLoaded_NotifyListenerOnAdLoaded()
        throws IllegalAccessException {
        AdViewManagerListener adViewManagerListener = getAdViewManagerListener();
        adViewManagerListener.adLoaded(mock(AdDetails.class));
        verify(mockDisplayViewListener).onAdLoaded();
    }

    @Test
    public void whenAdViewManagerListenerViewReadyForImmediateDisplay_NotifyListenerOnAdDisplayed()
        throws IllegalAccessException {
        AdViewManagerListener adViewManagerListener = getAdViewManagerListener();
        adViewManagerListener.viewReadyForImmediateDisplay(mock(View.class));
        verify(mockDisplayViewListener).onAdDisplayed();
    }

    @Test
    public void whenAdViewManagerListenerFailedToLoad_NotifyListenerOnAdFailed()
        throws IllegalAccessException {
        AdViewManagerListener adViewManagerListener = getAdViewManagerListener();
        adViewManagerListener.failedToLoad(new AdException(AdException.INTERNAL_ERROR, "Test"));
        verify(mockDisplayViewListener).onAdFailed(any(AdException.class));
    }

    @Test
    public void whenAdViewManagerListenerCreativeWasClicked_NotifyListenerOnAdClicked()
        throws IllegalAccessException {
        AdViewManagerListener adViewManagerListener = getAdViewManagerListener();
        adViewManagerListener.creativeClicked("");
        verify(mockDisplayViewListener).onAdClicked();
    }

    @Test
    public void whenAdViewManagerListenerCreativeInterstitialDidClose_NotifyListenerOnAdClosed()
        throws IllegalAccessException {
        AdViewManagerListener adViewManagerListener = getAdViewManagerListener();
        adViewManagerListener.creativeInterstitialClosed();
        verify(mockDisplayViewListener).onAdClosed();
    }

    @Test
    public void whenAdViewManagerListenerCreativeDidCollapse_NotifyListenerOnAdClosed()
        throws IllegalAccessException {
        AdViewManagerListener adViewManagerListener = getAdViewManagerListener();
        adViewManagerListener.creativeCollapsed();
        verify(mockDisplayViewListener).onAdClosed();
    }

    private AdViewManagerListener getAdViewManagerListener() throws IllegalAccessException {
        return (AdViewManagerListener) WhiteBox.field(PrebidDisplayView.class, "adViewManagerListener").get(prebidDisplayView);
    }

}