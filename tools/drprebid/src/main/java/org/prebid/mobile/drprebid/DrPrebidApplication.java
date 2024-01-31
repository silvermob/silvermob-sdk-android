package org.silvermob.mobile.drprebid;

import androidx.multidex.MultiDexApplication;
import org.silvermob.mobile.ServerRequestSettings;
import org.silvermob.mobile.drprebid.managers.LineItemKeywordManager;

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
