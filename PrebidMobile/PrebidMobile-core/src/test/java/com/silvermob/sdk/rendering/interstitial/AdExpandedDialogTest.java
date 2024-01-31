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

package com.silvermob.sdk.rendering.interstitial;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.silvermob.sdk.rendering.views.interstitial.InterstitialManager;
import com.silvermob.sdk.rendering.views.webview.PrebidWebViewBase;
import com.silvermob.sdk.rendering.views.webview.WebViewBase;
import com.silvermob.sdk.rendering.views.webview.mraid.BaseJSInterface;
import com.silvermob.sdk.rendering.views.webview.mraid.JSInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.silvermob.sdk.rendering.models.HTMLCreative;
import com.silvermob.sdk.rendering.views.interstitial.InterstitialManager;
import com.silvermob.sdk.rendering.views.webview.PrebidWebViewBase;
import com.silvermob.sdk.rendering.views.webview.WebViewBase;
import com.silvermob.sdk.rendering.views.webview.mraid.BaseJSInterface;
import com.silvermob.sdk.rendering.views.webview.mraid.JSInterface;
import com.silvermob.sdk.test.utils.WhiteBox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class AdExpandedDialogTest {

    private AdExpandedDialog adExpandedDialog;

    private Context mockContext;
    private WebViewBase mockWebViewBase;
    private BaseJSInterface mockBaseJSInterface;
    private PrebidWebViewBase mockPrebidWebView;
    private InterstitialManager mockInterstitialManager;

    @Before
    public void setUp() throws Exception {
        mockContext = Robolectric.buildActivity(Activity.class).create().get();
        mockWebViewBase = mock(WebViewBase.class);
        mockBaseJSInterface = mock(BaseJSInterface.class);
        mockInterstitialManager = mock(InterstitialManager.class);

        mockPrebidWebView = mock(PrebidWebViewBase.class);

        when(mockWebViewBase.getMRAIDInterface()).thenReturn(mockBaseJSInterface);
        when(mockWebViewBase.getPreloadedListener()).thenReturn(mockPrebidWebView);

        adExpandedDialog = new AdExpandedDialog(mockContext, mockWebViewBase, mockInterstitialManager);
    }

    @Test
    public void handleCloseClick() throws IllegalAccessException {
        InterstitialManager interstitialManager = mock(InterstitialManager.class);
        Field interstitialManagerField = WhiteBox.field(AdInterstitialDialog.class, "interstitialManager");
        interstitialManagerField.set(adExpandedDialog, interstitialManager);

        adExpandedDialog.handleCloseClick();
        verify(interstitialManager).interstitialClosed(eq(mockWebViewBase));
    }

    @Test
    public void cancelTest() {
        when(mockPrebidWebView.getCreative()).thenReturn(mock(HTMLCreative.class));
        when(mockWebViewBase.getJSName()).thenReturn("");

        adExpandedDialog.cancel();
        verify(mockPrebidWebView, atLeast(1)).addView(any(View.class));
        verify(mockBaseJSInterface).onStateChange(eq(JSInterface.STATE_DEFAULT));
    }
}