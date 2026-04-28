package com.example.vkr.presentation.fragments

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationBinding
import com.example.vkr.App
import com.example.vkr.logic.viewmodels.StationViewModel
import com.example.vkr.presentation.service.AudioService
import com.google.common.util.concurrent.ListenableFuture

class StationFragment : Fragment(R.layout.fragment_station) {

    private val viewModel: StationViewModel by viewModels {
        StationViewModel.Factory(requireContext(), (requireActivity().application as App).getDb().cellDao())
    }

    private var _binding: FragmentStationBinding? = null
    private val binding: FragmentStationBinding
        get() = _binding ?: throw RuntimeException()

    // Локальный плеер ТОЛЬКО для видео
    private var videoPlayer: ExoPlayer? = null
    // Контроллер для подключения к фоновому аудио сервису
    private var audioControllerFuture: ListenableFuture<MediaController>? = null

    // Запрос разрешения на уведомления для Android 13+
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted -> /* Не критично, просто не покажется уведомление */ }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        _binding = FragmentStationBinding.bind(view)

        requestNotificationPermission()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack(R.id.screen_map, false)
        }

        displayInitData()
        initStationData()
        displayStationData()

        initVideoPlayer()
        initAudioController()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun initVideoPlayer() {
        videoPlayer = ExoPlayer.Builder(requireContext()).build()
        binding.videoPlayer.player = videoPlayer
    }

    private fun initAudioController() {
        val sessionToken = SessionToken(requireContext(), ComponentName(requireContext(), AudioService::class.java))
        audioControllerFuture = MediaController.Builder(requireContext(), sessionToken).buildAsync()

        audioControllerFuture?.addListener({
            // Когда контроллер готов, привязываем его к UI аудиоплеера
            binding.audioPlayer.player = audioControllerFuture?.get()
        }, Runnable::run)
    }

    private fun initStationData() {
        viewModel.getStationInfo(
            name = binding.stationNameTxt.text.toString(),
            branch = binding.branchNameTxt.text.toString()
        )
    }

    private fun displayStationData() {
        viewModel.resultLive.observe(viewLifecycleOwner) { station ->
            if (station != null) {
                binding.descriptionTextView.text = station.description

                // Загружаем первое видео (если есть)
                if (station.videosRef.isNotEmpty()) {
                    val videoItem = MediaItem.fromUri(station.videosRef[0])
                    videoPlayer?.setMediaItem(videoItem)
                    videoPlayer?.prepare()
                }

                // Загружаем первое аудио (если есть) в фоновый контроллер
                val controller = audioControllerFuture?.get()
                if (controller != null && station.audiosRef.isNotEmpty()) {
                    val audioItem = MediaItem.fromUri(station.audiosRef[0])
                    controller.setMediaItem(audioItem)
                    controller.prepare()
                } else if (station.audiosRef.isNotEmpty()) {
                    // Если контроллер еще не готов, запускаем сервис и ставим очередь
                    startAudioService(station.audiosRef[0])
                }
            }
        }
    }

    private fun startAudioService(audioUrl: String) {
        // Запускаем сервис, чтобы он создал MediaSession
        val intent = Intent(requireContext(), AudioService::class.java)
        requireContext().startService(intent)

        // Ждем подключения контроллера и ставим трек
        audioControllerFuture?.addListener({
            val controller = audioControllerFuture?.get()
            val audioItem = MediaItem.fromUri(audioUrl)
            controller?.setMediaItem(audioItem)
            controller?.prepare()
        }, Runnable::run)
    }

    private fun displayInitData() {
        val args: StationFragmentArgs by navArgs()
        binding.stationNameTxt.text = args.STATION.title
        val branchName = when (args.STATION.branchNumber) {
            1 -> "Сокольническая"
            3 -> "Арбатско-Покровская"
            5 -> "Кольцевая"
            9 -> "Серпуховско-Тимирязевская"
            else -> null
        }
        if (branchName != null) {
            binding.branchNameTxt.text = branchName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Освобождаем локальный видеоплеер
        binding.videoPlayer.player = null
        videoPlayer?.release()
        videoPlayer = null

        // Отвязываем UI от аудио-контроллера (сам сервис и плеер в нем НЕ убиваются)
        binding.audioPlayer.player = null
        audioControllerFuture?.release()
        audioControllerFuture = null

        _binding = null
    }
}