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

package com.silvermob.sdk.rendering.errors;

import com.silvermob.sdk.api.exceptions.AdException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VastParseErrorTest {

    @Test
    public void testVastParseError() {
        VastParseError vastErr = new VastParseError("VASTERROR");
        assertEquals(AdException.INTERNAL_ERROR + ": Failed to parse VAST. VASTERROR", vastErr.getMessage());
    }
}