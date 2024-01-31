package org.silvermob.sdk.prebidkotlindemo.testcases

import android.app.Activity
import androidx.annotation.StringRes

data class TestCase(
    @StringRes val titleStringRes: Int,
    val adFormat: AdFormat,
    val integrationKind: IntegrationKind,
    val activity: Class<out Activity>
)
