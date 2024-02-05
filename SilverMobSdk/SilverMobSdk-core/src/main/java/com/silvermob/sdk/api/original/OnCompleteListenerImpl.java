package com.silvermob.sdk.api.original;

import com.silvermob.sdk.OnCompleteListener;
import com.silvermob.sdk.OnCompleteListener2;
import com.silvermob.sdk.ResultCode;
import com.silvermob.sdk.Util;
import com.silvermob.sdk.api.data.BidInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.silvermob.sdk.OnCompleteListener;
import com.silvermob.sdk.OnCompleteListener2;
import com.silvermob.sdk.ResultCode;
import com.silvermob.sdk.Util;
import com.silvermob.sdk.api.data.BidInfo;

import java.util.Map;

/**
 * Listener implementation for multiformat PrebidAdUnit.
 */
class OnCompleteListenerImpl implements OnCompleteListener, OnCompleteListener2 {

    @Nullable
    private final Object adObject;
    @NonNull
    private final MultiformatAdUnitFacade adUnit;
    @NonNull
    private final OnFetchDemandResult listener;

    OnCompleteListenerImpl(
            @NonNull MultiformatAdUnitFacade adUnit,
            @Nullable Object adObject,
            @NonNull OnFetchDemandResult listener
    ) {
        this.adObject = adObject;
        this.adUnit = adUnit;
        this.listener = listener;
    }

    @Override
    public void onComplete(ResultCode resultCode) {
        notifyListener(resultCode);
    }

    @Override
    public void onComplete(ResultCode resultCode, @Nullable Map<String, String> unmodifiableMap) {
        notifyListener(resultCode);
    }


    private void notifyListener(ResultCode resultCode) {
        BidInfo bidInfo = BidInfo.create(resultCode, adUnit.getBidResponse(), adUnit.getConfiguration());
        Util.saveCacheId(bidInfo.getNativeCacheId(), adObject);
        listener.onComplete(bidInfo);
    }

}