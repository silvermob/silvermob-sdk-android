package com.silvermob.sdk.prebidkotlindemo.testcases

import com.silvermob.sdk.prebidkotlindemo.R
import com.silvermob.sdk.prebidkotlindemo.activities.ads.admob.AdMobDisplayBanner320x50Activity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.admob.AdMobDisplayInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.admob.AdMobNativeActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.admob.AdMobVideoInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.admob.AdMobVideoRewardedActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.applovin.AppLovinMaxDisplayBanner300x250Activity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.applovin.AppLovinMaxDisplayBanner320x50Activity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.applovin.AppLovinMaxDisplayBanner728x90Activity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.applovin.AppLovinMaxDisplayInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.applovin.AppLovinMaxNativeActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.applovin.AppLovinMaxVideoRewardedActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiDisplayBanner300x250Activity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiDisplayBanner320x50Activity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiDisplayBannerMultiSizeActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiDisplayInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiInStreamActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiMultiformatBannerActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiMultiformatBannerVideoNativeInAppActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiMultiformatBannerVideoNativeStylesActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiMultiformatInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiNativeInAppActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiNativeStylesActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiVideoBannerActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiVideoInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.original.GamOriginalApiVideoRewardedActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.rendering.GamRenderingApiDisplayBanner320x50Activity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.rendering.GamRenderingApiDisplayInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.rendering.GamRenderingApiNativeActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.rendering.GamRenderingApiVideoBannerActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.rendering.GamRenderingApiVideoInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.gam.rendering.GamRenderingApiVideoRewardedActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.inapp.InAppDisplayBanner320x50Activity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.inapp.InAppDisplayInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.inapp.InAppVideoBannerActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.inapp.InAppVideoInterstitialActivity
import com.silvermob.sdk.prebidkotlindemo.activities.ads.inapp.InAppVideoRewardedActivity

object TestCaseRepository {

    lateinit var lastTestCase: TestCase

    fun getList() = arrayListOf(
        /* GAM Original API */
        TestCase(
            R.string.gam_original_display_banner_320x50,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiDisplayBanner320x50Activity::class.java,
        ),
        TestCase(
            R.string.gam_original_display_banner_300x250,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiDisplayBanner300x250Activity::class.java,
        ),
        TestCase(
            R.string.gam_original_display_banner_multi_size,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiDisplayBannerMultiSizeActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_video_banner,
            AdFormat.VIDEO_BANNER,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiVideoBannerActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_multiformat_banner,
            AdFormat.MULTIFORMAT,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiMultiformatBannerActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_multiformat_banner_video_native_in_app,
            AdFormat.MULTIFORMAT,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiMultiformatBannerVideoNativeInAppActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_multiformat_banner_video_native_styles,
            AdFormat.MULTIFORMAT,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiMultiformatBannerVideoNativeStylesActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_display_interstitial,
            AdFormat.DISPLAY_INTERSTITIAL,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiDisplayInterstitialActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_video_interstitial,
            AdFormat.VIDEO_INTERSTITIAL,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiVideoInterstitialActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_multiformat_interstitial,
            AdFormat.MULTIFORMAT,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiMultiformatInterstitialActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_video_rewarded,
            AdFormat.VIDEO_REWARDED,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiVideoRewardedActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_video_in_stream,
            AdFormat.IN_STREAM_VIDEO,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiInStreamActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_native_in_app,
            AdFormat.NATIVE,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiNativeInAppActivity::class.java,
        ),
        TestCase(
            R.string.gam_original_native_styles,
            AdFormat.NATIVE,
            IntegrationKind.GAM_ORIGINAL,
            GamOriginalApiNativeStylesActivity::class.java,
        ),

        /* GAM Rendering API */
        TestCase(
            R.string.gam_rendering_display_banner_320x50,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.GAM_RENDERING,
            GamRenderingApiDisplayBanner320x50Activity::class.java,
        ),
        TestCase(
            R.string.gam_rendering_video_banner,
            AdFormat.VIDEO_BANNER,
            IntegrationKind.GAM_RENDERING,
            GamRenderingApiVideoBannerActivity::class.java,
        ),
        TestCase(
            R.string.gam_rendering_display_interstitial,
            AdFormat.DISPLAY_INTERSTITIAL,
            IntegrationKind.GAM_RENDERING,
            GamRenderingApiDisplayInterstitialActivity::class.java,
        ),
        TestCase(
            R.string.gam_rendering_video_interstitial,
            AdFormat.VIDEO_INTERSTITIAL,
            IntegrationKind.GAM_RENDERING,
            GamRenderingApiVideoInterstitialActivity::class.java,
        ),
        TestCase(
            R.string.gam_rendering_video_rewarded,
            AdFormat.VIDEO_REWARDED,
            IntegrationKind.GAM_RENDERING,
            GamRenderingApiVideoRewardedActivity::class.java,
        ),
        TestCase(
            R.string.gam_rendering_native,
            AdFormat.NATIVE,
            IntegrationKind.GAM_RENDERING,
            GamRenderingApiNativeActivity::class.java,
        ),

        /* In-App (no ad server) */
        TestCase(
            R.string.in_app_display_banner_320x50,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.STANDALONE,
            InAppDisplayBanner320x50Activity::class.java,
        ),
/*        TestCase(
            R.string.in_app_display_banner_multi_size,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.STANDALONE,
            InAppDisplayBannerMultiSizeActivity::class.java,
        ),
        TestCase(
            R.string.in_app_display_banner_mraid_resize,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.STANDALONE,
            InAppDisplayBannerMraidResizeActivity::class.java,
        ),
        TestCase(
            R.string.in_app_display_banner_mraid_expand,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.STANDALONE,
            InAppDisplayBannerMraidExpandActivity::class.java,
        ),
        TestCase(
            R.string.in_app_display_banner_mraid_resize_errors,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.STANDALONE,
            InAppDisplayBannerMraidResizeWithErrorsActivity::class.java,
        ),*/
        TestCase(
            R.string.in_app_video_banner,
            AdFormat.VIDEO_BANNER,
            IntegrationKind.STANDALONE,
            InAppVideoBannerActivity::class.java,
        ),
        TestCase(
            R.string.in_app_display_interstitial,
            AdFormat.DISPLAY_INTERSTITIAL,
            IntegrationKind.STANDALONE,
            InAppDisplayInterstitialActivity::class.java,
        ),
        TestCase(
            R.string.in_app_video_interstitial,
            AdFormat.VIDEO_INTERSTITIAL,
            IntegrationKind.STANDALONE,
            InAppVideoInterstitialActivity::class.java,
        ),
        /*TestCase(
            R.string.in_app_video_interstitial_end_card,
            AdFormat.VIDEO_INTERSTITIAL,
            IntegrationKind.STANDALONE,
            InAppVideoInterstitialWithEndCardActivity::class.java,
        ),
        TestCase(
            R.string.in_app_video_interstitial_multi_format,
            AdFormat.MULTIFORMAT,
            IntegrationKind.STANDALONE,
            InAppVideoInterstitialMultiFormatActivity::class.java,
        ),*/
        TestCase(
            R.string.in_app_video_rewarded,
            AdFormat.VIDEO_REWARDED,
            IntegrationKind.STANDALONE,
            InAppVideoRewardedActivity::class.java,
        ),
        /*TestCase(
            R.string.in_app_native,
            AdFormat.NATIVE,
            IntegrationKind.STANDALONE,
            InAppNativeActivity::class.java,
        ),
*/
        /* AdMob */
        TestCase(
            R.string.ad_mob_display_banner_320x50,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.ADMOB,
            AdMobDisplayBanner320x50Activity::class.java,
        ),
        TestCase(
            R.string.ad_mob_display_interstitial,
            AdFormat.DISPLAY_INTERSTITIAL,
            IntegrationKind.ADMOB,
            AdMobDisplayInterstitialActivity::class.java,
        ),
        TestCase(
            R.string.ad_mob_video_interstitial,
            AdFormat.VIDEO_INTERSTITIAL,
            IntegrationKind.ADMOB,
            AdMobVideoInterstitialActivity::class.java,
        ),
        TestCase(
            R.string.ad_mob_video_rewarded,
            AdFormat.VIDEO_REWARDED,
            IntegrationKind.ADMOB,
            AdMobVideoRewardedActivity::class.java,
        ),
        TestCase(
            R.string.ad_mob_native,
            AdFormat.NATIVE,
            IntegrationKind.ADMOB,
            AdMobNativeActivity::class.java,
        ),

        /* AppLovin MAX */
        TestCase(
            R.string.app_lovin_max_display_banner_320x50,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.MAX,
            AppLovinMaxDisplayBanner320x50Activity::class.java,
        ),
        TestCase(
            R.string.app_lovin_max_display_banner_728x90,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.MAX,
            AppLovinMaxDisplayBanner728x90Activity::class.java,
        ),
        TestCase(
            R.string.app_lovin_max_display_banner_300x250,
            AdFormat.DISPLAY_BANNER,
            IntegrationKind.MAX,
            AppLovinMaxDisplayBanner300x250Activity::class.java,
        ),
        TestCase(
            R.string.app_lovin_max_display_interstitial,
            AdFormat.DISPLAY_INTERSTITIAL,
            IntegrationKind.MAX,
            AppLovinMaxDisplayInterstitialActivity::class.java,
        ),
        TestCase(
            R.string.app_lovin_max_video_rewarded,
            AdFormat.VIDEO_REWARDED,
            IntegrationKind.MAX,
            AppLovinMaxVideoRewardedActivity::class.java,
        ),
        TestCase(
            R.string.app_lovin_max_native,
            AdFormat.NATIVE,
            IntegrationKind.MAX,
            AppLovinMaxNativeActivity::class.java,
        ),
    )

}