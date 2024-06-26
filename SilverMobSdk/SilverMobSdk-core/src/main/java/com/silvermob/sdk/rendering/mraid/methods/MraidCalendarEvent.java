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

import com.silvermob.sdk.rendering.sdk.ManagersResolver;
import com.silvermob.sdk.rendering.sdk.calendar.CalendarEventWrapper;
import com.silvermob.sdk.rendering.views.webview.mraid.BaseJSInterface;
import com.silvermob.sdk.rendering.views.webview.mraid.JSInterface;

import org.json.JSONObject;

public class MraidCalendarEvent {

    private BaseJSInterface jsi;

    public MraidCalendarEvent(BaseJSInterface jsInterface) {
        this.jsi = jsInterface;
    }

    public void createCalendarEvent(String parameters) {
        if (parameters != null && !parameters.equals("")) {

            try {
                JSONObject params = new JSONObject(parameters);
                CalendarEventWrapper calendarEventWrapper = new CalendarEventWrapper(params);
                ManagersResolver.getInstance().getDeviceManager().createCalendarEvent(calendarEventWrapper);
            }
            catch (Exception e) {
                jsi.onError("create_calendar_event_error", JSInterface.ACTION_CREATE_CALENDAR_EVENT);
            }
        }
    }
}