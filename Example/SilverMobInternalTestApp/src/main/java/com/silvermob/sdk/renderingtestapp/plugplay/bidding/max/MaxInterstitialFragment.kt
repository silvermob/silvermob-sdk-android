package com.silvermob.sdk.renderingtestapp.plugplay.bidding.max

import android.os.Bundle
import android.util.Log
import android.view.View
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.adapters.prebid.utils.MaxMediationInterstitialUtils
import com.applovin.mediation.ads.MaxInterstitialAd
import com.silvermob.sdk.api.data.AdUnitFormat
import com.silvermob.sdk.api.mediation.MediationInterstitialAdUnit
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingInterstitialApplovinMaxBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents
import java.util.*

open class MaxInterstitialFragment : AdFragment() {

    companion object {
        private const val TAG = "MaxInterstitialFragment"

        public const val ARG_IS_VIDEO = TAG + "IsVideo"
    }

    protected var maxInterstitialAd: MaxInterstitialAd? = null
    protected var adUnit: com.silvermob.sdk.api.mediation.MediationInterstitialAdUnit? = null
    protected var isVideo = false

    override val layoutRes = R.layout.fragment_bidding_interstitial_applovin_max

    override fun configuratorMode() = AdConfiguratorDialogFragment.AdConfiguratorMode.INTERSTITIAL

    private val binding: FragmentBiddingInterstitialApplovinMaxBinding
        get() = getBinding()
    private lateinit var events: Events

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)
        events = Events(view)
        binding.adIdLabel.text = getString(R.string.label_auid, configId)
        binding.btnLoad.setOnClickListener {
            handleLoadButtonClick()
        }
    }

    override fun initAd(): Any? {
        isVideo = arguments?.getBoolean(ARG_IS_VIDEO) ?: false

        maxInterstitialAd = MaxInterstitialAd(adUnitId, activity)
        maxInterstitialAd?.setListener(createListener())

        val mediationUtils = MaxMediationInterstitialUtils(maxInterstitialAd)
        var adUnitFormats = EnumSet.of(com.silvermob.sdk.api.data.AdUnitFormat.DISPLAY)
        if (isVideo) {
            adUnitFormats = EnumSet.of(com.silvermob.sdk.api.data.AdUnitFormat.VIDEO)
        }
        adUnit = com.silvermob.sdk.api.mediation.MediationInterstitialAdUnit(
                activity,
                configId,
                adUnitFormats,
                mediationUtils
        )
        adUnit?.setMinSizePercentage(30, 30)
        return adUnit
    }

    override fun loadAd() {
        adUnit?.fetchDemand {
            maxInterstitialAd?.loadAd()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        maxInterstitialAd?.destroy()
        adUnit?.destroy()
    }

    private fun resetAdEvents() {
        events.loaded(false)
        events.clicked(false)
        events.loadFailed(false)
        events.displayFailed(false)
        events.displayed(false)
        events.hidden(false)
    }

    private fun handleLoadButtonClick() {
        if (binding.btnLoad.text == getString(R.string.text_show)) {
            maxInterstitialAd?.showAd()
            binding.btnLoad.text = getString(R.string.text_retry)
        } else if (binding.btnLoad.text == getString(R.string.text_retry)) {
            resetAdEvents()
            binding.btnLoad.isEnabled = false
            binding.btnLoad.text = "Loading..."
            loadAd()
        }
    }

    protected fun createListener(): MaxAdListener {
        return object : MaxAdListener {
            override fun onAdLoaded(ad: MaxAd?) {
                events.loaded(true)
                binding.btnLoad.isEnabled = true
                binding.btnLoad.text = getString(R.string.text_show)
            }

            override fun onAdClicked(ad: MaxAd?) {
                events.clicked(true)
            }

            override fun onAdDisplayed(ad: MaxAd?) {
                events.displayed(true)
            }

            override fun onAdHidden(ad: MaxAd?) {
                events.hidden(true)
            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                events.loadFailed(true)

                binding.btnLoad.isEnabled = true
                Log.d(TAG, "onAdLoadFailed(): ${error?.message}")
            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                events.displayFailed(true)

                Log.d(TAG, "onAdDisplayFailed(): ${error?.message}")
            }
        }
    }

    protected class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun impression(b: Boolean) = enable(R.id.btnAdImpression, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)

        fun displayed(b: Boolean) = enable(R.id.btnAdDisplayed, b)
        fun hidden(b: Boolean) = enable(R.id.btnAdHidden, b)
        fun loadFailed(b: Boolean) = enable(R.id.btnAdLoadFailed, b)
        fun displayFailed(b: Boolean) = enable(R.id.btnAdDisplayFailed, b)

    }

}