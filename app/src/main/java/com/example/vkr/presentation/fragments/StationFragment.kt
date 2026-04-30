package com.example.vkr.presentation.fragments

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationBinding
import com.example.vkr.App
import com.example.vkr.logic.viewmodels.StationViewModel
import com.example.vkr.presentation.service.AudioService
import com.google.common.util.concurrent.ListenableFuture

class StationFragment : Fragment(R.layout.fragment_station) {

    private val viewModel: StationViewModel by viewModels {
        StationViewModel.Factory(
            requireContext(),
            (requireActivity().application as App).getDb().cellDao()
        )
    }

    private var _binding: FragmentStationBinding? = null
    private val binding: FragmentStationBinding
        get() = _binding ?: throw RuntimeException()

    private var videoPlayer: ExoPlayer? = null
    private var audioControllerFuture: ListenableFuture<MediaController>? = null

    // Переменная для сохранения URL, если данные пришли быстрее, чем подключился MediaController
    private var pendingAudioUrl: String? = null

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* isGranted -> можно добавить логику, если нужно */ }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStationBinding.bind(view)

        requestNotificationPermission()
        initStationData()
        displayStationData()
        initVideoPlayer()
        initAudioServiceAndController()
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

    private fun initAudioServiceAndController() {
        // 1. СНАЧАЛА запускаем сервис, чтобы он создал MediaSession
        val intent = Intent(requireContext(), AudioService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)

        // 2. Подключаемся к сессии сервиса через MediaController
        val sessionToken = SessionToken(
            requireContext(),
            ComponentName(requireContext(), AudioService::class.java)
        )

        audioControllerFuture = MediaController.Builder(requireContext(), sessionToken).buildAsync()

        // 3. Добавляем слушатель ПОДКЛЮЧЕНИЯ контроллера (обязательно в главном потоке!)
        audioControllerFuture?.addListener({
            try {
                val controller = audioControllerFuture?.get()
                if (controller != null) {
                    // Привязываем контроллер к UI
                    binding.audioPlayer.player = controller

                    // Если URL аудио уже пришел (гонка), запускаем его
                    pendingAudioUrl?.let { url ->
                        playAudio(controller, url)
                        pendingAudioUrl = null
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext())) // Безопасный Executor для UI
    }

    private fun displayStationData() {
        viewModel.resultLive.observe(viewLifecycleOwner) { station ->
            if (station != null) {
                binding.descriptionTextView.text = station.description
                binding.stationNameTxt.text = station.name
                binding.branchNameTxt.text = station.branch

                if (station.videosRef.isNotEmpty()) {
                    val videoItem = MediaItem.fromUri(station.videosRef[0])
                    videoPlayer?.setMediaItem(videoItem)
                    videoPlayer?.prepare()
                    // videoPlayer?.playWhenReady = true // раскомментируйте, если нужно авто-воспроизведение
                }

                if (station.audiosRef.isNotEmpty()) {
                    playAudioSafely(station.audiosRef[0])
                }
            }
        }
    }

    private fun playAudioSafely(audioUrl: String) {
        val future = audioControllerFuture ?: return

        if (future.isDone) {
            // Если контроллер уже готов, просто играем
            try {
                val controller = future.get()
                playAudio(controller, audioUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Если контроллер еще в процессе подключения, сохраняем URL в pendingAudioUrl.
            // Когда контроллер подключится (в initAudioServiceAndController), он сам запустит этот URL.
            pendingAudioUrl = audioUrl
        }
    }

    // Вынесенная логика непосредственно воспроизведения
    private fun playAudio(controller: MediaController, audioUrl: String) {
        val audioItem = MediaItem.fromUri(audioUrl)
        controller.setMediaItem(audioItem)
        controller.prepare()
        // controller.playWhenReady = true // раскомментируйте для авто-воспроизведения
    }

    private fun initStationData() {
        val args: StationFragmentArgs by navArgs()
        val stationName = args.STATION.title
        val branchName = when (args.STATION.branchNumber) {
            1 -> "Сокольническая"
            3 -> "Арбатско-Покровская"
            5 -> "Кольцевая"
            9 -> "Серпуховско-Тимирязевская"
            else -> ""
        }
        viewModel.getStationInfo(
            name = stationName,
            branch = branchName
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Очистка VideoPlayer
        binding.videoPlayer.player = null
        videoPlayer?.release()
        videoPlayer = null

        // Очистка AudioController (отключаем от UI, но не останавливаем сервис!)
        binding.audioPlayer.player = null
        audioControllerFuture?.let { future ->
            MediaController.releaseFuture(future)
        }
        audioControllerFuture = null
        pendingAudioUrl = null

        _binding = null
    }
}