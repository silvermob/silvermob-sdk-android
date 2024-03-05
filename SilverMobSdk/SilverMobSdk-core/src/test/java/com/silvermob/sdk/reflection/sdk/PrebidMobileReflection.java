package com.silvermob.sdk.reflection.sdk;

import android.content.Context;

import com.silvermob.sdk.SilverMob;
import com.silvermob.sdk.reflection.Reflection;
import com.silvermob.sdk.rendering.sdk.InitializationNotifier;
import com.silvermob.sdk.rendering.sdk.PrebidContextHolder;

import org.mockito.Mockito;

public class PrebidMobileReflection {

    public static void setCustomStatusEndpoint(String url) {
        Reflection.setStaticVariableTo(SilverMob.class, "customStatusEndpoint", url);
    }

    public static String getCustomStatusEndpoint() {
        return Reflection.getStaticFieldOf(SilverMob.class, "customStatusEndpoint");
    }

    public static void setFlagsThatSdkIsInitialized() {
        Reflection.setStaticVariableTo(InitializationNotifier.class, "tasksCompletedSuccessfully", true);
        Reflection.setStaticVariableTo(InitializationNotifier.class, "initializationInProgress", false);
        PrebidContextHolder.setContext(Mockito.mock(Context.class));
    }

    public static void setFlagsThatSdkIsNotInitialized() {
        Reflection.setStaticVariableTo(InitializationNotifier.class, "tasksCompletedSuccessfully", false);
        Reflection.setStaticVariableTo(InitializationNotifier.class, "initializationInProgress", false);
        PrebidContextHolder.clearContext();
    }

}
