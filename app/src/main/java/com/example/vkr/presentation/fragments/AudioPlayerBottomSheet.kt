package com.example.vkr.presentation.fragments

import android.content.ComponentName
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.AudioPlayerBottomSheetBinding
import com.example.vkr.presentation.service.AudioService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.common.util.concurrent.ListenableFuture

class AudioPlayerBottomSheet : BottomSheetDialogFragment() {

    private val args: AudioPlayerBottomSheetArgs by navArgs()

    private var _binding: AudioPlayerBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AudioPlayerBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        initController()
    }

    override fun onStop() {
        super.onStop()
        releaseController()
    }

    @OptIn(UnstableApi::class)
    private fun initController() {
        val token = SessionToken(
            requireContext(),
            ComponentName(requireContext(), AudioService::class.java)
        )
        val future = MediaController.Builder(requireContext(), token).buildAsync()
        controllerFuture = future
        future.addListener({
            if (_binding == null) return@addListener
            try {
                val ctrl = future.get()
                controller = ctrl
                binding.audioPlayer.player = ctrl
                setupMedia(ctrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setupMedia(controller: MediaController) {
        val url = args.audioUrl
        if (controller.currentMediaItem?.mediaId != url) {
            controller.setMediaItem(
                MediaItem.Builder().setMediaId(url).setUri(url).build()
            )
            controller.prepare()
        }
        controller.play()
    }

    private fun releaseController() {
        _binding?.audioPlayer?.player = null
        controller = null
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controllerFuture = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}