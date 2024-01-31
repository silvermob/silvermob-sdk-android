package com.silvermob.sdk.reflection;

import com.silvermob.sdk.AdUnit;
import com.silvermob.sdk.rendering.bidding.loader.BidLoader;

public class AdUnitReflection {

    public static void setBidLoader(AdUnit adUnit, BidLoader bidLoader) {
        Reflection.setVariableTo(adUnit, "bidLoader", bidLoader);
    }

}
