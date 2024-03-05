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

package com.silvermob.sdk.rendering.views.webview.mraid;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.silvermob.sdk.rendering.views.webview.WebViewBase;

public class BannerJSInterface extends BaseJSInterface {
    /**
     * Instantiate the interface and set the context
     */
    public BannerJSInterface(Context context, WebViewBase adBaseView, JsExecutor jsExecutor) {
        super(context, adBaseView, jsExecutor);
    }

    @Override
    @JavascriptInterface
    public String getPlacementType() {
        return "inline";
    }
}
