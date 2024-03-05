package com.applovin.mediation.adapters;

import com.silvermob.sdk.Util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SilverMobMaxMediationAdapterTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void compareAdapterResponseKeyWithUtilsKey() {
        assertEquals(SilverMobMaxMediationAdapter.EXTRA_RESPONSE_ID, Util.APPLOVIN_MAX_RESPONSE_ID_KEY);
    }

    @Test
    public void compareAdapterKeywordsKeyWithUtilsKey() {
        assertEquals(SilverMobMaxMediationAdapter.EXTRA_KEYWORDS_ID, Util.APPLOVIN_MAX_KEYWORDS_KEY);
    }

}