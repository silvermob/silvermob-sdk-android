package com.applovin.mediation.adapters.prebid.managers;

import android.app.Activity;
import android.util.Log;

import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.adapter.MaxAdapterError;
import com.applovin.mediation.adapter.listeners.MaxAdViewAdapterListener;
import com.applovin.mediation.adapter.parameters.MaxAdapterResponseParameters;
import com.applovin.mediation.adapters.prebid.ListenersCreator;
import com.applovin.mediation.adapters.prebid.ParametersChecker;
import com.silvermob.sdk.LogUtil;
import com.silvermob.sdk.api.data.AdFormat;
import com.silvermob.sdk.api.rendering.DisplayView;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.listeners.DisplayViewListener;

import androidx.annotation.Nullable;

public class MaxBannerManager {

    private static final String TAG = MaxBannerManager.class.getSimpleName();

    @Nullable
    private DisplayView adView;
    @Nullable
    private MaxAdViewAdapterListener maxListener;

    public void loadAd(
            MaxAdapterResponseParameters parameters,
            MaxAdFormat maxAdFormat,
            Activity activity,
            MaxAdViewAdapterListener listener,
            String responseId
    ) {
        maxListener = listener;
        BidResponse bidResponse = ParametersChecker.getBidResponse(responseId, this::onError);
        if (bidResponse == null) {
            return;
        }

        switch (maxAdFormat.getLabel()) {
            case "BANNER":
            case "MREC":
            case "LEADER":
                showBanner(activity, parameters, bidResponse);
                break;
            default:
                String error = "Unknown type of MAX ad!";
                Log.e(TAG, error);
                onError(1005, error);
        }
    }
    public void loadAd(
            MaxAdapterResponseParameters parameters,
            MaxAdFormat maxAdFormat,
            Activity activity,
            MaxAdViewAdapterListener listener
    ) {
        String responseId = ParametersChecker.getResponseIdAndCheckKeywords(parameters, this::onError);
        loadAd(parameters,maxAdFormat,activity,listener,responseId);
    }

    public void destroy() {
        if (adView != null) {
            adView.destroy();
        }
        adView = null;
    }


    private void showBanner(
            Activity activity,
            MaxAdapterResponseParameters parameters,
            BidResponse response
    ) {
        AdUnitConfiguration adConfiguration = new AdUnitConfiguration();
        adConfiguration.setAdFormat(AdFormat.BANNER);
        DisplayViewListener listener = ListenersCreator.createBannerListener(
                maxListener,
                () -> {
                    if (maxListener != null) {
                        maxListener.onAdViewAdLoaded(adView);
                    }
                }
        );

        if (activity != null) {
            LogUtil.info(TAG, "Prebid ad won: " + parameters.getThirdPartyAdPlacementId());
            activity.runOnUiThread(() -> {
                adView = new DisplayView(activity, listener, adConfiguration, response);
            });
        } else {
            onError(1005, "Activity is null");
        }
    }

    private void onError(
            int code,
            String error
    ) {
        if (maxListener != null) {
            maxListener.onAdViewAdLoadFailed(new MaxAdapterError(code, error));
        } else {
            Log.e(TAG, "Max banner listener is null: " + error);
        }
    }

}
