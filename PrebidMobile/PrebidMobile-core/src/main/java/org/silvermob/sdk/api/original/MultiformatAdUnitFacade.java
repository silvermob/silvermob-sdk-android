package org.silvermob.sdk.api.original;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.silvermob.sdk.AdSize;
import org.silvermob.sdk.AdUnit;
import org.silvermob.sdk.BannerParameters;
import org.silvermob.sdk.ContentObject;
import org.silvermob.sdk.DataObject;
import org.silvermob.sdk.NativeParameters;
import org.silvermob.sdk.OnCompleteListener;
import org.silvermob.sdk.ResultCode;
import org.silvermob.sdk.Util;
import org.silvermob.sdk.VideoParameters;
import org.silvermob.sdk.api.data.AdFormat;
import org.silvermob.sdk.api.exceptions.AdException;
import org.silvermob.sdk.configuration.AdUnitConfiguration;
import org.silvermob.sdk.configuration.NativeAdUnitConfiguration;
import org.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import org.silvermob.sdk.rendering.bidding.listeners.BidRequesterListener;
import org.silvermob.sdk.rendering.models.AdPosition;
import org.silvermob.sdk.rendering.models.PlacementType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Internal AdUnit implementation that is used for PrebidAdUnit
 * with multiformat configuration. It separates logic for multiformat ad unit.
 */
class MultiformatAdUnitFacade extends AdUnit {

    @Nullable
    private BidResponse bidResponse;

    public MultiformatAdUnitFacade(@NotNull String configId, @NonNull PrebidRequest request) {
        super(configId);
        allowNullableAdObject = true;
        setConfigurationBasedOnRequest(request);
    }

    @Override
    protected BidRequesterListener createBidListener(OnCompleteListener originalListener) {
        return new BidRequesterListener() {
            @Override
            public void onFetchCompleted(BidResponse response) {
                bidResponse = response;

                HashMap<String, String> keywords = response.getTargeting();
                Util.apply(keywords, adObject);

                originalListener.onComplete(ResultCode.SUCCESS);
            }

            @Override
            public void onError(AdException exception) {
                bidResponse = null;

                Util.apply(null, adObject);

                originalListener.onComplete(convertToResultCode(exception));
            }
        };
    }

    private void setConfigurationBasedOnRequest(
            @NonNull PrebidRequest request
    ) {
        if (request.isInterstitial()) {
            configuration.setAdPosition(AdPosition.FULLSCREEN);
        }

        BannerParameters bannerParameters = request.getBannerParameters();
        if (bannerParameters != null) {
            if (request.isInterstitial()) {
                configuration.addAdFormat(AdFormat.INTERSTITIAL);

                Integer minWidth = bannerParameters.getInterstitialMinWidthPercentage();
                Integer minHeight = bannerParameters.getInterstitialMinHeightPercentage();
                if (minWidth != null && minHeight != null) {
                    configuration.setMinSizePercentage(new AdSize(minWidth, minHeight));
                }
            } else {
                configuration.addAdFormat(AdFormat.BANNER);
            }

            configuration.setBannerParameters(bannerParameters);
            configuration.addSizes(bannerParameters.getAdSizes());
        }

        VideoParameters videoParameters = request.getVideoParameters();
        if (videoParameters != null) {
            configuration.addAdFormat(AdFormat.VAST);

            if (request.isInterstitial()) {
                configuration.setPlacementType(PlacementType.INTERSTITIAL);
            }
            if (request.isRewarded()) {
                configuration.setRewarded(true);
            }

            configuration.setVideoParameters(videoParameters);
            configuration.addSize(videoParameters.getAdSize());
        }

        NativeParameters nativeParameters = request.getNativeParameters();
        if (nativeParameters != null) {
            configuration.addAdFormat(AdFormat.NATIVE);

            NativeAdUnitConfiguration nativeConfig = nativeParameters.getNativeConfiguration();
            configuration.setNativeConfiguration(nativeConfig);
        }

        String gpid = request.getGpid();
        configuration.setGpid(gpid);

        ContentObject contentObject = request.getAppContent();
        configuration.setAppContent(contentObject);

        ArrayList<DataObject> userData = request.getUserData();
        configuration.setUserData(userData);

        Map<String, Set<String>> extData = request.getExtData();
        configuration.setExtData(extData);

        Set<String> extKeywords = request.getExtKeywords();
        configuration.setExtKeywords(extKeywords);
    }

    @Nullable
    public BidResponse getBidResponse() {
        return bidResponse;
    }

    @SuppressLint("VisibleForTests")
    @Override
    public AdUnitConfiguration getConfiguration() {
        return super.getConfiguration();
    }

}