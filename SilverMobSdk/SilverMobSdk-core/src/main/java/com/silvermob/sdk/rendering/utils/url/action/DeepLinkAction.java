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

package com.silvermob.sdk.rendering.utils.url.action;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.silvermob.sdk.rendering.utils.helpers.ExternalViewerUtils;
import com.silvermob.sdk.rendering.utils.url.ActionNotResolvedException;
import com.silvermob.sdk.rendering.utils.url.UrlHandler;
import com.silvermob.sdk.rendering.utils.helpers.ExternalViewerUtils;
import com.silvermob.sdk.rendering.utils.url.ActionNotResolvedException;
import com.silvermob.sdk.rendering.utils.url.UrlHandler;

import static com.silvermob.sdk.SilverMob.SCHEME_HTTP;
import static com.silvermob.sdk.SilverMob.SCHEME_HTTPS;

public class DeepLinkAction implements UrlAction {
    @Override
    public boolean shouldOverrideUrlLoading(Uri uri) {
        final String scheme = uri.getScheme();
        return !TextUtils.isEmpty(scheme)
               && !(SCHEME_HTTP.equals(scheme) || SCHEME_HTTPS.equals(scheme))
               && !DeepLinkPlusAction.SCHEME_DEEPLINK_PLUS.equals(scheme);
    }

    @Override
    public void performAction(Context context, UrlHandler urlHandler, Uri uri)
    throws ActionNotResolvedException {
        ExternalViewerUtils.launchApplicationUrl(context, uri);
    }

    @Override
    public boolean shouldBeTriggeredByUserAction() {
        return true;
    }
}
