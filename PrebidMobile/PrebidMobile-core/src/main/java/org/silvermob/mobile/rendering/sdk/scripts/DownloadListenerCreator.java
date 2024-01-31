package org.silvermob.mobile.rendering.sdk.scripts;

import org.silvermob.mobile.rendering.loading.FileDownloadListener;

public interface DownloadListenerCreator {

    FileDownloadListener create(String filePath);

}