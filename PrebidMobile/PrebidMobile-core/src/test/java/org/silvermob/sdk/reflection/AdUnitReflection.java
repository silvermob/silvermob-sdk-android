package org.silvermob.sdk.reflection;

import org.silvermob.sdk.AdUnit;
import org.silvermob.sdk.rendering.bidding.loader.BidLoader;

public class AdUnitReflection {

    public static void setBidLoader(AdUnit adUnit, BidLoader bidLoader) {
        Reflection.setVariableTo(adUnit, "bidLoader", bidLoader);
    }

}
