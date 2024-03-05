package com.silvermob.sdk.renderingtestapp.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.silvermob.sdk.renderingtestapp.plugplay.utilities.consent.ConsentUpdateManager
import org.json.JSONArray
import org.json.JSONObject

object CommandLineArgumentParser {

    private val adUnitSpecificData = AdUnitSpecificData()

    class AdUnitSpecificData(
            var extKeywords: String? = null,
            var extData: Map<String, List<String>>? = null,
            var appContentData: com.silvermob.sdk.ContentObject? = null,
            var userData: com.silvermob.sdk.DataObject? = null,
    )

    fun parse(intent: Intent?, context: Context) {
        val extras = intent?.extras ?: return
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        extras.getBoolean("shareGeo").let { shareGeo ->
            com.silvermob.sdk.SilverMob.setShareGeoLocation(shareGeo)
        }

        extras.getString("targetingDomain")?.let { targetingDomain ->
            com.silvermob.sdk.TargetingParams.setDomain(targetingDomain)
        }

        extras.getString("gppString")?.let { gppStringValue ->
            GppHelper(preferences).addGppStringTestValue(gppStringValue)
        }

        extras.getString("gppSid")?.let { gppSidValue ->
            GppHelper(preferences).addGppSidTestValue(gppSidValue)
        }

        extras.getString("EXTRA_OPEN_RTB")?.let {
            extractOpenRtbExtra(it, context)
        }

        extras.getString("EXTRA_CONSENT_V1")?.let {
            handleConsentExtra(it, preferences)
        }

        extras.getString("EXTRA_EIDS")?.let {
            extractEidsExtras(it)
        }

        /* Global data */
        /* Example: arrayOf("value1", "value2") */
        extras.getStringArray("BIDDER_ACCESS_CONTROL_LIST")?.forEach {
            com.silvermob.sdk.TargetingParams.addBidderToAccessControlList(it)
        }
        /* Example: {"key1": ["value1"],"key2": ["value2"]} */
        extras.getString("ADD_USER_EXT_DATA")?.let {
            parseUserExtData(it)
        }
        /* Example: {"key1": ["value1"],"key2": ["value2"]} */
        extras.getString("ADD_APP_EXT")?.let {
            parseAppExtData(it)
        }
        /* Example: "appKeyword" */
        extras.getString("ADD_APP_KEYWORD")?.let {
            com.silvermob.sdk.TargetingParams.addExtKeyword(it)
        }
        /* Example: "userKeyword" */
        extras.getString("ADD_USER_KEYWORD")?.let {
            com.silvermob.sdk.TargetingParams.addUserKeyword(it)
        }

        /* Example: {"key1": ["value1"],"key2": ["value2"]} */
        extras.getString("ADD_ADUNIT_CONTEXT")?.let {
            adUnitSpecificData.extData = parseJsonToMapOfStringsAndStringLists(it)
        }
        /* Example: "keywords1,keywords2" */
        extras.getString("ADD_ADUNIT_KEYWORD")?.let {
            adUnitSpecificData.extKeywords = it
        }
        /* Example: "key value" */
        extras.getString("ADD_APP_CONTENT_DATA_EXT")?.let {
            adUnitSpecificData.appContentData = parseAppContentData(it)
        }
        /* Example: "key value" */
        extras.getString("ADD_USER_DATA_EXT")?.let {
            adUnitSpecificData.userData = parseUserData(it)
        }
    }

    fun addAdUnitSpecificData(adUnit: com.silvermob.sdk.BannerAdUnit) {
        val extData = adUnitSpecificData.extData
        if (extData != null) {
            for (key in extData.keys) {
                for (value in extData[key]!!) {
                    adUnit.addExtData(key, value)
                }
            }
        }

        val extKeywords = adUnitSpecificData.extKeywords
        if (extKeywords != null) {
            adUnit.addExtKeyword(extKeywords)
        }

        val appContentData = adUnitSpecificData.appContentData
        if (appContentData != null) {
            adUnit.appContent = appContentData
        }

        val userData = adUnitSpecificData.userData
        if (userData != null) {
            adUnit.addUserData(userData)
        }
    }

    fun addAdUnitSpecificData(bannerView: com.silvermob.sdk.api.rendering.BannerView) {
        val extData = adUnitSpecificData.extData
        if (extData != null) {
            for (key in extData.keys) {
                for (value in extData[key]!!) {
                    bannerView.addExtData(key, value)
                }
            }
        }

        val extKeywords = adUnitSpecificData.extKeywords
        if (extKeywords != null) {
            bannerView.addExtKeyword(extKeywords)
        }

        val appContentData = adUnitSpecificData.appContentData
        if (appContentData != null) {
            bannerView.setAppContent(appContentData)
        }

        val userData = adUnitSpecificData.userData
        if (userData != null) {
            bannerView.addUserData(userData)
        }
    }

    fun addAdUnitSpecificData(interstitial: com.silvermob.sdk.api.rendering.BaseInterstitialAdUnit) {
        val extData = adUnitSpecificData.extData
        if (extData != null) {
            for (key in extData.keys) {
                for (value in extData[key]!!) {
                    interstitial.addExtData(key, value)
                }
            }
        }

        val extKeywords = adUnitSpecificData.extKeywords
        if (extKeywords != null) {
            interstitial.addExtKeyword(extKeywords)
        }

        val appContentData = adUnitSpecificData.appContentData
        if (appContentData != null) {
            interstitial.appContent = appContentData
        }

        val userData = adUnitSpecificData.userData
        if (userData != null) {
            interstitial.addUserData(userData)
        }
    }

    fun addAdUnitSpecificData(mediationAdUnit: com.silvermob.sdk.api.mediation.MediationBaseAdUnit) {
        val extData = adUnitSpecificData.extData
        if (extData != null) {
            for (key in extData.keys) {
                for (value in extData[key]!!) {
                    mediationAdUnit.addExtData(key, value)
                }
            }
        }

        val extKeywords = adUnitSpecificData.extKeywords
        if (extKeywords != null) {
            mediationAdUnit.addExtKeyword(extKeywords)
        }

        val appContentData = adUnitSpecificData.appContentData
        if (appContentData != null) {
            mediationAdUnit.appContent = appContentData
        }

        val userData = adUnitSpecificData.userData
        if (userData != null) {
            mediationAdUnit.addUserData(userData)
        }
    }

    fun addAdUnitSpecificData(nativeAdUnit: com.silvermob.sdk.api.mediation.MediationNativeAdUnit) {
        val extData = adUnitSpecificData.extData
        if (extData != null) {
            for (key in extData.keys) {
                for (value in extData[key]!!) {
                    nativeAdUnit.addExtData(key, value)
                }
            }
        }

        val extKeywords = adUnitSpecificData.extKeywords
        if (extKeywords != null) {
            nativeAdUnit.addExtKeyword(extKeywords)
        }

        val appContentData = adUnitSpecificData.appContentData
        if (appContentData != null) {
            nativeAdUnit.appContent = appContentData
        }

        val userData = adUnitSpecificData.userData
        if (userData != null) {
            nativeAdUnit.addUserData(userData)
        }
    }


    private fun extractOpenRtbExtra(openRtbListJson: String, context: Context) {
        val openRtbExtrasList = try {
            Gson().fromJson<OpenRtbExtra>(openRtbListJson, object : TypeToken<OpenRtbExtra>() {}.type)
        } catch (ex: Exception) {
            Log.d("CommandLineArguments", "Unable to parse provided OpenRTB list ${Log.getStackTraceString(ex)}")
            Toast.makeText(
                context,
                "Unable to parse provided OpenRTB. Provided JSON might contain an error",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (openRtbExtrasList != null) {
            OpenRtbConfigs.setTargeting(openRtbExtrasList)
        }
    }

    private fun handleConsentExtra(configurationJson: String, preferences: SharedPreferences) {
        val consentUpdateManager = ConsentUpdateManager(preferences)
        consentUpdateManager.updateConsentConfiguration(configurationJson)
    }

    private fun extractEidsExtras(eidsJsonString: String) {
        val eidsJsonArray = JSONArray(eidsJsonString)
        for (i in 0 until eidsJsonArray.length()) {
            val jsonObject = eidsJsonArray.get(i)
            if (jsonObject is JsonObject) {
                val source = jsonObject.get("source").asString
                val identifier = jsonObject.get("identifier").asString
                if (source == null || identifier == null) {
                    val aType = jsonObject.get("atype")
                    com.silvermob.sdk.TargetingParams.storeExternalUserId(
                        if (aType == null) {
                            com.silvermob.sdk.ExternalUserId(source, identifier, null, null)
                        } else {
                            com.silvermob.sdk.ExternalUserId(source, identifier, aType.asInt, null)
                        }
                    )
                }
            }
        }
    }

    private fun parseUserExtData(json: String) {
        val map = parseJsonToMapOfStringsAndStringLists(json)
        map.forEach {
            for (value in it.value) {
                com.silvermob.sdk.TargetingParams.addUserData(it.key, value)
            }
        }
    }

    private fun parseAppExtData(json: String) {
        val map = parseJsonToMapOfStringsAndStringLists(json)
        map.forEach {
            for (value in it.value) {
                com.silvermob.sdk.TargetingParams.addExtData(it.key, value)
            }
        }
    }

    private fun parseAppContentData(value: String): com.silvermob.sdk.ContentObject {
        return com.silvermob.sdk.ContentObject().apply {
            val dataObject = com.silvermob.sdk.DataObject()
            val ext = com.silvermob.sdk.rendering.models.openrtb.bidRequests.Ext()

            val split = value.split(" ")
            if (split.size >= 2) {
                ext.put(split[0], split[1])
            } else {
                ext.put("key", value)
            }

            dataObject.setExt(ext)
            addData(dataObject)
        }
    }

    private fun parseUserData(value: String): com.silvermob.sdk.DataObject {
        return com.silvermob.sdk.DataObject().apply {
            val ext = com.silvermob.sdk.rendering.models.openrtb.bidRequests.Ext()

            val split = value.split(" ")
            if (split.size == 2) {
                ext.put(split[0], split[1])
            } else {
                ext.put("key", value)
            }

            setExt(ext)
        }
    }


    private fun parseJsonToMapOfStringsAndStringLists(json: String): Map<String, List<String>> {
        val result = mutableMapOf<String, List<String>>()

        val jsonObject = JSONObject(json)
        val keys = jsonObject.names() ?: return result
        for (i in 0 until keys.length()) {
            val key = keys.get(i) as String
            result[key] = jsonArrayToStringList(jsonObject.getJSONArray(key))
        }

        return result
    }

    private fun jsonArrayToStringList(array: JSONArray): List<String> {
        val result = mutableListOf<String>()
        for (i in 0 until array.length()) {
            val string = array.get(i) as String
            result.add(string)
        }
        return result
    }

}