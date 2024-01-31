# Save names for all Prebid classes
-keepnames class com.silvermob.sdk.**
-keepnames interface com.silvermob.sdk.**
-keepnames enum com.silvermob.sdk.**

# Google Ad Manager and AdMob
-keep class com.silvermob.sdk.PrebidNativeAd { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdView { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdRequest { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdRequest$Builder { *; }
-keep interface com.google.android.gms.ads.nativead.NativeCustomFormatAd { *; }
-keep interface com.google.android.gms.ads.formats.NativeCustomTemplateAd { *; }