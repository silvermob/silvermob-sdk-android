package com.silvermob.sdk.api.rendering;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.silvermob.sdk.SilverMob;
import com.silvermob.sdk.api.data.AdFormat;
import com.silvermob.sdk.api.rendering.pluginrenderer.PrebidMobilePluginRenderer;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.rendering.bidding.data.bid.Bid;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.listeners.DisplayVideoListener;
import com.silvermob.sdk.rendering.bidding.listeners.DisplayViewListener;
import com.silvermob.sdk.testutils.FakePrebidMobilePluginRenderer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.silvermob.sdk.api.rendering.pluginrenderer.PrebidMobilePluginRegister.PREBID_MOBILE_RENDERER_NAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class DisplayViewTest {

    private DisplayView displayView;
    private Context context;
    private BidResponse mockResponse;
    private PrebidMobilePluginRenderer fakePrebidMobilePluginRenderer;
    @Spy
    private AdUnitConfiguration adUnitConfiguration;
    @Mock
    private View mockBannerView;
    @Mock
    private DisplayViewListener mockDisplayViewListener;
    @Mock
    private DisplayVideoListener mockDisplayVideoListener;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(DisplayViewTest.this);

        context = Robolectric.buildActivity(Activity.class).create().get();

        adUnitConfiguration.setAdFormat(AdFormat.BANNER);

        fakePrebidMobilePluginRenderer = Mockito.spy(FakePrebidMobilePluginRenderer.getFakePrebidRenderer(null, mockBannerView, true, PREBID_MOBILE_RENDERER_NAME, "1.0"));
        SilverMob.registerPluginRenderer(fakePrebidMobilePluginRenderer);

        mockResponse = mock(BidResponse.class);
        Bid mockBid = mock(Bid.class);
        when(mockBid.getAdm()).thenReturn("adm");
        when(mockResponse.getWinningBid()).thenReturn(mockBid);
        when(mockResponse.getPreferredPluginRendererName()).thenReturn(PREBID_MOBILE_RENDERER_NAME);
    }

    @Test
    public void onDisplayViewWinNotification_returnBannerAdView() {
        displayView = new DisplayView(context, mockDisplayViewListener, adUnitConfiguration, mockResponse);


        verify(adUnitConfiguration).modifyUsingBidResponse(mockResponse);
        verify(fakePrebidMobilePluginRenderer).createBannerAdView(context, mockDisplayViewListener, null, adUnitConfiguration, mockResponse);
        // bannerAdView added to view hierarchy
        assertEquals(1, displayView.getChildCount());
    }

    @Test
    public void onDisplayViewWithVideoListenerWinNotification_returnBannerAdView() {
        displayView = new DisplayView(context, mockDisplayViewListener, mockDisplayVideoListener, adUnitConfiguration, mockResponse);

        verify(adUnitConfiguration).modifyUsingBidResponse(mockResponse);
        verify(fakePrebidMobilePluginRenderer).createBannerAdView(context, mockDisplayViewListener, mockDisplayVideoListener, adUnitConfiguration, mockResponse);
        // bannerAdView added to view hierarchy
        assertEquals(1, displayView.getChildCount());
    }
}