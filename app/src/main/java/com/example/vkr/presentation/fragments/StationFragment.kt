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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.MarginPageTransformer
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationBinding
import com.example.vkr.App
import com.example.vkr.logic.viewmodels.StationViewModel
import com.example.vkr.network.dto.StationAttractionData
import com.example.vkr.network.dto.StationData
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
        StationViewModel.Factory(
            requireContext()
        )
    }
    private  var stationId : Long?=null
    private var _binding: FragmentStationBinding? = null
    private val binding: FragmentStationBinding
        get() = _binding ?: throw RuntimeException()

    private var videoPlayer: ExoPlayer? = null
    private var audioControllerFuture: ListenableFuture<MediaController>? = null

    private var pendingAudioUrl: String? = null

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {  }

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
        val sessionToken = SessionToken(
            requireContext(),
            ComponentName(requireContext(), AudioService::class.java)
        )

        audioControllerFuture = MediaController.Builder(requireContext(), sessionToken).buildAsync()

        audioControllerFuture?.addListener({
            try {
                val controller = audioControllerFuture?.get()
                if (controller != null) {
                    binding.audioPlayer.player = controller
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
                stationId=station.id
                binding.descriptionTextView.text = station.description
                binding.stationNameTxt.text = station.name
                binding.branchNameTxt.text = station.branch
                if (station.imagesRef.isNotEmpty()) {
                    binding.gallery.adapter = StationImagePagerAdapter(station.imagesRef)

                    val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.gallery_page_margin)
                    binding.gallery.setPageTransformer(MarginPageTransformer(pageMarginPx))

                    binding.gallery.offscreenPageLimit = 1
                }


//                // 2. Галерея достопримечательностей
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
                    // videoPlayer?.playWhenReady = true // раскомментируйте, если нужно авто-воспроизведение
                }

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