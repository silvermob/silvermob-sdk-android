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

package com.silvermob.sdk.rendering.mraid.methods;

import com.silvermob.sdk.rendering.models.HTMLCreative;
import com.silvermob.sdk.rendering.models.internal.MraidEvent;
import com.silvermob.sdk.rendering.views.webview.WebViewBase;
import com.silvermob.sdk.rendering.views.webview.mraid.JsExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class MraidEventHandlerNotifierRunnableTest {
    private MraidEventHandlerNotifierRunnable mraidEventHandlerNotifierRunnable;

    @Mock
    HTMLCreative mockHTMLCreative;
    @Mock
    WebViewBase mockWebViewBase;
    @Mock
    JsExecutor mockJsExecutor;
    @Mock
    MraidEvent mockMraidEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mraidEventHandlerNotifierRunnable = new MraidEventHandlerNotifierRunnable(
                mockHTMLCreative, mockWebViewBase, mockMraidEvent, mockJsExecutor
        );
    }

    @Test
    public void runWithValidHtmlCreativeAndWebViewBase_HandleMraidEventsInCreative() {
        mraidEventHandlerNotifierRunnable.run();

        verify(mockHTMLCreative).handleMRAIDEventsInCreative(mockMraidEvent, mockWebViewBase);
    }

    @Test
    public void runWithInValidHtmlCreativeOrWebViewBase_NoInteractions() {
        MraidEventHandlerNotifierRunnable mraidEventHandlerNotifierRunnable =
            new MraidEventHandlerNotifierRunnable(null, mockWebViewBase, mockMraidEvent, mockJsExecutor
            );
        mraidEventHandlerNotifierRunnable.run();

        verifyNoInteractions(mockHTMLCreative);
        verifyNoInteractions(mockJsExecutor);
    }

    @Test
    public void runWithValidHtmlCreativeAndWebViewBase_ExecuteNativeCallComplete() {
        mraidEventHandlerNotifierRunnable.run();

        verify(mockJsExecutor).executeNativeCallComplete();
    }
}
