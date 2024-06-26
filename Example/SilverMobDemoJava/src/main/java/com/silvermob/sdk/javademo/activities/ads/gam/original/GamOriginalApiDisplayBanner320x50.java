package com.silvermob.sdk.javademo.activities.ads.gam.original;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.silvermob.sdk.BannerAdUnit;
import com.silvermob.sdk.BannerParameters;
import com.silvermob.sdk.Signals;
import com.silvermob.sdk.addendum.AdViewUtils;
import com.silvermob.sdk.addendum.PbFindSizeError;
import com.silvermob.sdk.javademo.activities.BaseAdActivity;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GamOriginalApiDisplayBanner320x50 extends BaseAdActivity {

    private static final String AD_UNIT_ID = "/21808260008/prebid_demo_app_original_api_banner";
    private static final String CONFIG_ID = "prebid-demo-banner-320-50";
    private static final int WIDTH = 320;
    private static final int HEIGHT = 50;

    public BannerAdUnit adUnit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAd();
    }

    private void createAd() {
        adUnit = new BannerAdUnit(CONFIG_ID, WIDTH, HEIGHT);

        BannerParameters parameters = new BannerParameters();
        parameters.setApi(Collections.singletonList(Signals.Api.MRAID_2));
        adUnit.setBannerParameters(parameters);

        /* For GAM less than version 20 use PublisherAdView */
        final AdManagerAdView gamView = new AdManagerAdView(this);
        gamView.setAdUnitId(AD_UNIT_ID);
        gamView.setAdSizes(new com.google.android.gms.ads.AdSize(WIDTH, HEIGHT));

        getAdWrapperView().addView(gamView);

        gamView.setAdListener(createListener(gamView));

        final AdManagerAdRequest.Builder builder = new AdManagerAdRequest.Builder();
        adUnit.setAutoRefreshInterval(getRefreshTimeSeconds());
        adUnit.fetchDemand(builder, resultCode -> {
            /* For GAM less than version 20 use PublisherAdRequest */
            AdManagerAdRequest request = builder.build();
            gamView.loadAd(request);
        });
    }

    private AdListener createListener(AdManagerAdView gamView) {
        return new AdListener() {
            @Override
            public void onAdLoaded() {
                AdViewUtils.findPrebidCreativeSize(gamView, new AdViewUtils.PbFindSizeListener() {
                    @Override
                    public void success(
                        int width,
                        int height
                    ) {
                        gamView.setAdSizes(new AdSize(width, height));
                    }

                    @Override
                    public void failure(@NonNull PbFindSizeError error) {
                    }
                });
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adUnit != null) {
            adUnit.stopAutoRefresh();
        }
    }
}
