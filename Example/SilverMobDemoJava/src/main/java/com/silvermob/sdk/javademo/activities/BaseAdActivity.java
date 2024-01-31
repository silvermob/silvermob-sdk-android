package com.silvermob.sdk.javademo.activities;

import android.os.Bundle;
import android.view.ViewGroup;

import com.silvermob.sdk.javademo.testcases.TestCase;
import com.silvermob.sdk.javademo.utils.Settings;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.silvermob.sdk.javademo.R;
import com.silvermob.sdk.javademo.databinding.ActivityDemoBinding;
import com.silvermob.sdk.javademo.testcases.TestCase;
import com.silvermob.sdk.javademo.testcases.TestCaseRepository;
import com.silvermob.sdk.javademo.utils.Settings;

public class BaseAdActivity extends AppCompatActivity {

    protected ActivityDemoBinding binding;

    private TestCase testCase = TestCaseRepository.lastTestCase;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_demo);
        binding.tvTestCaseName.setText(getString(testCase.getTitleStringRes()));
    }

    protected ViewGroup getAdWrapperView() {
        return binding.frameAdWrapper;
    }

    protected int getRefreshTimeSeconds() {
        return Settings.get().getRefreshTimeSeconds();
    }

}
