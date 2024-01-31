# Save names for all Prebid classes
-keepnames class org.silvermob.sdk.**
-keepnames interface org.silvermob.sdk.**
-keepnames enum org.silvermob.sdk.**

# Google Ad Manager and AdMob
-keep class org.silvermob.sdk.PrebidNativeAd { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdView { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdRequest { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdRequest$Builder { *; }
-keep interface com.google.android.gms.ads.nativead.NativeCustomFormatAd { *; }
-keep interface com.google.android.gms.ads.formats.NativeCustomTemplateAd { *; }