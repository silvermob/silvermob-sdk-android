package com.silvermob.sdk.javademo.activities.ads.gam.original;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.silvermob.sdk.BannerParameters;
import com.silvermob.sdk.NativeAdUnit;
import com.silvermob.sdk.NativeAsset;
import com.silvermob.sdk.NativeDataAsset;
import com.silvermob.sdk.NativeEventTracker;
import com.silvermob.sdk.NativeImageAsset;
import com.silvermob.sdk.NativeParameters;
import com.silvermob.sdk.NativeTitleAsset;
import com.silvermob.sdk.VideoParameters;
import com.silvermob.sdk.addendum.AdViewUtils;
import com.silvermob.sdk.addendum.PbFindSizeError;
import com.silvermob.sdk.api.original.PrebidAdUnit;
import com.silvermob.sdk.api.original.PrebidRequest;
import com.silvermob.sdk.javademo.activities.BaseAdActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GamOriginalApiMultiformatBannerVideoNativeStyles extends BaseAdActivity {

    private final String AD_UNIT_ID = "/21808260008/prebid-demo-multiformat-native-styles";
    private final String CONFIG_ID_BANNER = "prebid-demo-banner-300-250";
    private final String CONFIG_ID_NATIVE = "prebid-demo-banner-native-styles";
    private final String CONFIG_ID_VIDEO = "prebid-demo-video-outstream-original-api";

    private PrebidAdUnit prebidAdUnit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAd();
    }

    private void createAd() {
        // Random only for test cases. For production use one config id.
        ArrayList<String> configIds = Lists.newArrayList(CONFIG_ID_BANNER, CONFIG_ID_VIDEO, CONFIG_ID_NATIVE);
        String configId = configIds.get((new Random().nextInt(3)));

        // 1. Create PrebidAdUnit with configId
        prebidAdUnit = new PrebidAdUnit(configId);

        // 2. Create PrebidRequest with needed multiformat parameters
        PrebidRequest prebidRequest = new PrebidRequest();
        prebidRequest.setBannerParameters(createBannerParameters());
        prebidRequest.setVideoParameters(createVideoParameters());
        prebidRequest.setNativeParameters(creativeNativeParameters());

        // 3. Make a bid request to Prebid Server
        AdManagerAdRequest gamRequest = new AdManagerAdRequest.Builder().build();
        prebidAdUnit.fetchDemand(gamRequest, prebidRequest, bidInfo -> {
            loadGam(gamRequest);
        });
    }

    private void loadGam(AdManagerAdRequest gamRequest) {
        // 4. Load GAM ad
        AdManagerAdView gamView = new AdManagerAdView(this);
        gamView.setAdUnitId(AD_UNIT_ID);
        gamView.setAdSizes(AdSize.FLUID, AdSize.BANNER, AdSize.MEDIUM_RECTANGLE);
        gamView.setAdListener(createListener(gamView));
        gamView.loadAd(gamRequest);
        binding.frameAdWrapper.addView(gamView);
    }

    private static AdListener createListener(AdManagerAdView gamView) {
        return new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // 5. Update ad view
                AdViewUtils.findPrebidCreativeSize(gamView, new AdViewUtils.PbFindSizeListener() {
                    @Override
                    public void success(int width, int height) {
                        gamView.setAdSizes(new AdSize(width, height));
                    }

                    @Override
                    public void failure(@NonNull @NotNull PbFindSizeError error) {
                    }
                });
            }
        };
    }

    private BannerParameters createBannerParameters() {
        BannerParameters parameters = new BannerParameters();
        parameters.setAdSizes(Sets.newHashSet(new com.silvermob.sdk.AdSize(300, 250)));
        return parameters;
    }

    private VideoParameters createVideoParameters() {
        VideoParameters parameters = new VideoParameters(Lists.newArrayList("video/mp4"));
        parameters.setAdSize(new com.silvermob.sdk.AdSize(320, 480));
        return parameters;
    }

    private NativeParameters creativeNativeParameters() {
        ArrayList<NativeAsset> nativeAssets = Lists.newArrayList();

        NativeTitleAsset title = new NativeTitleAsset();
        title.setLength(90);
        title.setRequired(true);
        nativeAssets.add(title);

        NativeImageAsset icon = new NativeImageAsset(20, 20, 20, 20);
        icon.setImageType(NativeImageAsset.IMAGE_TYPE.ICON);
        icon.setRequired(true);
        nativeAssets.add(icon);

        NativeImageAsset image = new NativeImageAsset(200, 200, 200, 200);
        image.setImageType(NativeImageAsset.IMAGE_TYPE.MAIN);
        image.setRequired(true);
        nativeAssets.add(image);

        NativeDataAsset data = new NativeDataAsset();
        data.setLen(90);
        data.setDataType(NativeDataAsset.DATA_TYPE.SPONSORED);
        data.setRequired(true);
        nativeAssets.add(data);

        NativeDataAsset body = new NativeDataAsset();
        body.setRequired(true);
        body.setDataType(NativeDataAsset.DATA_TYPE.DESC);
        nativeAssets.add(body);

        NativeDataAsset cta = new NativeDataAsset();
        cta.setRequired(true);
        cta.setDataType(NativeDataAsset.DATA_TYPE.CTATEXT);
        nativeAssets.add(cta);

        NativeParameters nativeParameters = new NativeParameters(nativeAssets);

        ArrayList<NativeEventTracker.EVENT_TRACKING_METHOD> methods = new ArrayList<>();
        methods.add(NativeEventTracker.EVENT_TRACKING_METHOD.IMAGE);
        methods.add(NativeEventTracker.EVENT_TRACKING_METHOD.JS);
        NativeEventTracker tracker = new NativeEventTracker(NativeEventTracker.EVENT_TYPE.IMPRESSION, methods);
        nativeParameters.addEventTracker(tracker);

        nativeParameters.setContextType(NativeAdUnit.CONTEXT_TYPE.SOCIAL_CENTRIC);
        nativeParameters.setPlacementType(NativeAdUnit.PLACEMENTTYPE.CONTENT_FEED);
        nativeParameters.setContextSubType(NativeAdUnit.CONTEXTSUBTYPE.GENERAL_SOCIAL);
        return nativeParameters;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (prebidAdUnit != null) {
            prebidAdUnit.destroy();
        }
    }

}
