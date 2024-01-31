package com.silvermob.sdk.reflection.sdk;

import com.silvermob.sdk.reflection.Reflection;
import com.silvermob.sdk.rendering.sdk.ManagersResolver;
import com.silvermob.sdk.reflection.Reflection;
import com.silvermob.sdk.rendering.sdk.ManagersResolver;

public class ManagersResolverReflection {

    public static void resetManagers(ManagersResolver resolver) {
        Reflection.setVariableTo(resolver, "deviceManager", null);
        Reflection.setVariableTo(resolver, "locationManager", null);
        Reflection.setVariableTo(resolver, "connectionManager", null);
        Reflection.setVariableTo(resolver, "userConsentManager", null);
    }

}
