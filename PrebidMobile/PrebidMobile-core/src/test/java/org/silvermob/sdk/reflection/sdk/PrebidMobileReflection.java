package org.silvermob.sdk.reflection.sdk;

import android.content.Context;

import org.mockito.Mockito;
import org.silvermob.sdk.SilverMob;
import org.silvermob.sdk.reflection.Reflection;
import org.silvermob.sdk.rendering.sdk.InitializationNotifier;
import org.silvermob.sdk.rendering.sdk.PrebidContextHolder;

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
