package com.silvermob.sdk.rendering.models.openrtb.bidRequests.mapper;

import android.content.Context;
import android.view.View;

import com.silvermob.sdk.api.rendering.PrebidMobileInterstitialControllerInterface;
import com.silvermob.sdk.api.rendering.pluginrenderer.PluginEventListener;
import com.silvermob.sdk.api.rendering.pluginrenderer.PrebidMobilePluginRenderer;
import com.silvermob.sdk.configuration.AdUnitConfiguration;
import com.silvermob.sdk.rendering.bidding.data.bid.BidResponse;
import com.silvermob.sdk.rendering.bidding.interfaces.InterstitialControllerListener;
import com.silvermob.sdk.rendering.bidding.listeners.DisplayVideoListener;
import com.silvermob.sdk.rendering.bidding.listeners.DisplayViewListener;
import com.silvermob.sdk.rendering.models.openrtb.bidRequests.PluginRenderer;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static org.junit.Assert.assertEquals;

public class PluginRendererListMapperTest {

    String pluginName;
    String pluginVersion;
    JSONObject pluginData;

    PrebidMobilePluginRenderer testPlugin = new PrebidMobilePluginRenderer() {
        @Override
        public String getName() {
            return pluginName;
        }

        @Override
        public String getVersion() {
            return pluginVersion;
        }

        @Nullable
        @Override
        public JSONObject getData() {
            return pluginData;
        }

        @Override
        public void registerEventListener(PluginEventListener pluginEventListener, String listenerKey) { }

        @Override
        public void unregisterEventListener(String listenerKey) { }

        @Override
        public View createBannerAdView(@NonNull Context context, @NonNull DisplayViewListener displayViewListener, @Nullable DisplayVideoListener displayVideoListener, @NonNull AdUnitConfiguration adUnitConfiguration, @NonNull BidResponse bidResponse) {
            return null;
        }

        @Override
        public PrebidMobileInterstitialControllerInterface createInterstitialController(@NonNull Context context, @NonNull InterstitialControllerListener interstitialControllerListener, @NonNull AdUnitConfiguration adUnitConfiguration, @NonNull BidResponse bidResponse) {
            return null;
        }

        @Override
        public boolean isSupportRenderingFor(AdUnitConfiguration adUnitConfiguration) {
            return false;
        }
    };

    @Test
    public void whenMap_valuesAreCorrect() throws JSONException {
        // Given
        PluginRendererListMapper mapper = new PluginRendererListMapper();
        List<PrebidMobilePluginRenderer> pluginList = new ArrayList<>();
        pluginList.add(testPlugin);
        pluginName = "name";
        pluginVersion = "1.0";
        pluginData = new JSONObject();
        pluginData.put("key", true);

        // When
        List<PluginRenderer> result = mapper.map(pluginList);
        PluginRenderer mappedRenderer = result.get(0);

        // Then
        assertEquals(mappedRenderer.getName(), pluginName);
        assertEquals(mappedRenderer.getVersion(), pluginVersion);
        assertEquals(mappedRenderer.getData(), pluginData);
    }
}