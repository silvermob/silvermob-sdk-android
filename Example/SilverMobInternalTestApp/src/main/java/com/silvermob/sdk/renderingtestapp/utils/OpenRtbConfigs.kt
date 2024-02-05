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

package com.silvermob.sdk.renderingtestapp.utils

import com.google.gson.Gson
import com.silvermob.sdk.TargetingParams
import com.silvermob.sdk.rendering.models.openrtb.bidRequests.Ext
import java.lang.reflect.InvocationTargetException

object OpenRtbConfigs {

    var impExtData: Map<String, List<String>>? = null

    fun setTargeting(openRtbExtra: OpenRtbExtra) {
        if (openRtbExtra.age != 0) {
            com.silvermob.sdk.TargetingParams.setUserAge(openRtbExtra.age)
        }
        if (openRtbExtra.appStoreUrl != null) {
            com.silvermob.sdk.TargetingParams.setStoreUrl(openRtbExtra.appStoreUrl)
        }
        if (openRtbExtra.userId != null) {
            com.silvermob.sdk.TargetingParams.setUserId(openRtbExtra.userId)
        }
        if (openRtbExtra.gender != null) {
            com.silvermob.sdk.TargetingParams.setGender(com.silvermob.sdk.TargetingParams.GENDER.genderByKey(openRtbExtra.gender))
        }
        if (openRtbExtra.buyerId != null) {
            com.silvermob.sdk.TargetingParams.setBuyerId(openRtbExtra.buyerId)
        }
        if (openRtbExtra.customData != null) {
            com.silvermob.sdk.TargetingParams.setUserCustomData(openRtbExtra.customData)
        }
        if (openRtbExtra.keywords != null) {
            openRtbExtra.keywords.split(",").forEach {
                com.silvermob.sdk.TargetingParams.addUserKeyword(it)
            }
        }
        if (openRtbExtra.geo != null) {
            com.silvermob.sdk.TargetingParams.setUserLatLng(openRtbExtra.geo.lat, openRtbExtra.geo.lon)
        }
        if (openRtbExtra.publisherName != null) {
            com.silvermob.sdk.TargetingParams.setPublisherName(openRtbExtra.publisherName)
        }
        if (openRtbExtra.userExt?.isNotEmpty() == true) {
            val ext = com.silvermob.sdk.rendering.models.openrtb.bidRequests.Ext()
            val gson = Gson()
            for (key in openRtbExtra.userExt.keys) {
                ext.put(key, gson.toJson(openRtbExtra.userExt[key]))
            }
            com.silvermob.sdk.TargetingParams.setUserExt(ext)
        }
        if (openRtbExtra.accessControl?.isNotEmpty() == true) {
            for (bidder in openRtbExtra.accessControl) {
                com.silvermob.sdk.TargetingParams.addBidderToAccessControlList(bidder)
            }
        }
        if (openRtbExtra.userData?.isNotEmpty() == true) {
            for (key in openRtbExtra.userData.keys) {
                val dataList = openRtbExtra.userData[key]
                if (dataList != null) {
                    for (data in dataList) {
                        com.silvermob.sdk.TargetingParams.addUserData(key, data)
                    }
                }
            }
        }
        if (openRtbExtra.appExtData?.isNotEmpty() == true) {
            for (key in openRtbExtra.appExtData.keys) {
                val dataList = openRtbExtra.appExtData[key]
                if (dataList != null) {
                    for (data in dataList) {
                        com.silvermob.sdk.TargetingParams.addExtData(key, data)
                    }
                }
            }
        }
        if (openRtbExtra.impExtData?.isNotEmpty() == true) {
            impExtData = openRtbExtra.impExtData
        }
    }

    fun setImpExtDataTo(adView: Any?) {
        if (adView == null || impExtData == null || impExtData?.isEmpty() == true) {
            return
        }
        for (key in impExtData!!.keys) {
            val dataList = impExtData!![key]
            if (dataList != null) {
                for (data in dataList) {
                    callMethodOnObject(adView, "addExtData", key, data)
                }
            }
        }
    }

    private fun callMethodOnObject(target: Any, methodName: String, vararg params: Any): Any? {
        try {
            val len = params.size
            val classes: Array<Class<*>?> = arrayOfNulls(len)
            for (i in 0 until len) {
                classes[i] = params[i].javaClass
            }
            val method = target.javaClass.getMethod(methodName, *classes)
            return method.invoke(target, *params)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
        catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}
