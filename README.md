# SilverMob Android SDK

Easily include the SilverMob SDK using Maven. Simply add this line to your gradle dependencies:

```groovy
implementation 'com.silvermob:silvermob-sdk:2.2.0'
```

## Integrate SilverMob SDK with your existing Applovin MAX SDK
This guide assumes you already have [Applovin MAX SDK](https://dash.applovin.com/documentation/mediation/android/getting-started/integration) correctly integrated. 

### Integrate SilverMob Adapters
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
implementation('com.silvermob:silbermob-sdk-max-adapters:x.x.x')
```
### Adjust banner integration
**Code for the small banner integration:**
```kotlin
// 1. Create MaxAdView
adView = MaxAdView(adUnitId, requireContext())
adView?.setListener(createListener())
adWrapperView.addView(adView)

// 2. Create MaxMediationBannerUtils
val mediationUtils = MaxMediationBannerUtils(adView)

// 3. Create MediationBannerAdUnit
adUnit = MediationBannerAdUnit(
        requireContext(),
        SILVERMOB_SDK_AD_UNIT,
        AdSize(width, height),
        mediationUtils
)

// 4. Make a bid request
adUnit?.fetchDemand {

    // 5. Make an ad request to MAX
    adView?.loadAd()
}
```
**Code for interstitial integration:**
```kotlin
// 1. Create MaxInterstitialAd
maxInterstitialAd = MaxInterstitialAd(adUnitId, activity)
maxInterstitialAd?.setListener(createListener())
        
// 2. Create MaxMediationInterstitialUtils
val mediationUtils = MaxMediationInterstitialUtils(maxInterstitialAd)

// 3. Create MediationInterstitialAdUnit
adUnit = MediationInterstitialAdUnit(
            activity,
            SILVERMOB_SDK_AD_UNIT,
            EnumSet.of(AdUnitFormat.BANNER),
            mediationUtils
        )
        
// 4. Make a bid request
adUnit?.fetchDemand {
 
    // 5. Make an ad request to MAX
    maxInterstitialAd?.loadAd()
}
```
The default ad format for interstitial is **DISPLAY**. In order to make a `multiformat bid request`, set the respective values into the `adUnitFormats` parameter.
```kotlin
adUnit = MediationInterstitialAdUnit(
            activity,
            SILVERMOB_SDK_AD_UNIT,
            EnumSet.of(AdUnitFormat.BANNER, AdUnitFormat.VIDEO),
            mediationUtils
        )
```
**Code for rewarded video integration:**
```kotlin
// 1. Get an instance of MaxRewardedAd
maxRewardedAd = MaxRewardedAd.getInstance(adUnitId, activity)
maxRewardedAd?.setListener(createListener())

// 2. Create MaxMediationRewardedUtils
val mediationUtils = MaxMediationRewardedUtils(maxRewardedAd)
    
// 3. Create MediationRewardedVideoAdUnit
adUnit = MediationRewardedVideoAdUnit(
            activity,
            SILVERMOB_SDK_AD_UNIT,
            mediationUtils
        )
        
// 4. Make a bid request
adUnit?.fetchDemand {

    // 5. Make an ad request to MAX
    maxRewardedAd?.loadAd()
}
```


### Setup SilverMob SDK adapter in your Applovin account

1. In your Applovin dashboard open [MAX > Manage > Networks](https://dash.applovin.com/o/mediation/networks/) and scroll to the bottom of the page. 
Select "Click here to add a Custom Network".
![networks list](https://files.silvermob.com/img/2024-02-02_14-37-57.png)


2. Fill out network data as shown on the screenshot.
![custom network settings](https://files.silvermob.com/img/2024-02-02_14-38-35.png)
Custom Network Name `SilverMob`, iOS Adapter Class Name `SilverMobMaxMediationAdapter`, Android Adapter Class Name `com.applovin.mediation.adapters.SilverMobMaxMediationAdapter`.
*Note: don't leave iOS adapter name empty or adapter integration might not work.*


3. Enable SilverMob network for your Ad Units: go to ad unit waterfall settings, scroll to **"Custom Networks"**, enable **SilverMob** and adjust settings accordingly. Wait around 60 minutes for Applovin to update Ad Unit and Network changes.
![ad unit settings](https://files.silvermob.com/img/2024-02-02_14-39-29.png)
*Note: mediation adapters don't work in Test Mode, be sure to disable it for testing mediation.*

## Acknowledgments

This project is based on a fork of [Prebid Mobile Android SDK](https://github.com/prebid/prebid-mobile-android), licensed under the Apache License 2.0. We are grateful to the original authors for their work and contributions to the open-source community.