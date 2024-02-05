package com.silvermob.sdk.renderingtestapp.plugplay.bidding.gam.original

import android.net.Uri
import android.view.ViewGroup
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.silvermob.sdk.*
import com.silvermob.sdk.renderingtestapp.AdFragment
import com.silvermob.sdk.renderingtestapp.R
import com.silvermob.sdk.renderingtestapp.databinding.FragmentBiddingBannerVideoBinding
import com.silvermob.sdk.renderingtestapp.plugplay.config.AdConfiguratorDialogFragment

class GamOriginalInstreamFragment : AdFragment() {

    private var adUnit: com.silvermob.sdk.VideoAdUnit? = null
    private var player: SimpleExoPlayer? = null
    private var adsUri: Uri? = null
    private var adsLoader: ImaAdsLoader? = null
    private var playerView: PlayerView? = null

    private val binding: FragmentBiddingBannerVideoBinding
        get() = getBinding()

    override val layoutRes: Int = R.layout.fragment_bidding_banner_video

    override fun initAd(): Any? {
        com.silvermob.sdk.SilverMob.setServerAccountId("1001")
        createAd()
        return null
    }

    override fun loadAd() {
        adUnit?.fetchDemand { _: com.silvermob.sdk.ResultCode?, keysMap: Map<String?, String?>? ->
            val sizes = HashSet<com.silvermob.sdk.AdSize>()
            sizes.add(com.silvermob.sdk.AdSize(width, height))
            adsUri = Uri.parse(
                com.silvermob.sdk.Util.generateInstreamUriForGam(
                    adUnitId,
                    sizes,
                    keysMap
                )
            )
            val imaBuilder = ImaAdsLoader.Builder(requireActivity())
            adsLoader = imaBuilder.build()
            initializePlayer()
        }
    }

    override fun configuratorMode(): AdConfiguratorDialogFragment.AdConfiguratorMode? {
        return AdConfiguratorDialogFragment.AdConfiguratorMode.BANNER
    }

    private fun createAd() {
        playerView = PlayerView(requireContext())
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600)
        binding.viewContainer.addView(playerView, params)

        val parameters = com.silvermob.sdk.VideoBaseAdUnit.Parameters()
        parameters.protocols = listOf(com.silvermob.sdk.Signals.Protocols.VAST_2_0)
        parameters.playbackMethod = listOf(com.silvermob.sdk.Signals.PlaybackMethod.AutoPlaySoundOff)
        parameters.placement = com.silvermob.sdk.Signals.Placement.InStream
        parameters.mimes = listOf("video/mp4")

        adUnit = com.silvermob.sdk.VideoAdUnit(configId, width, height)
        adUnit?.parameters = parameters
    }

    private fun initializePlayer() {
        val playerBuilder = SimpleExoPlayer.Builder(requireContext())
        player = playerBuilder.build()
        playerView!!.player = player
        adsLoader!!.setPlayer(player)

        val uri = Uri.parse("https://storage.googleapis.com/gvabox/media/samples/stock.mp4")
        // Uri uri = Uri.parse("<![CDATA[https://storage.googleapis.com/gvabox/media/samples/stock.mp4]]>");

        val mediaItem = MediaItem.fromUri(uri)
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(requireContext(), getString(R.string.app_name))
        val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)
        val mediaSource: MediaSource = mediaSourceFactory.createMediaSource(mediaItem)
        val dataSpec = DataSpec(adsUri!!)
        val adsMediaSource = AdsMediaSource(
            mediaSource, dataSpec, "ad", mediaSourceFactory,
            adsLoader!!, playerView!!
        )
        player?.setMediaSource(adsMediaSource)
        player?.playWhenReady = true
        player?.prepare()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adUnit?.stopAutoRefresh()
        adsLoader?.setPlayer(null)
        adsLoader?.release()
        player?.release()
        com.silvermob.sdk.SilverMob.setServerAccountId(getString(R.string.prebid_account_id_prod))
    }

}