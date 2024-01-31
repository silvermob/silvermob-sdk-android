package org.silvermob.mobile.reflection;

import org.silvermob.mobile.AdUnit;
import org.silvermob.mobile.rendering.bidding.loader.BidLoader;

public class AdUnitReflection {

    public static void setBidLoader(AdUnit adUnit, BidLoader bidLoader) {
        Reflection.setVariableTo(adUnit, "bidLoader", bidLoader);
    }

}
