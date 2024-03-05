# SilverMob Android SDK

SilverMob Android SDK allows you to display SilverMob advertisement in your app.

**Contents:**
- [Add SDK to your project and initialize it](#add-sdk-to-your-project-and-initialize-it)
    * [Integrate SilverMob](#integrate-silvermob)
    * [Initialize SDK in your app initialization code](#initialize-sdk-in-your-app-initialization-code)
- [Use SilverMob SDK standalone to display ads in your app](#use-silvermob-sdk-standalone-to-display-ads-in-your-app)
- [Integrate SilverMob SDK with your existing Applovin MAX SDK](#integrate-silvermob-sdk-with-your-existing-applovin-max-sdk)
    * [Integrate SilverMob Adapters](#integrate-silvermob-adapters)
    * [Adjust banner integration](#adjust-banner-integration)
    * [Setup SilverMob SDK adapter in your Applovin account](#setup-silvermob-sdk-adapter-in-your-applovin-account)
- [Acknowledgments](#acknowledgments)


## Add SDK to your project and initialize it
These steps are common for every integration.
### Integrate SilverMob
Root build.gradle
```groovy
allprojects {
    repositories {
        ...
        mavenCentral()
        ...
    }
}
```
App module build.gradle:
```groovy
implementation("com.silvermob:silvermob-sdk:2.2.5")
```
### Initialize SDK in your app initialization code
```kotlin
        SilverMob.setServerAccountId("YOUR_ACCOUNT_ID")
        SilverMob.setShareGeoLocation(true)
        SilverMob.setPbsDebug(true)//WARNING: Test mode for SilverMob test banners, disable in production
        SilverMob.initializeSdk(applicationContext) { status ->
            if (status == InitializationStatus.SUCCEEDED) {
                Log.d(TAG, "SDK initialized successfully!")
            } else {
                Log.e(TAG, "SDK initialization error: $status\n${status.description}")
            }
        }
```

## Use SilverMob SDK standalone to display ads in your app
**Code for the small banner integration:**
```kotlin
// 1. Create an Ad View
bannerView = BannerView(requireContext(), SILVERMOB_SDK_AD_UNIT, AdSize(WIDTH, HEIGHT))
bannerView?.setBannerListener(this)

// Add view to viewContainer
viewContainer?.addView(bannerView)
adView?.setAutoRefreshDelay(refreshTimeSeconds)

// 2. Load ad
bannerView?.loadAd()
```
**Code for interstitial integration:**
```kotlin
// 1. Create an Interstitial Ad Unit
interstitialAdUnit = InterstitialAdUnit(this, SILVERMOB_SDK_AD_UNIT, EnumSet.of(AdUnitFormat.BANNER,AdUnitFormat.VIDEO))
interstitialAdUnit?.setMinSizePercentage(AdSize(50,50)) //Set minimum % of screen ad should occupy
//2. Set load listener 
interstitialAdUnit?.setInterstitialAdUnitListener(object : InterstitialAdUnitListener {
    override fun onAdLoaded(interstitialAdUnit: InterstitialAdUnit?) {
        interstitialAdUnit?.show() //Show the ad
    }

    override fun onAdDisplayed(interstitialAdUnit: InterstitialAdUnit?) {}
    override fun onAdFailed(interstitialAdUnit: InterstitialAdUnit?, e: AdException?) {}
    override fun onAdClicked(interstitialAdUnit: InterstitialAdUnit?) {}
    override fun onAdClosed(interstitialAdUnit: InterstitialAdUnit?) {}
})

// 3. Load Ad
interstitialAdUnit?.loadAd()
```
_Note: Pay attention that the loadAd() should be called on the main thread._

**Code for rewarded video integration:**
```kotlin
// 1. Create an Ad Unit
adUnit = RewardedAdUnit(this, SILVERMOB_SDK_AD_UNIT)
//2. Set load listener 
val rewardedAdUnitListener = adUnit?.setRewardedAdUnitListener(object :
    RewardedAdUnitListener {
    override fun onAdLoaded(rewardedAdUnit: RewardedAdUnit?) {
        adUnit?.show()
    }

    override fun onAdDisplayed(rewardedAdUnit: RewardedAdUnit?) {}
    override fun onAdFailed(rewardedAdUnit: RewardedAdUnit?,exception: AdException?) {}
    override fun onAdClicked(rewardedAdUnit: RewardedAdUnit?) {}
    override fun onAdClosed(rewardedAdUnit: RewardedAdUnit?) {}
    override fun onUserEarnedReward(rewardedAdUnit: RewardedAdUnit?) {}
})
// 3. Load Ad
adUnit?.loadAd()
```


## Integrate SilverMob SDK with your existing Applovin MAX SDK
This guide assumes you already have [Applovin MAX SDK](https://dash.applovin.com/documentation/mediation/android/getting-started/integration) correctly integrated. 

*Don't forget to integrate and initialize SilverMob SDK as shown [here](#initialize-sdk-in-your-app-initialization-code).*

### Integrate SilverMob Adapters

```groovy
implementation("com.silvermob:silvermob-sdk-max-adapters:2.2.5")
```

### Setup SilverMob SDK adapter in your Applovin account

1. In your Applovin dashboard open [MAX > Manage > Networks](https://dash.applovin.com/o/mediation/networks/) and scroll to the bottom of the page. 
Select "Click here to add a Custom Network".
![networks list](https://files.silvermob.com/img/2024-02-02_14-37-57.png)


2. Fill out network data as shown on the screenshot.
![custom network settings](https://files.silvermob.com/img/2024-02-02_14-38-35.png)
Custom Network Name `SilverMob`, iOS Adapter Class Name `SilverMobMaxMediationAdapter`, Android Adapter Class Name `com.applovin.mediation.adapters.SilverMobMaxMediationAdapter`.
*Note: don't leave iOS adapter name empty or adapter integration might not work.*


3. Enable SilverMob network for your Ad Units: go to ad unit waterfall settings, scroll to **"Custom Networks"**, enable **SilverMob** and adjust settings accordingly.
Add your **SilverMob ad unit id in "Placement ID"** for your placement. Supported ad types: **Banner, Interstitial, MREC, Rewarded.** Wait around 60 minutes for Applovin to update Ad Unit and Network changes.

![ad unit settings](https://files.silvermob.com/img/2024-02-02_14-39-29.png)
*Note: mediation adapters don't work in Test Mode, be sure to disable it for testing mediation.*
## Acknowledgments

This project is based on a fork of [Prebid Mobile Android SDK](https://github.com/prebid/prebid-mobile-android), licensed under the Apache License 2.0. We are grateful to the original authors for their work and contributions to the open-source community.