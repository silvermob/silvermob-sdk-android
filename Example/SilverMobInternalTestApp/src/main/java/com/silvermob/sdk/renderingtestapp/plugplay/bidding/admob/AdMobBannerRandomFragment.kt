package com.silvermob.sdk.renderingtestapp.plugplay.bidding.admob

import android.util.Log
import com.silvermob.sdk.admob.PrebidBannerAdapter
import com.silvermob.sdk.rendering.bidding.display.BidResponseCache
import kotlin.random.Random

class AdMobBannerRandomFragment : AdMobBannerFragment() {

    private val random = Random.Default

    override fun loadAd() {
        adUnit?.fetchDemand { result ->
            Log.d("Prebid", "Fetch demand result: $result")
            randomRemovingBidResponseFromCache()
            bannerView?.loadAd(adRequest!!)
        }
    }

    private fun randomRemovingBidResponseFromCache() {
        val randomValue = random.nextInt(0, 2)
        val doRemove = randomValue == 1
        if (doRemove) {
            Log.d("RandomAdMobBanner", "Random removing response!")
            val responseId = adRequestExtras?.getString(com.silvermob.sdk.admob.PrebidBannerAdapter.EXTRA_RESPONSE_ID) ?: ""
            if (responseId.isNotBlank()) {
                com.silvermob.sdk.rendering.bidding.display.BidResponseCache.getInstance().popBidResponse(responseId)
            }
        } else {
            Log.d("RandomAdMobBanner", "Without removing.")
        }
    }

}