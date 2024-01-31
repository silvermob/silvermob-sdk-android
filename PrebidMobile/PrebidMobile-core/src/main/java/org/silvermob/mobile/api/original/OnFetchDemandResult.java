package org.silvermob.mobile.api.original;

import androidx.annotation.NonNull;

import org.silvermob.mobile.api.data.BidInfo;

public interface OnFetchDemandResult {

    void onComplete(@NonNull BidInfo bidInfo);

}
