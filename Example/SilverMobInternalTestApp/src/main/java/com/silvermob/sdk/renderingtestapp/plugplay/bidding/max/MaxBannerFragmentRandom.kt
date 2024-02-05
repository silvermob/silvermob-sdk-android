package com.silvermob.sdk.renderingtestapp.plugplay.bidding.max

import android.util.Log
import com.applovin.mediation.adapters.SilverMobMaxMediationAdapter
import kotlin.random.Random

class MaxBannerFragmentRandom : MaxBannerFragment() {

    private val random = Random.Default

    override fun loadAd() {
        adUnit?.fetchDemand {
            randomRemovingResponseIdFromCache()
            adView?.loadAd()
        }
    }

    private fun randomRemovingResponseIdFromCache() {
        val randomValue = random.nextInt(0, 2)
        val doRemove = randomValue == 1
        if (doRemove) {
            Log.d("RandomAdMobBanner", "Random removing response id!")
            adView?.setLocalExtraParameter(SilverMobMaxMediationAdapter.EXTRA_RESPONSE_ID, "")
        } else {
            Log.d("RandomAdMobBanner", "Without removing.")
        }
    }

}