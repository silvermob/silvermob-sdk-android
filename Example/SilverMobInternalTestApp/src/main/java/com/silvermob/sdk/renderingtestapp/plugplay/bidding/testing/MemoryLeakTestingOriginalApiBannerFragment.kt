package com.silvermob.sdk.renderingtestapp.plugplay.bidding.testing

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment
import com.silvermob.sdk.renderingtestapp.utils.CommandLineArgumentParser

/**
 * Example for testing memory leaks with original API ad units.
 * It doesn't use Google ad view because it causes another memory leak after loadAd().
 */
open class MemoryLeakTestingOriginalApiBannerFragment : AdFragment() {

    private var adUnit: com.silvermob.sdk.BannerAdUnit? = null

    override val layoutRes = R.layout.fragment_bidding_banner

    private val binding: FragmentBiddingBannerBinding
        get() = getBinding()

    override fun configuratorMode() = AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER

    open fun createAdUnit() = com.silvermob.sdk.BannerAdUnit(configId, width, height)

    override fun initUi(view: View, savedInstanceState: Bundle?) {
        super.initUi(view, savedInstanceState)
        binding.adIdLabel.text = getString(R.string.label_auid, configId)
        binding.btnLoad.setOnClickListener {
            resetEventButtons()
            loadAd()
        }
    }

    override fun initAd(): Any {
        adUnit = createAdUnit()
        adUnit?.setAutoRefreshInterval(refreshDelay)
        CommandLineArgumentParser.addAdUnitSpecificData(adUnit!!)
        return "Testing"
    }

    override fun loadAd() {
        val request = AdManagerAdRequest.Builder().build()

        // 1) Fetch Demand with anonymous class
        adUnit?.fetchDemand(request) {
            Log.d("TEST", "Anonymous fetch demand")
        }

        // 2) Fetch Demand with static class and weak references
//        adUnit?.fetchDemand(request, MyOnCompleteListener())
    }


    private class MyOnCompleteListener : com.silvermob.sdk.OnCompleteListener {

        override fun onComplete(resultCode: com.silvermob.sdk.ResultCode?) {
            Log.d("TEST", "Static fetch demand")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        // 1) Call only stop auto-refresh
//        adUnit?.stopAutoRefresh()

        // 2) Call destroy
        adUnit?.destroy()
    }

}