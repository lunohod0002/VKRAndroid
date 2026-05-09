package com.example.vkr.presentation.fragments

import android.Manifest
import android.content.ComponentName
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationBinding
import com.example.vkr.logic.viewmodels.StationViewModel
import com.example.vkr.network.dto.StationAttractionData
import com.example.vkr.presentation.adapters.StationAttractionPagerAdapter
import com.example.vkr.presentation.adapters.StationImagePagerAdapter
import com.example.vkr.presentation.service.AudioService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.util.concurrent.ListenableFuture
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.TextStyle
import com.yandex.runtime.image.ImageProvider

class StationFragment : Fragment(R.layout.fragment_station) {

    private val viewModel: StationViewModel by viewModels {
        StationViewModel.Factory(requireContext())
    }

    private var stationId: Long? = null
    private var _binding: FragmentStationBinding? = null
    private val binding: FragmentStationBinding
        get() = _binding ?: throw RuntimeException()

    private var videoPlayer: ExoPlayer? = null

    // Работа с аудио
    private var audioControllerFuture: ListenableFuture<MediaController>? = null
    private var currentAudioController: MediaController? = null // Сохраняем подключенный контроллер
    private var pendingAudioUrl: String? = null

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

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
        // Если контроллер уже создается или создан, не делаем это повторно при пересоздании фрагмента
        if (audioControllerFuture != null) return

        val sessionToken = SessionToken(
            requireContext(),
            ComponentName(requireContext(), AudioService::class.java)
        )

        audioControllerFuture = MediaController.Builder(requireContext(), sessionToken).buildAsync()

        audioControllerFuture?.addListener({
            try {
                val controller = audioControllerFuture?.get()
                if (controller != null) {
                    currentAudioController = controller // Кэшируем контроллер
                    binding.audioPlayer.player = controller

                    // Если URL пришел до того, как контроллер подключился, играем его сейчас
                    pendingAudioUrl?.let { url ->
                        playAudio(controller, url)
                        pendingAudioUrl = null
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun displayStationData() {
        viewModel.resultLive.observe(viewLifecycleOwner) { station ->
            if (station != null) {
                stationId = station.id
                binding.descriptionTextView.text = station.description
                binding.stationNameTxt.text = station.name
                binding.branchNameTxt.text = station.branch

                if (station.imagesRef.isNotEmpty()) {
                    binding.gallery.adapter = StationImagePagerAdapter(station.imagesRef)
                    val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.gallery_page_margin)
                    binding.gallery.setPageTransformer(MarginPageTransformer(pageMarginPx))
                    binding.gallery.offscreenPageLimit = 1
                }

                if (station.attractionResponseList.isNotEmpty()) {
                    binding.attractionsGallery.adapter = StationAttractionPagerAdapter(station.attractionResponseList)
                    val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.gallery_page_margin)
                    binding.attractionsGallery.setPageTransformer(MarginPageTransformer(pageMarginPx))
                    binding.attractionsGallery.offscreenPageLimit = 1
                }

                if (station.videosRef.isNotEmpty()) {
                    val videoItem = MediaItem.fromUri(station.videosRef[0])
                    videoPlayer?.setMediaItem(videoItem)
                    videoPlayer?.prepare()
                }

                // Запускаем аудио
                if (station.audiosRef.isNotEmpty()) {
                    playAudioSafely(station.audiosRef[0])
                }
            }
        }
        val args: StationFragmentArgs by navArgs()
        val branchNumber = args.STATION.branchNumber
        val iconResId = when (branchNumber) {
            1 -> R.drawable.red_branch_logo
            9 -> R.drawable.gray_branch_logo
            3 -> R.drawable.blue_branch_logo
            5 -> R.drawable.brown_branch_logo
            else -> 0
        }

        if (iconResId != 0) {
            binding.branchLogo.setImageResource(iconResId)
        } else {
            binding.branchLogo.setImageResource(0)
        }

        binding.seeAllTextView.setOnClickListener {

            val stationData = StationAttractionData(
                title = args.STATION.title,id=stationId!!, branchNumber = args.STATION.branchNumber)

            val action= StationFragmentDirections.actionScreenStationToStationAttractionsFragment(
                STATION = stationData
            )
            val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            val previousItemId = bottomNavigationView?.selectedItemId
            if (previousItemId != null) {
                bottomNavigationView.menu.findItem(previousItemId)?.isChecked = false

            }
            findNavController().navigate(action)
        }
    }

    private fun playAudioSafely(audioUrl: String) {
        // Если контроллер уже подключен, меняем трек сразу
        if (currentAudioController != null) {
            playAudio(currentAudioController!!, audioUrl)
        } else {
            // Если контроллер еще не успел подключиться к сервису, сохраняем URL в очередь
            pendingAudioUrl = audioUrl
        }
    }

    private fun playAudio(controller: MediaController, audioUrl: String) {
        val audioItem = MediaItem.fromUri(audioUrl)

        // Останавливаем текущее воспроизведение и очищаем плейлист
        controller.stop()
        // Устанавливаем новый трек
        controller.setMediaItem(audioItem)
        controller.prepare()
        // controller.playWhenReady = true // Раскомментировано для автоматического воспроизведения
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

        binding.videoPlayer.player = null
        videoPlayer?.release()
        videoPlayer = null

        // ОТВЯЗЫВАЕМ UI от контроллера, но НЕ ОСВОБОЖДАЕМ контроллер и сервис!
        // Сервис продолжит играть музыку в фоне, если пользователь свернул приложение.
        binding.audioPlayer.player = null

        // Обнуляем ссылку на контроллер для этого View
        currentAudioController = null
        pendingAudioUrl = null

        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Полностью отключаемся от сервиса только когда фрагмент окончательно умирает
        // (Не при смене экрана назад/вперед, а именно при уничтожении ViewModel/Фрагмента)
        audioControllerFuture?.let { future ->
            MediaController.releaseFuture(future)
        }
        audioControllerFuture = null
    }
}