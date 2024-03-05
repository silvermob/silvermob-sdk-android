package com.silvermob.sdk.rendering.sdk.scripts;

import android.os.AsyncTask;

import com.silvermob.sdk.rendering.loading.FileDownloadTask;
import com.silvermob.sdk.rendering.networking.BaseNetworkTask;
import com.silvermob.sdk.rendering.utils.helpers.AppInfoManager;

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
