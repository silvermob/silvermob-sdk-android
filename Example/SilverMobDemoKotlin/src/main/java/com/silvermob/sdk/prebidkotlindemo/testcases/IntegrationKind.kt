package com.silvermob.sdk.prebidkotlindemo.testcases

enum class IntegrationKind(
    val adServer: String
) {

    GAM_ORIGINAL("GAM (Original API)"),
    GAM_RENDERING("GAM (Rendering API)"),
    STANDALONE("Standalone"),
    ADMOB("AdMob"),
    MAX("AppLovin MAX");

}