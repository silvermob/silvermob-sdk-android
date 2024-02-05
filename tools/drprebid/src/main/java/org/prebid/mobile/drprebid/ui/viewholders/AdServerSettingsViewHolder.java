package com.silvermob.sdk.drprebid.ui.viewholders;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.silvermob.sdk.drprebid.Constants;
import com.silvermob.sdk.drprebid.R;
import com.silvermob.sdk.drprebid.managers.SettingsManager;
import com.silvermob.sdk.drprebid.model.AdServer;
import com.silvermob.sdk.drprebid.model.AdServerSettings;
import com.silvermob.sdk.drprebid.model.HelpScreen;
import com.silvermob.sdk.drprebid.ui.activities.InfoActivity;
import com.silvermob.sdk.drprebid.ui.dialog.InputDialog;
import com.silvermob.sdk.drprebid.ui.viewmodels.SettingsViewModel;
import com.silvermob.sdk.drprebid.util.HelpScreenUtil;

import java.util.Locale;

public class AdServerSettingsViewHolder extends RecyclerView.ViewHolder implements SettingsViewHolder, LifecycleOwner {

    private RadioGroup adServerGroup;
    private TextView adUnitIdView;
    private TextView bidPriceView;
    private SettingsViewModel settingsViewModel;

    public AdServerSettingsViewHolder(@NonNull final View itemView) {
        super(itemView);

        itemView.findViewById(R.id.button_info).setOnClickListener(v -> {
            HelpScreen aboutScreen = HelpScreenUtil.getAdServerInfo(itemView.getContext());
            Intent intent = InfoActivity.newIntent(
                    itemView.getContext(),
                    aboutScreen.getTitle(),
                    aboutScreen.getHtmlAsset()
            );
            itemView.getContext().startActivity(intent);
        });

        bidPriceView = itemView.findViewById(R.id.view_bid_price);
        bidPriceView.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
            InputDialog dialog = InputDialog.newInstance(itemView.getContext().getString(R.string.bid_price),
                    Constants.Params.TYPE_BID_PRICE,
                    Constants.Params.FORMAT_FLOAT,
                    false
            );
            dialog.show(fragmentManager, InputDialog.TAG);
        });

        adUnitIdView = itemView.findViewById(R.id.view_ad_unit_id);
        adUnitIdView.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
            InputDialog dialog = InputDialog.newInstance(itemView.getContext().getString(R.string.ad_unit_id),
                    Constants.Params.TYPE_AD_UNIT_ID,
                    Constants.Params.FORMAT_TEXT,
                    true
            );
            dialog.show(fragmentManager, InputDialog.TAG);
        });

        adServerGroup = itemView.findViewById(R.id.group_ad_server);
        adServerGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_dfp) {
                SettingsManager.getInstance(itemView.getContext()).setAdServer(AdServer.GOOGLE_AD_MANAGER);
            }
        });

        settingsViewModel = ViewModelProviders.of((AppCompatActivity) itemView.getContext())
                                              .get(SettingsViewModel.class);

        settingsViewModel.getBidPrice().observe(this, bidPrice -> {
            if (bidPrice != null) {
                bidPriceView.setText(String.format(Locale.ENGLISH, "$ %.2f", bidPrice));
            }
        });

        settingsViewModel.getAdUnitId().observe(this, adUnitId -> {
            if (!TextUtils.isEmpty(adUnitId)) {
                adUnitIdView.setText(adUnitId);
            } else {
                adUnitIdView.setText(R.string.click_to_choose);
            }
        });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return ((AppCompatActivity) itemView.getContext()).getLifecycle();
    }

    @Override
    public void bind() {
        fillValues();
    }

    private void fillValues() {
        AdServerSettings settings = SettingsManager.getInstance(itemView.getContext()).getAdServerSettings();

        adServerGroup.check(R.id.radio_dfp);


        bidPriceView.setText(String.format(Locale.ENGLISH, "$ %.2f", settings.getBidPrice()));

        if (!TextUtils.isEmpty(settings.getAdUnitId())) {
            adUnitIdView.setText(settings.getAdUnitId());
        }
    }
}
