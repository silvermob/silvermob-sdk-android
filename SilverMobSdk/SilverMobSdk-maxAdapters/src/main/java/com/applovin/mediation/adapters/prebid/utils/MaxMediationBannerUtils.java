package com.applovin.mediation.adapters.prebid.utils;

import android.util.Log;

import com.applovin.mediation.adapters.SilverMobMaxMediationAdapter;
import com.applovin.mediation.ads.MaxAdView;
import com.silvermob.sdk.LogUtil;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.display.PrebidMediationDelegate;
import com.silvermob.sdk.rendering.models.internal.VisibilityTrackerOption;
import com.silvermob.sdk.rendering.models.ntv.NativeEventTracker;
import com.silvermob.sdk.rendering.utils.helpers.VisibilityChecker;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import androidx.annotation.Nullable;

public class MaxMediationBannerUtils implements PrebidMediationDelegate {

    private static final String TAG = MaxMediationBannerUtils.class.getSimpleName();

    private final WeakReference<MaxAdView> adViewReference;

    public MaxMediationBannerUtils(MaxAdView adView) {
        adViewReference = new WeakReference<>(adView);
    }

    @Override
    public void setResponseToLocalExtras(@Nullable BidResponse response) {
        if (adViewReference.get() != null) {
            String responseId; if (response != null) {
                responseId = response.getId();
            } else {
                responseId = null;
            } adViewReference.get().setLocalExtraParameter(SilverMobMaxMediationAdapter.EXTRA_RESPONSE_ID, responseId);
        }
    }

    @Override
    public boolean canPerformRefresh() {
        MaxAdView view = adViewReference.get();
        if (view == null) {
            LogUtil.error(TAG, "AdView is null, it can be destroyed as WeakReference");
            return false;
        }

        final VisibilityTrackerOption visibilityTrackerOption = new VisibilityTrackerOption(NativeEventTracker.EventType.IMPRESSION);
        final VisibilityChecker checker = new VisibilityChecker(visibilityTrackerOption);

        boolean isVisible = checker.isVisibleForRefresh(view);
        if (isVisible) {
            Log.d(TAG, "Visibility checker result: " + true);
        } else {
            Log.e(TAG, "Can't perform refresh. Ad view is not visible.");
        }
        return true;
    }

    @Override
    public void handleKeywordsUpdate(@Nullable HashMap<String, String> keywords) {}

}
