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

package com.silvermob.sdk.rendering.networking.tracking;

import static com.silvermob.sdk.SilverMob.SDK_VERSION;

import android.os.AsyncTask;

import com.silvermob.sdk.rendering.networking.BaseNetworkTask;
import com.silvermob.sdk.rendering.networking.ResponseHandler;
import com.silvermob.sdk.rendering.utils.helpers.AppInfoManager;

public class ServerConnection {

    public static void fireWithResult(String url, ResponseHandler responseHandler) {
        BaseNetworkTask networkTask = new BaseNetworkTask(responseHandler);
        BaseNetworkTask.GetUrlParams params = new BaseNetworkTask.GetUrlParams();
        params.url = url;
        params.requestType = "GET";
        params.userAgent = AppInfoManager.getUserAgent();
        params.name = "recordevents";

        networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    public static void fireStatusRequest(String url, ResponseHandler responseHandler) {
        BaseNetworkTask networkTask = new BaseNetworkTask(responseHandler);
        BaseNetworkTask.GetUrlParams params = new BaseNetworkTask.GetUrlParams();
        params.url = url;
        params.requestType = "GET";
        params.userAgent = AppInfoManager.getUserAgent();
        params.name = BaseNetworkTask.STATUS_TASK;
        params.queryParams = "?ver=" + SDK_VERSION;

        networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    public static void fireAndForget(String resourceURL) {
        fireWithResult(resourceURL, null);
    }

    public static void fireAndForgetImpressionUrl(String impressionUrl) {

        BaseNetworkTask.GetUrlParams params = new BaseNetworkTask.GetUrlParams();
        params.url = impressionUrl;
        params.requestType = "GET";
        params.userAgent = AppInfoManager.getUserAgent();
        params.name = BaseNetworkTask.REDIRECT_TASK;

        BaseNetworkTask networkTask = new ImpressionUrlTask(null);
        networkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }
}
