package com.silvermob.sdk.rendering.sdk;

import com.silvermob.sdk.rendering.loading.FileDownloadListener;
import com.silvermob.sdk.rendering.sdk.scripts.DownloadListenerCreator;
import com.silvermob.sdk.rendering.sdk.scripts.JsScriptData;
import com.silvermob.sdk.rendering.sdk.scripts.JsScriptRequester;
import com.silvermob.sdk.rendering.sdk.scripts.JsScriptStorage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JsScriptsDownloaderTest {

    private AutoCloseable mockManager;

    @Mock
    private JsScriptStorage mockStorage;
    @Mock
    private JsScriptRequester mockRequester;
    @Mock
    private FileDownloadListener fileDownloadListener;
    @Mock
    private File mockFile;

    private final DownloadListenerCreator mockDownloadListenerCreator = filePath -> fileDownloadListener;

    private JsScriptsDownloader downloader;

    @Before
    public void setUp() throws Exception {
        mockManager = MockitoAnnotations.openMocks(this);

        downloader = new JsScriptsDownloader(
                mockStorage,
                mockRequester
        );
    }

    @After
    public void cleanUp() throws Exception {
        mockManager.close();
    }

    @Test
    public void checkPathsAndSkippingDownloadingIfFileIsAlreadyDownloaded() {
        when(
                mockStorage.getInnerFile(any())
        ).thenReturn(mockFile);

        when(
                mockStorage.isFileAlreadyDownloaded(any(), any())
        ).thenReturn(true);


        downloader.downloadScripts(mockDownloadListenerCreator);


        verify(mockStorage).getInnerFile("PBMJSLibraries/omsdk.js");
        verify(mockStorage).getInnerFile("PBMJSLibraries/mraid.js");

        verify(mockStorage).isFileAlreadyDownloaded(
                mockFile,
                "PBMJSLibraries/omsdk.js"
        );
        verify(mockStorage).isFileAlreadyDownloaded(
                mockFile,
                "PBMJSLibraries/mraid.js"
        );

        verify(mockStorage, never()).createParentFolders(any());
        verify(mockRequester, never()).download(any(), any(), any());
    }

    @Test
    public void checkDownloading() {
        when(
                mockStorage.getInnerFile(any())
        ).thenReturn(mockFile);
        when(
                mockStorage.isFileAlreadyDownloaded(any(), any())
        ).thenReturn(false);


        downloader.downloadScripts(mockDownloadListenerCreator);


        verify(mockStorage, atLeastOnce()).getInnerFile("PBMJSLibraries/omsdk.js");
        verify(mockStorage, atLeastOnce()).getInnerFile("PBMJSLibraries/mraid.js");

        verify(mockStorage).isFileAlreadyDownloaded(
                mockFile,
                "PBMJSLibraries/omsdk.js"
        );
        verify(mockStorage).isFileAlreadyDownloaded(
                mockFile,
                "PBMJSLibraries/mraid.js"
        );

        verify(mockRequester).download(mockFile, JsScriptData.openMeasurementData, mockDownloadListenerCreator);
        verify(mockRequester).download(mockFile, JsScriptData.mraidData, mockDownloadListenerCreator);
    }

    @Test
    public void checkScriptsDownloadListener_success() {
        String testPath = "test/path.txt";
        JsScriptsDownloader.ScriptDownloadListener downloadListener = new JsScriptsDownloader.ScriptDownloadListener(testPath, mockStorage);
        downloadListener.onFileDownloaded(testPath);

        verify(mockStorage, only()).markFileAsDownloadedCompletely(testPath);

        verify(mockStorage, never()).fileDownloadingFailed(any());
    }

    @Test
    public void checkScriptsDownloadListener_failure() {
        String testPath = "test/path.txt";
        JsScriptsDownloader.ScriptDownloadListener downloadListener = new JsScriptsDownloader.ScriptDownloadListener(testPath, mockStorage);
        downloadListener.onFileDownloadError("Error");

        verify(mockStorage, only()).fileDownloadingFailed(testPath);

        verify(mockStorage, never()).markFileAsDownloadedCompletely(any());
    }

}