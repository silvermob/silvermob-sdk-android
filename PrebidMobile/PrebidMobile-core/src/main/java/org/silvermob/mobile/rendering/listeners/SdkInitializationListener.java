package org.silvermob.mobile.rendering.listeners;

import org.jetbrains.annotations.NotNull;
import org.silvermob.mobile.api.data.InitializationStatus;
import org.silvermob.mobile.api.exceptions.InitError;

public interface SdkInitializationListener {

    void onInitializationComplete(@NotNull InitializationStatus status);

    @Deprecated
    default void onSdkInit() {
    }

    @Deprecated
    default void onSdkFailedToInit(InitError error) {
    }

}
