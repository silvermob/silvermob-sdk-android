package com.silvermob.sdk.rendering.sdk.scripts;

import java.io.File;

public interface JsScriptRequester {

    public void download(
            File saveToFile,
            JsScriptData data,
            DownloadListenerCreator downloadListener
    );

}
