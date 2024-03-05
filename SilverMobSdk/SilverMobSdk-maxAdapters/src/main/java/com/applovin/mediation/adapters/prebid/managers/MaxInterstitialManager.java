package com.applovin.mediation.adapters.prebid.managers;

import android.app.Activity;
import android.util.Log;

import com.applovin.mediation.adapter.MaxAdapterError;
import com.applovin.mediation.adapter.listeners.MaxInterstitialAdapterListener;
import com.applovin.mediation.adapter.listeners.MaxRewardedAdapterListener;
import com.applovin.mediation.adapter.parameters.MaxAdapterResponseParameters;
import com.applovin.mediation.adapters.prebid.ListenersCreator;
import com.applovin.mediation.adapters.prebid.ParametersChecker;
import com.silvermob.sdk.api.exceptions.AdException;
import com.silvermob.sdk.rendering.bidding.display.InterstitialController;
import com.silvermob.sdk.rendering.bidding.interfaces.InterstitialControllerListener;

import androidx.annotation.Nullable;

public class MaxInterstitialManager {

    private static final String TAG = MaxInterstitialManager.class.getSimpleName();

    private boolean isRewarded = false;

    @Nullable
    private Object maxListener;
    @Nullable
    private InterstitialController interstitialController;

    public void loadAd(
            MaxAdapterResponseParameters parameters,
            Activity activity,
            MaxInterstitialAdapterListener maxListener,
            String responseId
    ) {
        this.maxListener = maxListener;
        isRewarded = false;
        loadAd(parameters, activity,responseId);
    }

    public void loadAd(
            MaxAdapterResponseParameters parameters,
            Activity activity,
            MaxRewardedAdapterListener maxListener,
            String responseId
    ) {
        this.maxListener = maxListener;
        isRewarded = true;
        loadAd(parameters, activity,responseId);
    }

    private void loadAd(
            MaxAdapterResponseParameters parameters,
            Activity activity,
            String responseId
    ) {
        if (responseId == null) {
            return;
        }

        activity.runOnUiThread(() -> {
            try {
                InterstitialControllerListener listener = createPrebidListener();
                interstitialController = new InterstitialController(activity, listener);
                interstitialController.loadAd(responseId, isRewarded);
            } catch (AdException e) {
                String error = "Exception in Prebid interstitial controller (" + e.getMessage() + ")";
                Log.e(TAG, error);
                onError(1006, error);
            }
        });
    }

    public void showAd() {
        if (interstitialController == null) {
            MaxAdapterError error = new MaxAdapterError(2010, "InterstitialController is null");
            if (isRewarded) {
                MaxRewardedAdapterListener listener = getRewardedListener();
                if (listener != null) {
                    listener.onRewardedAdDisplayFailed(error);
                }
            } else {
                MaxInterstitialAdapterListener listener = getInterstitialListener();
                if (listener != null) {
                    listener.onInterstitialAdDisplayFailed(error);
                }
            }
            return;
        }
        interstitialController.show();
    }

    public void destroy() {
        if (interstitialController != null) {
            interstitialController.destroy();
        }
    }


    private InterstitialControllerListener createPrebidListener() {
        if (maxListener != null) {
            if (isRewarded) {
                return ListenersCreator.createRewardedListener(getRewardedListener());
            } else {
                return ListenersCreator.createInterstitialListener(getInterstitialListener());
            }
        } else {
            Log.e(TAG, "Max interstitial listener must be not null!");
        }
        return null;
    }

    private void onError(
            int code,
            String error
    ) {
        if (maxListener != null) {
            if (isRewarded) {
                getRewardedListener().onRewardedAdLoadFailed(new MaxAdapterError(code, error));
            } else {
                getInterstitialListener().onInterstitialAdLoadFailed(new MaxAdapterError(code, error));
            }
        } else Log.e(TAG, "Max interstitial listener must be not null!");
    }

    private MaxInterstitialAdapterListener getInterstitialListener() {
        return ((MaxInterstitialAdapterListener) maxListener);
    }

    private MaxRewardedAdapterListener getRewardedListener() {
        return ((MaxRewardedAdapterListener) maxListener);
    }

}
