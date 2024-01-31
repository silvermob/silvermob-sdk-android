package org.silvermob.mobile.reflection.sdk;

import android.content.Context;

import org.mockito.Mockito;
import org.silvermob.mobile.PrebidMobile;
import org.silvermob.mobile.reflection.Reflection;
import org.silvermob.mobile.rendering.sdk.InitializationNotifier;
import org.silvermob.mobile.rendering.sdk.PrebidContextHolder;

public class PrebidMobileReflection {

    public static void setCustomStatusEndpoint(String url) {
        Reflection.setStaticVariableTo(PrebidMobile.class, "customStatusEndpoint", url);
    }

    public static String getCustomStatusEndpoint() {
        return Reflection.getStaticFieldOf(PrebidMobile.class, "customStatusEndpoint");
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
