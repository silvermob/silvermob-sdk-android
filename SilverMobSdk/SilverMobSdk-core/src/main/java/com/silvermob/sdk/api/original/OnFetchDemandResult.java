package com.silvermob.sdk.api.original;

import com.silvermob.sdk.api.data.BidInfo;

import androidx.annotation.NonNull;

import com.silvermob.sdk.api.data.BidInfo;

public interface OnFetchDemandResult {

    void onComplete(@NonNull BidInfo bidInfo);

}
