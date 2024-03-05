package com.silvermob.sdk.api.original;

import com.silvermob.sdk.api.data.BidInfo;

import androidx.annotation.NonNull;

public interface OnFetchDemandResult {

    void onComplete(@NonNull BidInfo bidInfo);

}
