package com.silvermob.sdk.eventhandlers.global;

import com.google.android.gms.ads.MobileAds;
import com.silvermob.sdk.SilverMob;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 29)
public class GoogleAdVersionTest {

    @Test
    public void checkIfLastVersionUsed() {
        String currentVersion = MobileAds.getVersion().toString();
        assertEquals(
                "Google Ad SDK was updated to " + currentVersion + "! " +
                        "Please test Prebid SDK with the new version, resolve compilation problems, rewrite deprecated code. " +
                        "After testing you must update PrebidMobile.TESTED_GOOGLE_SDK_VERSION" +
                        "and also must update version in publishing XML files (scripts/Maven/*.xml.",
                currentVersion,
                SilverMob.TESTED_GOOGLE_SDK_VERSION
        );
    }

}
