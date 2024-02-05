package com.silvermob.sdk.api.original;

import com.silvermob.sdk.LogUtil;
import com.silvermob.sdk.ResultCode;
import com.silvermob.sdk.SilverMob;
import com.silvermob.sdk.api.data.BidInfo;

import static com.silvermob.sdk.SilverMob.AUTO_REFRESH_DELAY_MAX;
import static com.silvermob.sdk.SilverMob.AUTO_REFRESH_DELAY_MIN;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.silvermob.sdk.LogUtil;
import com.silvermob.sdk.ResultCode;
import com.silvermob.sdk.api.data.BidInfo;

public class PrebidAdUnit {

    @NonNull
    private final String configId;
    @Nullable
    private MultiformatAdUnitFacade adUnit;

    public PrebidAdUnit(@NonNull String configId) {
        this.configId = configId;
    }

    public void fetchDemand(
            PrebidRequest request,
            OnFetchDemandResult listener
    ) {
        baseFetchDemand(request, null, listener);
    }

    public void fetchDemand(
            Object adObject,
            PrebidRequest request,
            OnFetchDemandResult listener
    ) {
        baseFetchDemand(request, adObject, listener);
    }

    public void setAutoRefreshInterval(
            @IntRange(from = SilverMob.AUTO_REFRESH_DELAY_MIN / 1000, to = SilverMob.AUTO_REFRESH_DELAY_MAX / 1000) int seconds
    ) {
        if (adUnit != null) {
            adUnit.setAutoRefreshInterval(seconds);
        }
    }

    public void resumeAutoRefresh() {
        if (adUnit != null) {
            adUnit.resumeAutoRefresh();
        }
    }

    public void stopAutoRefresh() {
        if (adUnit != null) {
            adUnit.stopAutoRefresh();
        }
    }

    public void destroy() {
        if (adUnit != null) {
            adUnit.destroy();
        }
    }


    private void baseFetchDemand(
            @Nullable PrebidRequest request,
            @Nullable Object adObject,
            @Nullable OnFetchDemandResult userListener
    ) {
        if (userListener == null) {
            LogUtil.error("Parameter OnFetchDemandResult in fetchDemand() must be not null.");
            return;
        }

        if (request == null || requestDoesNotHaveAnyConfiguration(request)) {
            userListener.onComplete(BidInfo.create(ResultCode.INVALID_PREBID_REQUEST_OBJECT, null, null));
            return;
        }

        if (adUnit != null) {
            adUnit.destroy();
        }

        adUnit = new MultiformatAdUnitFacade(configId, request);

        OnCompleteListenerImpl innerListener = new OnCompleteListenerImpl(adUnit, adObject, userListener);
        if (adObject != null) {
            adUnit.fetchDemand(adObject, innerListener);
        } else {
            adUnit.fetchDemand(innerListener);
        }
    }


    private boolean requestDoesNotHaveAnyConfiguration(PrebidRequest request) {
        return request.getBannerParameters() == null &&
                request.getVideoParameters() == null &&
                request.getNativeParameters() == null;
    }

}
