package com.silvermob.sdk.rendering.listeners;

import com.silvermob.sdk.api.data.InitializationStatus;
import com.silvermob.sdk.api.exceptions.InitError;

import org.jetbrains.annotations.NotNull;

public interface SdkInitializationListener {

    void onInitializationComplete(@NotNull InitializationStatus status);

    @Deprecated
    default void onSdkInit() {
    }

    @Deprecated
    default void onSdkFailedToInit(InitError error) {
    }

}
