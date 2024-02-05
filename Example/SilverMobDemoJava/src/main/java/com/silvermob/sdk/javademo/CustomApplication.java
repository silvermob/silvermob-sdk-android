/*
 *    Copyright 2018-2019 Prebid.org, Inc.
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

package com.silvermob.sdk.javademo;

import android.app.Application;
import android.util.Log;

import com.silvermob.sdk.ExternalUserId;
import com.silvermob.sdk.SilverMob;
import com.silvermob.sdk.api.data.InitializationStatus;
import com.silvermob.sdk.javademo.utils.Settings;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomApplication extends Application {

    private static final String TAG = "PrebidCustomApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        Settings.init(this);
        initSilverMob();
        initPrebidExternalUserIds();
    }

    private void initSilverMob() {
        SilverMob.setShareGeoLocation(true);
        SilverMob.setServerAccountId("13c4f9d0-6d7d-4398-8e39-f08052acbc70");
        SilverMob.initializeSdk(getApplicationContext(), status -> {
            if (status == InitializationStatus.SUCCEEDED) {
                Log.d(TAG, "SDK initialized successfully!");
            } else {
                Log.e(TAG, "SDK initialization error: " + status.getDescription());
            }
        });
    }

    private void initPrebidExternalUserIds() {
        ArrayList<ExternalUserId> externalUserIdArray = new ArrayList<>();
        externalUserIdArray.add(new ExternalUserId("adserver.org", "111111111111", null, new HashMap<String, Object>() {{
            put("rtiPartner", "TDID");
        }}));
        externalUserIdArray.add(new ExternalUserId("netid.de", "999888777", null, null));
        externalUserIdArray.add(new ExternalUserId("criteo.com", "_fl7bV96WjZsbiUyQnJlQ3g4ckh5a1N", null, null));
        externalUserIdArray.add(new ExternalUserId("liveramp.com", "AjfowMv4ZHZQJFM8TpiUnYEyA81Vdgg", null, null));
        externalUserIdArray.add(new ExternalUserId("sharedid.org", "111111111111", 1, new HashMap<String, Object>() {{
            put("third", "01ERJWE5FS4RAZKG6SKQ3ZYSKV");
        }}));
        SilverMob.setExternalUserIds(externalUserIdArray);
    }

}
