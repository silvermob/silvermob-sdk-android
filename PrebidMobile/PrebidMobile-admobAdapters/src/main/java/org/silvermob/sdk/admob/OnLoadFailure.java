package org.silvermob.sdk.admob;

import com.google.android.gms.ads.AdError;

interface OnLoadFailure {

    void run(AdError adError);

}
