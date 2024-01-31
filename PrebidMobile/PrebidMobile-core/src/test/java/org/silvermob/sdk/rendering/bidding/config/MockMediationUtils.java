package org.silvermob.sdk.rendering.bidding.config;

import androidx.annotation.Nullable;
import org.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import org.silvermob.sdk.rendering.bidding.display.PrebidMediationDelegate;

import java.util.HashMap;

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