package org.silvermob.mobile.api.original;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.silvermob.mobile.AdSize;
import org.silvermob.mobile.AdUnit;
import org.silvermob.mobile.BannerParameters;
import org.silvermob.mobile.ContentObject;
import org.silvermob.mobile.DataObject;
import org.silvermob.mobile.NativeParameters;
import org.silvermob.mobile.OnCompleteListener;
import org.silvermob.mobile.ResultCode;
import org.silvermob.mobile.Util;
import org.silvermob.mobile.VideoParameters;
import org.silvermob.mobile.api.data.AdFormat;
import org.silvermob.mobile.api.exceptions.AdException;
import org.silvermob.mobile.configuration.AdUnitConfiguration;
import org.silvermob.mobile.configuration.NativeAdUnitConfiguration;
import org.silvermob.mobile.rendering.bidding.data.bid.BidResponse;
import org.silvermob.mobile.rendering.bidding.listeners.BidRequesterListener;
import org.silvermob.mobile.rendering.models.AdPosition;
import org.silvermob.mobile.rendering.models.PlacementType;

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