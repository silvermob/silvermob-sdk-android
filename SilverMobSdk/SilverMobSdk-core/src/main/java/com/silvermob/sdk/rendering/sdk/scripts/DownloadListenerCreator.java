package com.silvermob.sdk.rendering.sdk.scripts;

import com.silvermob.sdk.rendering.loading.FileDownloadListener;

public interface DownloadListenerCreator {

    FileDownloadListener create(String filePath);

}