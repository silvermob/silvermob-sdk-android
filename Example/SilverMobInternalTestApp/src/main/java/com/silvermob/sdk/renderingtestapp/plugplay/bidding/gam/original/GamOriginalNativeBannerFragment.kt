package com.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.original

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.silvermob.sdk.NativeEventTracker
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.BaseEvents

class GamOriginalNativeBannerFragment : AdFragment() {

    companion object {
        private const val TAG = "GamOriginalNativeBanner"
    }

    private var nativeAdUnit: com.silvermob.sdk.NativeAdUnit? = null
    override val layoutRes: Int = R.layout.fragment_bidding_banner

    private val binding: FragmentBiddingBannerBinding
        get() = getBinding()
    private lateinit var events: Events

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        events = Events(view)
        binding.btnLoad.setOnClickListener {
            resetEventButtons()
            loadAd()
        }
    }

    override fun initAd(): Any? {
        nativeAdUnit = configureNativeAdUnit()
        return nativeAdUnit
    }

    override fun loadAd() {
        createAd()
    }

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode? {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER
    }

    private fun createAd() {
        val gamView = AdManagerAdView(requireContext())
        gamView.adUnitId = adUnitId
        gamView.setAdSizes(AdSize.FLUID)
        gamView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                com.silvermob.sdk.addendum.AdViewUtils.findPrebidCreativeSize(gamView, object : com.silvermob.sdk.addendum.AdViewUtils.PbFindSizeListener {
                    override fun success(width: Int, height: Int) {
                        gamView.setAdSizes(AdSize(width, height))
                    }

                    override fun failure(error: com.silvermob.sdk.addendum.PbFindSizeError) {}
                })
                Log.d(TAG, "onAdLoaded() called")
                resetEventButtons()
                events.loaded(true)
                binding.btnLoad.isEnabled = true
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.d(TAG, "onAdFailed() called with throwable = [${p0.message}]")
                resetEventButtons()
                events.failed(true)
                binding.btnLoad.isEnabled = true
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Log.d(TAG, "onAdClicked() called")
                events.clicked(true)
            }

        }
        binding.viewContainer.addView(gamView)

        val builder = AdManagerAdRequest.Builder()
        nativeAdUnit?.setAutoRefreshInterval(refreshDelay)
        nativeAdUnit?.fetchDemand(builder) {
            val request = builder.build()
            gamView.loadAd(request)
        }
    }

    private fun configureNativeAdUnit(): com.silvermob.sdk.NativeAdUnit {
        val adUnit = com.silvermob.sdk.NativeAdUnit(configId)

        adUnit.setContextType(com.silvermob.sdk.NativeAdUnit.CONTEXT_TYPE.SOCIAL_CENTRIC)
        adUnit.setPlacementType(com.silvermob.sdk.NativeAdUnit.PLACEMENTTYPE.CONTENT_FEED)
        adUnit.setContextSubType(com.silvermob.sdk.NativeAdUnit.CONTEXTSUBTYPE.GENERAL_SOCIAL)
        val methods = ArrayList<com.silvermob.sdk.NativeEventTracker.EVENT_TRACKING_METHOD>()
        methods.add(com.silvermob.sdk.NativeEventTracker.EVENT_TRACKING_METHOD.IMAGE)
        try {
            val tracker = com.silvermob.sdk.NativeEventTracker(NativeEventTracker.EVENT_TYPE.IMPRESSION, methods)
            adUnit.addEventTracker(tracker)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val title = com.silvermob.sdk.NativeTitleAsset()
        title.setLength(90)
        title.isRequired = true
        adUnit.addAsset(title)
        val icon = com.silvermob.sdk.NativeImageAsset(20, 20, 20, 20)
        icon.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.ICON
        icon.isRequired = true
        adUnit.addAsset(icon)
        val image = com.silvermob.sdk.NativeImageAsset(200, 200, 200, 200)
        image.imageType = com.silvermob.sdk.NativeImageAsset.IMAGE_TYPE.MAIN
        image.isRequired = true
        adUnit.addAsset(image)
        val data = com.silvermob.sdk.NativeDataAsset()
        data.len = 90
        data.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.SPONSORED
        data.isRequired = true
        adUnit.addAsset(data)
        val body = com.silvermob.sdk.NativeDataAsset()
        body.isRequired = true
        body.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.DESC
        adUnit.addAsset(body)
        val cta = com.silvermob.sdk.NativeDataAsset()
        cta.isRequired = true
        cta.dataType = com.silvermob.sdk.NativeDataAsset.DATA_TYPE.CTATEXT
        adUnit.addAsset(cta)
        return adUnit
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeAdUnit?.stopAutoRefresh()
    }

    private class Events(parentView: View) : BaseEvents(parentView) {

        fun loaded(b: Boolean) = enable(R.id.btnAdLoaded, b)
        fun clicked(b: Boolean) = enable(R.id.btnAdClicked, b)
        fun failed(b: Boolean) = enable(R.id.btnAdFailed, b)

    }

}