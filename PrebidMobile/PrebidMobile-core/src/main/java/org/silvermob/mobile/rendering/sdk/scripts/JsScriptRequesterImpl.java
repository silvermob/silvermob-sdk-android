package org.silvermob.mobile.rendering.sdk.scripts;

import android.os.AsyncTask;

import org.silvermob.mobile.rendering.loading.FileDownloadTask;
import org.silvermob.mobile.rendering.networking.BaseNetworkTask;
import org.silvermob.mobile.rendering.utils.helpers.AppInfoManager;

import java.io.File;

public class JsScriptRequesterImpl implements JsScriptRequester {

    public void download(File saveToFile, JsScriptData script, DownloadListenerCreator listener) {
        BaseNetworkTask.GetUrlParams params = new BaseNetworkTask.GetUrlParams();
        params.url = script.getUrl();
        params.userAgent = AppInfoManager.getUserAgent();
        params.requestType = "GET";
        params.name = BaseNetworkTask.DOWNLOAD_TASK;

        FileDownloadTask omSdkTask = new FileDownloadTask(listener.create(script.getPath()), saveToFile);
        omSdkTask.setIgnoreContentLength(true);
        omSdkTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

}
