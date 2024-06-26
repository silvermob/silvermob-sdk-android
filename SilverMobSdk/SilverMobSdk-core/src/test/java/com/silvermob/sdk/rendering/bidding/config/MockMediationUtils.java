package com.silvermob.sdk.rendering.bidding.config;

import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.display.PrebidMediationDelegate;

import java.util.HashMap;

import androidx.annotation.Nullable;

public class MockMediationUtils implements PrebidMediationDelegate {

    @Override
    public void handleKeywordsUpdate(@Nullable @org.jetbrains.annotations.Nullable HashMap<String, String> keywords) {

    }

    @Override
    public void setResponseToLocalExtras(@Nullable @org.jetbrains.annotations.Nullable BidResponse response) {

    }

    @Override
    public boolean canPerformRefresh() {
        return false;
    }

}