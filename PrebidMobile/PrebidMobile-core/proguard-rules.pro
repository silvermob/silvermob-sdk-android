# Save names for all Prebid classes
-keepnames class org.silvermob.mobile.**
-keepnames interface org.silvermob.mobile.**
-keepnames enum org.silvermob.mobile.**

# Google Ad Manager and AdMob
-keep class org.silvermob.mobile.PrebidNativeAd { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdView { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdRequest { *; }
-keep class com.google.android.gms.ads.admanager.AdManagerAdRequest$Builder { *; }
-keep interface com.google.android.gms.ads.nativead.NativeCustomFormatAd { *; }
-keep interface com.google.android.gms.ads.formats.NativeCustomTemplateAd { *; }