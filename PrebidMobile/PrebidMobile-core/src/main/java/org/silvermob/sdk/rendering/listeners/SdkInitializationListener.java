package org.silvermob.sdk.rendering.listeners;

import org.jetbrains.annotations.NotNull;
import org.silvermob.sdk.api.data.InitializationStatus;
import org.silvermob.sdk.api.exceptions.InitError;

public interface SdkInitializationListener {

    void onInitializationComplete(@NotNull InitializationStatus status);

    @Deprecated
    default void onSdkInit() {
    }

    @Deprecated
    default void onSdkFailedToInit(InitError error) {
    }

}
