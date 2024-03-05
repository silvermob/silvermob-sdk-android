package com.silvermob.sdk.rendering.bidding.display;

import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;

import java.util.HashMap;

import androidx.annotation.Nullable;

/**
 * PrebidMediationDelegate is a delegate of custom mediation platform.
 */
public interface PrebidMediationDelegate {

    /**
     * Sets keywords into a given mediation ad object
     */
    public void handleKeywordsUpdate(@Nullable HashMap<String, String> keywords);

    /**
     * Sets response into a given mediation ad object
     */
    public void setResponseToLocalExtras(@Nullable BidResponse response);

    /**
     * Checks if banner view is visible, and it is possible to make refresh.
     */
    public boolean canPerformRefresh();

}
