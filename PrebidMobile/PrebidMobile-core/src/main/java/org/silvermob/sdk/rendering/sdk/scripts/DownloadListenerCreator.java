package org.silvermob.sdk.rendering.sdk.scripts;

import org.silvermob.sdk.rendering.loading.FileDownloadListener;

public interface DownloadListenerCreator {

    FileDownloadListener create(String filePath);

}