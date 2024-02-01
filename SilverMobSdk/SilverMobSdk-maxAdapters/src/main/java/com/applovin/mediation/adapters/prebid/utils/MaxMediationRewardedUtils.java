package com.applovin.mediation.adapters.prebid.utils;

import androidx.annotation.Nullable;
import com.applovin.mediation.adapters.SilverMobMaxMediationAdapter;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.display.PrebidMediationDelegate;

import java.util.HashMap;

public class MaxMediationRewardedUtils implements PrebidMediationDelegate {

    private final MaxRewardedAd rewardedAd;

    public MaxMediationRewardedUtils(MaxRewardedAd rewardedAd) {
        this.rewardedAd = rewardedAd;
    }

    @Override
    public void setResponseToLocalExtras(@Nullable BidResponse response) {
        if (rewardedAd != null) {
            String responseId; if (response != null) {
                responseId = response.getId();
            } else {
                responseId = null;
            } rewardedAd.setLocalExtraParameter(SilverMobMaxMediationAdapter.EXTRA_RESPONSE_ID, responseId);
        }
    }

    @Override
    public boolean canPerformRefresh() {
        return false;
    }

    @Override
    public void handleKeywordsUpdate(@Nullable HashMap<String, String> keywords) {}

}
