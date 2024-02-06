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

package com.silvermob.sdk.rendering.networking.urlBuilder;

import com.silvermob.sdk.rendering.networking.parameters.AdRequestInput;

import org.json.JSONObject;
import com.silvermob.sdk.LogUtil;
import com.silvermob.sdk.rendering.networking.parameters.AdRequestInput;

public class BidUrlComponents extends URLComponents {
    private final static String TAG = BidUrlComponents.class.getSimpleName();

    public BidUrlComponents(String baseUrl, AdRequestInput adRequestInput) {
        super(baseUrl, adRequestInput);
    }

    @Override
    public String getQueryArgString() {
        String openrtb = "";
        try {
            JSONObject bidRequestJson = adRequestInput.getBidRequest().getJsonObject();

            //TODO slvrmb: constant for test mode
            //bidRequestJson.put("test",1);
            if (bidRequestJson.length() > 0) {
                openrtb = bidRequestJson.toString();
            }
        }
        catch (Exception e) {
            LogUtil.error(TAG, "Failed to add OpenRTB query arg");
        }

        return openrtb;
    }
}
