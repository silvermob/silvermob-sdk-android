package com.applovin.mediation.adapters;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.adapter.MaxAdViewAdapter;
import com.applovin.mediation.adapter.MaxInterstitialAdapter;
import com.applovin.mediation.adapter.MaxNativeAdAdapter;
import com.applovin.mediation.adapter.MaxRewardedAdapter;
import com.applovin.mediation.adapter.listeners.MaxAdViewAdapterListener;
import com.applovin.mediation.adapter.listeners.MaxInterstitialAdapterListener;
import com.applovin.mediation.adapter.listeners.MaxNativeAdAdapterListener;
import com.applovin.mediation.adapter.listeners.MaxRewardedAdapterListener;
import com.applovin.mediation.adapter.parameters.MaxAdapterInitializationParameters;
import com.applovin.mediation.adapter.parameters.MaxAdapterResponseParameters;
import com.applovin.mediation.adapters.prebid.managers.MaxBannerManager;
import com.applovin.mediation.adapters.prebid.managers.MaxInterstitialManager;
import com.applovin.mediation.adapters.prebid.managers.MaxNativeManager;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkUtils;
import com.silvermob.sdk.SilverMob;
import com.silvermob.sdk.TargetingParams;
import com.silvermob.sdk.api.data.AdUnitFormat;
import com.silvermob.sdk.api.data.InitializationStatus;
import com.silvermob.sdk.api.mediation.MediationBannerAdUnit;
import com.silvermob.sdk.AdSize;
import com.silvermob.sdk.api.mediation.MediationInterstitialAdUnit;
import com.silvermob.sdk.api.mediation.MediationRewardedVideoAdUnit;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.display.PrebidMediationDelegate;

import java.util.EnumSet;
import java.util.HashMap;

import androidx.annotation.Nullable;

public class SilverMobMaxMediationAdapter extends MediationAdapterBase implements MaxAdViewAdapter, MaxInterstitialAdapter, MaxRewardedAdapter, MaxNativeAdAdapter {

    public static final String TAG = SilverMobMaxMediationAdapter.class.getSimpleName();
    public static final String EXTRA_RESPONSE_ID = TAG + "ExtraResponseId";
    public static final String EXTRA_KEYWORDS_ID = TAG + "ExtraKeywordsId";

    private MaxBannerManager maxBannerManager;
    private MaxInterstitialManager maxInterstitialManager;
    private MaxNativeManager maxNativeManager;
    private String responseId;
    private MediationBannerAdUnit bannerAdUnit;
    private MediationInterstitialAdUnit interstitialAdUnit;
    private MediationRewardedVideoAdUnit rewardedVideoAdUnit;

    public SilverMobMaxMediationAdapter(AppLovinSdk appLovinSdk) {
        super(appLovinSdk);
    }

    @Override
    public void initialize(
            MaxAdapterInitializationParameters parameters,
            Activity activity,
            OnCompletionListener onCompletionListener
    ) {
        if (SilverMob.isSdkInitialized()) {
            onCompletionListener.onCompletion(InitializationStatus.INITIALIZED_SUCCESS, null);
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = () -> {
                if (activity == null) {
                    return;
                }

                //String accountId = parameters.getServerParameters().getString( "app_id", "default" );
                //SilverMob.setServerAccountId(accountId);
                if(SilverMob.getPrebidServerAccountId() == null || TextUtils.isEmpty(SilverMob.getPrebidServerAccountId())){
                    SilverMob.setServerAccountId("default");
                }

                SilverMob.initializeSdk(activity.getApplicationContext(), status -> {
                    if (onCompletionListener != null) {
                        if (status == com.silvermob.sdk.api.data.InitializationStatus.SUCCEEDED) {
                            onCompletionListener.onCompletion(InitializationStatus.INITIALIZED_SUCCESS, null);
                        } else {//status FAILED or status SERVER_STATUS_WARNING
                            onCompletionListener.onCompletion(InitializationStatus.INITIALIZED_FAILURE, status.getDescription());
                        }
                    }
                });
            };
            handler.post(runnable);

            onCompletionListener.onCompletion(InitializationStatus.INITIALIZING, null);
        }
        setConsents(parameters);
    }

    private double extractBidFloor(MaxAdapterResponseParameters parameters){
        double bidFloor = parameters.getCustomParameters().getDouble("floor_price",-1);
        if(bidFloor!= -1) return bidFloor;
        bidFloor = parameters.getCustomParameters().getInt("floor_price",-1);
        return bidFloor;
    }

    @Override
    public void loadAdViewAd(
            MaxAdapterResponseParameters parameters,
            MaxAdFormat maxAdFormat,
            Activity activity,
            MaxAdViewAdapterListener listener
    ) {
        PrebidMediationDelegate mediationUtils = new PrebidMediationDelegate() {
            @Override
            public void handleKeywordsUpdate(@Nullable HashMap<String, String> keywords) {}
            @Override
            public void setResponseToLocalExtras(@Nullable BidResponse response) {
                responseId = response.getId();
            }
            @Override
            public boolean canPerformRefresh() {return true;}
        };

        AppLovinSdkUtils.Size size = maxAdFormat.getSize();
        bannerAdUnit = new MediationBannerAdUnit(
                activity,
                parameters.getThirdPartyAdPlacementId(),
                new AdSize(size.getWidth(), size.getHeight()),
                mediationUtils
        );

        double bidFloor = extractBidFloor(parameters);
        if(bidFloor!= -1){
            bannerAdUnit.getConfiguration().setBidFloor(bidFloor);
        }

        bannerAdUnit.fetchDemand(result -> {
            maxBannerManager = new MaxBannerManager();
            maxBannerManager.loadAd(parameters, maxAdFormat, activity, listener,responseId);
        });
    }


    @Override
    public void loadInterstitialAd(
            MaxAdapterResponseParameters parameters,
            Activity activity,
            MaxInterstitialAdapterListener maxListener
    ) {
        PrebidMediationDelegate mediationUtils = new PrebidMediationDelegate() {
            @Override
            public void handleKeywordsUpdate(@Nullable HashMap<String, String> keywords) {}
            @Override
            public void setResponseToLocalExtras(@Nullable BidResponse response) {
                responseId = response.getId();
            }
            @Override
            public boolean canPerformRefresh() {return true;}
        };
        EnumSet<AdUnitFormat> adUnitFormats = EnumSet.of(AdUnitFormat.BANNER, AdUnitFormat.VIDEO);
        interstitialAdUnit = new MediationInterstitialAdUnit(
                activity,
                parameters.getThirdPartyAdPlacementId(),
                adUnitFormats,
                mediationUtils
        );

        double bidFloor = extractBidFloor(parameters);
        if(bidFloor!= -1){
            interstitialAdUnit.getConfiguration().setBidFloor(bidFloor);
        }

        interstitialAdUnit.setMinSizePercentage(1,1);
        interstitialAdUnit.fetchDemand(result -> {
            maxInterstitialManager = new MaxInterstitialManager();
            maxInterstitialManager.loadAd(parameters, activity, maxListener,responseId);
        });
    }

    @Override
    public void loadRewardedAd(
            MaxAdapterResponseParameters parameters,
            Activity activity,
            MaxRewardedAdapterListener maxListener
    ) {
        PrebidMediationDelegate mediationUtils = new PrebidMediationDelegate() {
            @Override
            public void handleKeywordsUpdate(@Nullable HashMap<String, String> keywords) {}

            @Override
            public void setResponseToLocalExtras(@Nullable BidResponse response) {
                responseId = response.getId();
            }

            @Override
            public boolean canPerformRefresh() {return true;}
        };
        rewardedVideoAdUnit = new MediationRewardedVideoAdUnit(
                activity,
                parameters.getThirdPartyAdPlacementId(),
                mediationUtils
        );

        double bidFloor = extractBidFloor(parameters);
        if(bidFloor!= -1){
            rewardedVideoAdUnit.getConfiguration().setBidFloor(bidFloor);
        }

        rewardedVideoAdUnit.fetchDemand (result -> {
            maxInterstitialManager = new MaxInterstitialManager();
            maxInterstitialManager.loadAd(parameters, activity, maxListener,responseId);
        });
    }

    @Override
    public void showInterstitialAd(
            MaxAdapterResponseParameters parameters,
            Activity activity,
            MaxInterstitialAdapterListener maxListener
    ) {

        maxInterstitialManager.showAd(activity);
    }


    @Override
    public void showRewardedAd(
            MaxAdapterResponseParameters parameters,
            Activity activity,
            MaxRewardedAdapterListener maxListener
    ) {
        maxInterstitialManager.showAd(activity);
    }


    @Override
    public void loadNativeAd(
            MaxAdapterResponseParameters parameters,
            Activity activity,
            MaxNativeAdAdapterListener maxListener
    ) {
        maxNativeManager = new MaxNativeManager();
        maxNativeManager.loadAd(parameters, activity, maxListener);
    }


    @Override
    public void onDestroy() {
       if (maxBannerManager != null) {
            maxBannerManager.destroy();
        }

        if (maxInterstitialManager != null) {
            maxInterstitialManager.destroy();
        }

        if (maxNativeManager != null) {
            maxNativeManager.destroy();
        }
        if(bannerAdUnit != null) {
            bannerAdUnit.destroy();
        }
        if(interstitialAdUnit != null) {
            interstitialAdUnit.destroy();
        }

        if(rewardedVideoAdUnit != null) {
            rewardedVideoAdUnit.destroy();
        }
    }

    @Override
    public String getAdapterVersion() {
        return SilverMob.SDK_VERSION;
    }

    @Override
    public String getSdkVersion() {
        return SilverMob.SDK_VERSION;
    }


    private void setConsents(MaxAdapterInitializationParameters parameters) {
        if (parameters != null) {
            Boolean ageRestrictedUser = parameters.isAgeRestrictedUser();
            if (ageRestrictedUser != null) {
                TargetingParams.setSubjectToCOPPA(ageRestrictedUser);
            }
        }
    }

}
