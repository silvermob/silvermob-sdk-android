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

package com.silvermob.sdk.rendering.networking.modelcontrollers;

import android.app.Activity;
import android.content.Context;

import com.silvermob.sdk.api.exceptions.AdException;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.rendering.networking.ResponseHandler;
import com.silvermob.sdk.rendering.networking.parameters.AdRequestInput;
import com.silvermob.sdk.rendering.sdk.ManagersResolver;
import com.silvermob.sdk.rendering.sdk.PrebidContextHolder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 19)
public class BidRequesterTest {

    private Context context;
    private AdUnitConfiguration adConfiguration;
    private AdRequestInput adRequestInput;

    @Mock
    private ResponseHandler mockResponseHandler;

    @After
    public void clean() {
        PrebidContextHolder.clearContext();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context = Robolectric.buildActivity(Activity.class).create().get();
        adConfiguration = new AdUnitConfiguration();
        adRequestInput = new AdRequestInput();
        ManagersResolver.getInstance().prepare(context);
    }

    @Test
    public void whenStartAdRequestAndContextNull_OnErrorWithExceptionCalled() {
        adConfiguration.setConfigId("test");
        BidRequester requester = new BidRequester(adConfiguration, adRequestInput, mockResponseHandler);
        requester.startAdRequest();
        verify(mockResponseHandler).onErrorWithException(any(AdException.class), anyLong());
    }

    @Test
    public void whenStartAdRequestAndNoConfigId_OnErrorCalled() {
        adConfiguration.setConfigId(null);
        BidRequester requester = new BidRequester(adConfiguration, adRequestInput, mockResponseHandler);
        requester.startAdRequest();
        verify(mockResponseHandler).onError(anyString(), anyLong());
    }

    @Test
    public void whenStartAdRequestAndInitValid_InitAdId() {
        PrebidContextHolder.setContext(context);

        adConfiguration.setConfigId("test");
        BidRequester requester = spy(new BidRequester(adConfiguration, adRequestInput, mockResponseHandler));
        requester.startAdRequest();
        verify(requester).makeAdRequest();
    }

}