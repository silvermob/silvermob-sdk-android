package com.silvermob.sdk.api.data;

import com.silvermob.sdk.CacheManager;
import com.silvermob.sdk.ResultCode;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.rendering.bidding.data.bid.Bid;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BidInfo {

    @NonNull
    private final ResultCode resultCode;
    @Nullable
    private Map<String, String> targetingKeywords;
    @Nullable
    private Map<String, String> events;
    @Nullable
    private String nativeCacheId;
    @Nullable
    private Integer exp;

    /**
     * Key for {@link #getEvents()} map to get win event.
     */
    public static final String EVENT_WIN = "ext.prebid.events.win";
    /**
     * Key for {@link #getEvents()} map to get impression event.
     */
    public static final String EVENT_IMP = "ext.prebid.events.imp";


    private BidInfo(@NonNull ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    @NonNull
    public ResultCode getResultCode() {
        return resultCode;
    }

    @Nullable
    public Map<String, String> getTargetingKeywords() {
        return targetingKeywords;
    }

    @Nullable
    public String getNativeCacheId() {
        return nativeCacheId;
    }

    @Nullable
    public Integer getExp() {
        return exp;
    }

    @Nullable
    public Map<String, String> getEvents() {
        return events;
    }


    @NonNull
    public static BidInfo create(
            @NonNull ResultCode resultCode,
            @Nullable BidResponse bidResponse,
            @Nullable AdUnitConfiguration configuration
    ) {
        BidInfo bidInfo = new BidInfo(resultCode);
        if (bidResponse == null) {
            return bidInfo;
        }

        bidInfo.targetingKeywords = bidResponse.getTargeting();

        bidInfo.exp = bidResponse.getExpirationTimeSeconds();

        Bid winningBid = bidResponse.getWinningBid();
        if (winningBid != null) {
            bidInfo.events = winningBid.getEvents();
        }

        boolean isNative = configuration != null && configuration.getNativeConfiguration() != null;
        if (isNative && bidInfo.resultCode == ResultCode.SUCCESS) {
            bidInfo.nativeCacheId = CacheManager.save(bidResponse.getWinningBidJson());
        }

        return bidInfo;
    }

}
