package com.silvermob.sdk.prebidkotlindemo.ui

import androidx.annotation.StringRes
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import com.silvermob.sdk.prebidkotlindemo.R
import com.silvermob.sdk.prebidkotlindemo.testcases.TestCase
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MraidAdsTest(
    @StringRes private val testCaseTitleId: Int,
    private val testCaseName: String
) : com.silvermob.sdk.prebidkotlindemo.ui.BaseAdsTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun arguments() = listOf(
            arrayOf(R.string.in_app_display_banner_mraid_resize, "In-App MRAID Resize"),
            arrayOf(R.string.in_app_display_banner_mraid_resize_errors, "In-App MRAID Resize With Errors"),
            arrayOf(R.string.in_app_display_banner_mraid_expand, "In-App MRAID Expand"),
        )
    }

    @Test
    fun mraidAdsShouldBeDisplayed() {
        testAd(testCaseTitleId)
    }

    override fun checkAd(testCase: TestCase) {
        when (testCase.titleStringRes) {
            R.string.in_app_display_banner_mraid_resize -> checkMraidResize()
            R.string.in_app_display_banner_mraid_resize_errors -> checkMraidResizeWithErrors()
            R.string.in_app_display_banner_mraid_expand -> checkMraidExpand()
        }
    }

    private fun checkMraidResize() {
        val clickToResize = By.res("adContainer")
        val findClickToResize = device.wait(Until.findObject(clickToResize), timeout)
        assertNotNull(findClickToResize)
        findClickToResize.click()

        val closeButton = By.clazz(".*ImageView".toPattern())
        val findCloseButton = device.wait(Until.findObject(closeButton), timeout)
        assertNotNull(findCloseButton)
        findCloseButton.click()
        device.pressBack()

    }

    private fun checkMraidResizeWithErrors() {
        val clickToggleScreen = By.res("toggleOffscreenDiv")
        val findClickToggleScreen = device.wait(Until.findObject(clickToggleScreen), timeout)
        assertNotNull(findClickToggleScreen)
        findClickToggleScreen.click()

        val resizeLeft = By.res("resizeLeftText")
        val findResizeLeft = device.wait(Until.findObject(resizeLeft), timeout)
        assertNotNull(findResizeLeft)
        findResizeLeft.click()

        val resizeRight = By.res("resizeRightText")
        val findResizeRight = device.wait(Until.findObject(resizeRight), timeout)
        assertNotNull(findResizeRight)
        findResizeRight.click()

        val resizeUp = By.res("resizeUpText")
        val findResizeUp = device.wait(Until.findObject(resizeUp), timeout)
        assertNotNull(findResizeUp)
        findResizeUp.click()

        val resizeDown = By.res("resizeDownText")
        val findResizeDown = device.wait(Until.findObject(resizeDown), timeout)
        assertNotNull(findResizeDown)
        findResizeDown.click()

        val closeButton = By.res("closeButtonDiv")
        val findCloseButton = device.wait(Until.findObject(closeButton), timeout)
        assertNotNull(findCloseButton)
        findCloseButton.click()

        device.pressBack()
    }

    private fun checkMraidExpand() {
        val clickToExpand = By.res("maindiv")
        val findClickToExpand = device.wait(Until.findObject(clickToExpand), timeout)
        assertNotNull(findClickToExpand)
        findClickToExpand.click()

        val closeButton = By.res("closediv")
        val findCloseButton = device.wait(Until.findObject(closeButton), timeout)
        assertNotNull(findCloseButton)
        findCloseButton.click()
        device.pressBack()
    }

}