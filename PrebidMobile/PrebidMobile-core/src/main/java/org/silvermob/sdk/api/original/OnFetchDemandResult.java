package org.silvermob.sdk.api.original;

import androidx.annotation.NonNull;

import org.silvermob.sdk.api.data.BidInfo;

public interface OnFetchDemandResult {

    void onComplete(@NonNull BidInfo bidInfo);

}
