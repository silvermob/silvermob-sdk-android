package com.silvermob.sdk.drprebid;

import androidx.multidex.MultiDexApplication;
import com.silvermob.sdk.ServerRequestSettings;
import com.silvermob.sdk.drprebid.managers.LineItemKeywordManager;

public class DrPrebidApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            ServerRequestSettings.update(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LineItemKeywordManager.getInstance().refreshCacheIds(this);
    }
}
