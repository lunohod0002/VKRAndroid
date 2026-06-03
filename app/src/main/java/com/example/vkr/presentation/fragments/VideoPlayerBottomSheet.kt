package com.example.vkr.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.VideoPlayerBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VideoPlayerBottomSheet : BottomSheetDialogFragment() {

    private val args: VideoPlayerBottomSheetArgs by navArgs()

    private var _binding: VideoPlayerBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var videoPlayer: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VideoPlayerBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onStart() {
        super.onStart()
        initVideoPlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    @OptIn(UnstableApi::class)
    private fun initVideoPlayer() {
        val player = ExoPlayer.Builder(requireContext())
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build(),
                /* handleAudioFocus = */ true
            )
            .setSeekBackIncrementMs(15_000)
            .setSeekForwardIncrementMs(15_000)
            .setHandleAudioBecomingNoisy(true)
            .build()

        videoPlayer = player
        binding.videoPlayerPopUp.player = player

        player.setMediaItem(MediaItem.fromUri(args.videoUrl))
        player.prepare()
        player.playWhenReady = true
    }

    private fun releasePlayer() {
        _binding?.videoPlayerPopUp?.player = null
        videoPlayer?.release()
        videoPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}