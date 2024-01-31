package org.silvermob.sdk.drprebid;

import androidx.multidex.MultiDexApplication;
import org.silvermob.sdk.ServerRequestSettings;
import org.silvermob.sdk.drprebid.managers.LineItemKeywordManager;

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
