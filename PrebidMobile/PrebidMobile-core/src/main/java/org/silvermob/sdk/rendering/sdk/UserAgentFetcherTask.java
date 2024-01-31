package org.silvermob.sdk.rendering.sdk;

import android.text.TextUtils;
import android.webkit.WebSettings;

import org.silvermob.sdk.LogUtil;
import org.silvermob.sdk.rendering.utils.helpers.AppInfoManager;

public class UserAgentFetcherTask implements Runnable {

    private static final String TAG = "UserAgentFetcherTask";

    @Override
    public void run() {
        String userAgent = "";

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                userAgent = WebSettings.getDefaultUserAgent(PrebidContextHolder.getContext());
            }
        } catch (Exception any) {
            LogUtil.error(TAG, "Failed to get user agent");
        }

        if (TextUtils.isEmpty(userAgent) || userAgent.contains("UNAVAILABLE")) {
            userAgent = "Mozilla/5.0 (Linux; U; Android " + android.os.Build.VERSION.RELEASE + ";" + " " + AppInfoManager.getDeviceName() + ")";
        }

        AppInfoManager.setUserAgent(userAgent);
    }

}
