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

package com.silvermob.sdk.rendering.loading;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.silvermob.sdk.SilverMob;
import com.silvermob.sdk.api.data.AdFormat;
import com.silvermob.sdk.api.exceptions.AdException;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.rendering.models.AbstractCreative;
import com.silvermob.sdk.rendering.models.CreativeModel;
import com.silvermob.sdk.rendering.models.HTMLCreative;
import com.silvermob.sdk.rendering.session.manager.OmAdSessionManager;
import com.silvermob.sdk.rendering.video.VideoAdEvent;
import com.silvermob.sdk.rendering.video.VideoCreative;
import com.silvermob.sdk.rendering.video.VideoCreativeModel;
import com.silvermob.sdk.rendering.views.interstitial.InterstitialManager;
import com.silvermob.sdk.test.utils.WhiteBox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.silvermob.sdk.rendering.models.CreativeModelsMakerVast.HTML_CREATIVE_TAG;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class CreativeFactoryTest {

    private Context mockContext;
    private CreativeModel mockModel;
    private CreativeFactory.Listener mockListener;
    private OmAdSessionManager mockOmAdSessionManager;
    private InterstitialManager mockInterstitialManager;

    @Before
    public void setUp() throws Exception {
        mockContext = Robolectric.buildActivity(Activity.class).create().get();
        mockModel = mock(CreativeModel.class);
        mockListener = mock(CreativeFactory.Listener.class);
        mockOmAdSessionManager = mock(OmAdSessionManager.class);
        mockInterstitialManager = mock(InterstitialManager.class);
    }

    @Test
    public void testCreativeFactory() throws Exception {
        boolean hasException;

        // Valid
        new CreativeFactory(mockContext, mockModel, mockListener, mockOmAdSessionManager, mockInterstitialManager);

        // Null context
        hasException = false;
        try {
            new CreativeFactory(null, mockModel, mockListener, mockOmAdSessionManager, mockInterstitialManager);
        }
        catch (AdException e) {
            hasException = true;
        }
        assertTrue(hasException);

        // Null creative model
        hasException = false;
        try {
            new CreativeFactory(mockContext, null, mockListener, mockOmAdSessionManager, mockInterstitialManager);
        }
        catch (AdException e) {
            hasException = true;
        }
        assertTrue(hasException);

        // Null listener
        hasException = false;
        try {
            new CreativeFactory(mockContext, mockModel, null, mockOmAdSessionManager, mockInterstitialManager);
        }
        catch (AdException e) {
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    public void testAttemptAuidCreative() throws Exception {
        AdUnitConfiguration adConfiguration = new AdUnitConfiguration();
        adConfiguration.setAdFormat(AdFormat.BANNER);
        Handler mockHandler = mock(Handler.class);
        when(mockModel.getAdConfiguration()).thenReturn(adConfiguration);
        when(mockModel.getName()).thenReturn(HTML_CREATIVE_TAG);
        when(mockModel.getImpressionUrl()).thenReturn("impressionUrl");
        when(mockModel.getClickUrl()).thenReturn("clickUrl");
        SilverMob.setCreativeFactoryTimeout(7000);
        //Run the creativeFactory
        CreativeFactory creativeFactory = new CreativeFactory(mockContext,
                mockModel,
                mockListener,
                mockOmAdSessionManager,
                mockInterstitialManager
        );
        WhiteBox.field(CreativeFactory.class, "timeoutHandler").set(creativeFactory, mockHandler);
        creativeFactory.start();

        AbstractCreative creative = creativeFactory.getCreative();
        assertNotNull(creative);
        assertTrue(creative instanceof HTMLCreative);
        verify(mockHandler).postDelayed(any(Runnable.class), eq(7_000L));
    }

    @Test
    public void testAttemptVastCreative() throws Exception {
        VideoCreativeModel mockVideoModel = mock(VideoCreativeModel.class);
        AdUnitConfiguration adConfiguration = new AdUnitConfiguration();
        Handler mockHandler = mock(Handler.class);
        adConfiguration.setAdFormat(AdFormat.VAST);
        SilverMob.setCreativeFactoryTimeoutPreRenderContent(25000);
        HashMap<VideoAdEvent.Event, ArrayList<String>> videoEventsUrls = new HashMap<>();
        videoEventsUrls.put(VideoAdEvent.Event.AD_EXPAND, new ArrayList<>(Arrays.asList("AD_EXPAND")));
        when(mockVideoModel.getVideoEventUrls()).thenReturn(videoEventsUrls);
        when(mockVideoModel.getAdConfiguration()).thenReturn(adConfiguration);
        CreativeFactory creativeFactory;

        // Blank media URL
        when(mockVideoModel.getMediaUrl()).thenReturn("");
        creativeFactory = new CreativeFactory(mockContext,
                mockVideoModel,
                mockListener,
                mockOmAdSessionManager,
                mockInterstitialManager
        );
        creativeFactory.start();
        assertNull(WhiteBox.getInternalState(creativeFactory, "creative"));

        // Valid
        when(mockVideoModel.getMediaUrl()).thenReturn("mediaUrl");
        creativeFactory = new CreativeFactory(mockContext,
                mockVideoModel,
                mockListener,
                mockOmAdSessionManager,
                mockInterstitialManager
        );
        WhiteBox.field(CreativeFactory.class, "timeoutHandler").set(creativeFactory, mockHandler);
        creativeFactory.start();

        AbstractCreative creative = creativeFactory.getCreative();
        assertNotNull(creative);
        assertTrue(creative instanceof VideoCreative);
        verify(mockHandler).postDelayed(any(Runnable.class), eq(25_000L));
    }

    @Test
    public void testCreativeFactoryCreativeResolutionListener() throws Exception {
        CreativeFactory mockCreativeFactory = mock(CreativeFactory.class);
        CreativeFactory.Listener mockCreativeFactoryListener = mock(CreativeFactory.Listener.class);
        CreativeFactory.CreativeFactoryCreativeResolutionListener creativeResolutionListener = new CreativeFactory.CreativeFactoryCreativeResolutionListener(
                mockCreativeFactory);

        WhiteBox.field(CreativeFactory.class, "listener").set(mockCreativeFactory, mockCreativeFactoryListener);
        WhiteBox.field(CreativeFactory.class, "timeoutHandler").set(mockCreativeFactory, mock(Handler.class));

        // Success
        creativeResolutionListener.creativeReady(mock(AbstractCreative.class));
        verify(mockCreativeFactoryListener).onSuccess();

        // Failure
        AdException adException = new AdException(AdException.INTERNAL_ERROR, "msg");
        creativeResolutionListener.creativeFailed(adException);

        verify(mockCreativeFactoryListener).onFailure(adException);
    }

    @Test
    public void creativeReadyWithExpiredTimeoutStatus_FactoryListenerNotCalled()
    throws IllegalAccessException {
        CreativeFactory mockCreativeFactory = mock(CreativeFactory.class);
        CreativeFactory.Listener mockCreativeFactoryListener = mock(CreativeFactory.Listener.class);
        CreativeFactory.TimeoutState expired = CreativeFactory.TimeoutState.EXPIRED;
        CreativeFactory.CreativeFactoryCreativeResolutionListener creativeResolutionListener = new CreativeFactory.CreativeFactoryCreativeResolutionListener(
                mockCreativeFactory);

        WhiteBox.field(CreativeFactory.class, "listener").set(mockCreativeFactory, mockCreativeFactoryListener);
        WhiteBox.field(CreativeFactory.class, "timeoutState").set(mockCreativeFactory, expired);

        creativeResolutionListener.creativeReady(mock(AbstractCreative.class));

        verify(mockCreativeFactoryListener, never()).onSuccess();
    }

    @Test
    public void destroyCalled_removeCallbacksCalled()
    throws IllegalAccessException, AdException {
        CreativeFactory creativeFactory = new CreativeFactory(mockContext,
                mockModel,
                mockListener,
                mockOmAdSessionManager,
                mockInterstitialManager
        );
        Handler mockHandler = mock(Handler.class);

        WhiteBox.field(CreativeFactory.class, "timeoutHandler").set(creativeFactory, mockHandler);

        creativeFactory.destroy();

        verify(mockHandler).removeCallbacks(null);
    }
}
