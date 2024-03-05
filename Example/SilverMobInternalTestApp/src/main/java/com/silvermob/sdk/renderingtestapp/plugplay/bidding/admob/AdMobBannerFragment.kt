package com.silvermob.sdk.renderingtestapp.plugplay.bidding.admob

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerAdmobBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents
import com.silvermob.sdk.renderingtestapp.utils.CommandLineArgumentParser

open class AdMobBannerFragment : AdFragment() {

    companion object {
        private const val TAG = "AdMobBannerFragment"
    }

    protected var adRequest: AdRequest? = null
    protected var adUnit: com.silvermob.sdk.api.mediation.MediationBannerAdUnit? = null
    protected var bannerView: AdView? = null
    protected var adRequestExtras: Bundle? = null

    protected val binding: FragmentBiddingBannerAdmobBinding
        get() = getBinding()
    private lateinit var events: Events

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)

        events = Events(view)

        binding.adIdLabel.text = getString(R.string.label_auid, configId)
        binding.btnLoad.setOnClickListener {
            resetAdEvents()
            it.isEnabled = false
            loadAd()
        }

        binding.btnStopRefresh.setOnClickListener {
            adUnit?.stopRefresh()
            resetEventButtons()
            binding.btnLoad.isEnabled = true
        }
    }

    override fun initAd(): Any? {
        bannerView = AdView(requireActivity())
        bannerView?.setAdSize(com.google.android.gms.ads.AdSize(width, height))
        bannerView?.adUnitId = adUnitId
        bannerView?.adListener = getListener()
        binding.viewContainer.addView(bannerView)

        adRequestExtras = Bundle()
        adRequest = AdRequest
            .Builder()
            .addNetworkExtrasBundle(com.silvermob.sdk.admob.PrebidBannerAdapter::class.java, adRequestExtras!!)
            .build()
        val mediationUtils =
                com.silvermob.sdk.admob.AdMobMediationBannerUtils(adRequestExtras, bannerView)


        adUnit = com.silvermob.sdk.api.mediation.MediationBannerAdUnit(
                requireContext(),
                configId,
                com.silvermob.sdk.AdSize(width, height),
                mediationUtils
        )
        adUnit?.setRefreshInterval(refreshDelay)
        adUnit?.let { CommandLineArgumentParser.addAdUnitSpecificData(it) }
        return adUnit
    }

    override fun loadAd() {
        adUnit?.fetchDemand { result ->
            Log.d("Prebid", "Fetch demand result: $result")
            bannerView?.loadAd(adRequest!!)
        }
    }

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode? {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER
    }

    override val layoutRes = R.layout.fragment_bidding_banner_admob

    private fun resetAdEvents() {
        events.failed(false)
        events.clicked(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adUnit?.destroy()
        bannerView?.destroy()
    }

    protected fun getListener() = object : AdListener() {
        override fun onAdLoaded() {
            Log.d(TAG, "onAdLoaded")
            resetAdEvents()
            binding.btnLoad.isEnabled = true
            events.loaded(true)
        }

        override fun onAdClicked() {
            Log.d(TAG, "onAdClicked")
            events.clicked(true)
        }

        override fun onAdOpened() {
            Log.d(TAG, "onAdOpened")
            events.opened(true)
        }

        override fun onAdImpression() {
            Log.d(TAG, "onAdImpression")
            events.impression(true)
        }

        override fun onAdClosed() {
            Log.d(TAG, "onAdClosed")
            events.closed(true)
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            Log.d(TAG, "onAdFailedToLoad - ${p0.message}")
            resetAdEvents()
            binding.btnLoad.isEnabled = true
            events.failed(true)
        }

    }

    private class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun impression(b: Boolean) = enable(R.id.btnAdImpression, b)
        fun opened(b: Boolean) = enable(R.id.btnAdOpened, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun closed(b: Boolean) = enable(R.id.btnAdClosed, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)

    }

}