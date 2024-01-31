package com.silvermob.sdk.api.rendering;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.silvermob.sdk.api.rendering.pluginrenderer.PrebidMobilePluginRegister.PREBID_MOBILE_RENDERER_NAME;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.silvermob.sdk.core.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.silvermob.sdk.api.data.AdFormat;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.core.BuildConfig;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.interfaces.InterstitialControllerListener;
import com.silvermob.sdk.rendering.bidding.listeners.DisplayVideoListener;
import com.silvermob.sdk.rendering.bidding.listeners.DisplayViewListener;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class PrebidRendererTest {

    @Mock
    private AdUnitConfiguration mockAdUnitConfiguration;
    @Mock
    private BidResponse mockBidResponse;

    private PrebidRenderer prebidRenderer;
    private Context context;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(PrebidRendererTest.this);
        context = Robolectric.buildActivity(Activity.class).create().get();
        prebidRenderer = new PrebidRenderer();
    }

    @Test
    public void getName_returnStaticName() {
        assertEquals(PREBID_MOBILE_RENDERER_NAME, prebidRenderer.getName());
    }

    @Test
    public void getVersion_returnCurrentBuildConfigVersion() {
        assertEquals(BuildConfig.VERSION, prebidRenderer.getVersion());
    }

    @Test
    public void getData_returnNull() {
        assertNull(prebidRenderer.getData());
    }

    @Test
    public void createBannerAdView_returnPrebidDisplayViewInstance() {
        DisplayViewListener mockDisplayViewListener = mock(DisplayViewListener.class);
        DisplayVideoListener mockDisplayVideoListener = mock(DisplayVideoListener.class);
        View result = prebidRenderer.createBannerAdView(context, mockDisplayViewListener, mockDisplayVideoListener, mockAdUnitConfiguration, mockBidResponse);
        assertTrue(result instanceof PrebidDisplayView);
    }

    @Test
    public void createInterstitialController_isNotNull() {
        InterstitialControllerListener mockInterstitialControllerListener = mock(InterstitialControllerListener.class);
        PrebidMobileInterstitialControllerInterface interstitialController = prebidRenderer.createInterstitialController(context, mockInterstitialControllerListener, mockAdUnitConfiguration, mockBidResponse);
        assertNotNull(interstitialController);
    }

    @Test
    public void isSupportRenderingFor_complyWithAllAdFormats() {
        for (AdFormat adFormat : AdFormat.values()) {
            when(mockAdUnitConfiguration.isAdType(adFormat)).thenReturn(true);
            assertTrue(prebidRenderer.isSupportRenderingFor(mockAdUnitConfiguration));
        }
    }
}