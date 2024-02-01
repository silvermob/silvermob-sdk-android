package com.applovin.mediation.adapters.prebid.utils;

import androidx.annotation.Nullable;
import com.applovin.mediation.adapters.SilverMobMaxMediationAdapter;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.display.PrebidMediationDelegate;

import java.util.HashMap;

public class MaxMediationInterstitialUtils implements PrebidMediationDelegate {

    private final MaxInterstitialAd interstitialAd;

    public MaxMediationInterstitialUtils(MaxInterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
    }

    @Override
    public void setResponseToLocalExtras(@Nullable BidResponse response) {
        if (interstitialAd != null) {
            String responseId; if (response != null) {
                responseId = response.getId();
            } else {
                responseId = null;
            } interstitialAd.setLocalExtraParameter(SilverMobMaxMediationAdapter.EXTRA_RESPONSE_ID, responseId);
        }
    }

    @Override
    public boolean canPerformRefresh() {
        return false;
    }

    @Override
    public void handleKeywordsUpdate(@Nullable HashMap<String, String> keywords) {}

}
