package org.silvermob.mobile.drprebid.model;

import org.silvermob.mobile.drprebid.Constants;

public enum AdServer {
    GOOGLE_AD_MANAGER(Constants.Settings.AdServerCodes.GOOGLE_AD_MANAGER);

    private int code;

    AdServer(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
