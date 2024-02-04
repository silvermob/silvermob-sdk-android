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

package com.silvermob.sdk.prebidkotlindemo

import android.app.Application
import android.util.Log
import com.applovin.sdk.AppLovinSdk
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.silvermob.sdk.SilverMob
import com.silvermob.sdk.api.data.InitializationStatus
import com.silvermob.sdk.prebidkotlindemo.utils.Settings

class CustomApplication : Application() {

    companion object {
        private const val TAG = "PrebidCustomApplication"
    }

    override fun onCreate() {
        super.onCreate()
        initSilverMobSdk()
        initAdMob()
        initApplovinMax()
        Settings.init(this)
    }

    private fun initSilverMobSdk() {
        com.silvermob.sdk.SilverMob.setServerAccountId("13c4f9d0-6d7d-4398-8e39-f08052acbc70")
        com.silvermob.sdk.SilverMob.initializeSdk(applicationContext) { status ->
            if (status == com.silvermob.sdk.api.data.InitializationStatus.SUCCEEDED) {
                Log.d(TAG, "SDK initialized successfully!")
            } else {
                Log.e(TAG, "SDK initialization error: $status\n${status.description}")
            }
        }
        com.silvermob.sdk.SilverMob.setShareGeoLocation(true)
    }

    private fun initAdMob() {
        MobileAds.initialize(this) {
            Log.d("MobileAds", "Initialization complete.")
        }
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(
            listOf("38250D98D8E3A07A2C03CD3552013B29")
        ).build()
        MobileAds.setRequestConfiguration(configuration)
    }

    private fun initApplovinMax() {
        AppLovinSdk.getInstance(this).mediationProvider = "max"
        AppLovinSdk.getInstance(this).initializeSdk { }
        AppLovinSdk.getInstance(this).settings.setVerboseLogging(true)
    }

}