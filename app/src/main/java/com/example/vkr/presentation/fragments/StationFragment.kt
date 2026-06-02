package com.example.vkr.presentation.fragments

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationBinding
import com.example.vkr.logic.viewmodels.StationViewModel
import com.example.vkr.network.dto.AttractionId
import com.example.vkr.network.dto.StationAttractionData
import com.example.vkr.presentation.adapters.StationAttractionPagerAdapter
import com.example.vkr.presentation.adapters.StationImagePagerAdapter
import com.example.vkr.presentation.service.AudioService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.util.concurrent.ListenableFuture
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.TextStyle
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationFragment : Fragment(R.layout.fragment_station) {

    private val viewModel: StationViewModel by viewModels()

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
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            binding.root.visibility = View.VISIBLE

            requestNotificationPermission()

            initStationData()
            displayStationData()
            initVideoPlayer()
            initAudioServiceAndController()
        } else {
            Toast.makeText(
                requireContext(),
                "Без разрешения на геолокацию работа станции невозможна",
                Toast.LENGTH_LONG
            ).show()

            viewModel.onBackPressed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStationBinding.bind(view)
        checkLocationPermission()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun checkLocationPermission() {
        val hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            binding.root.visibility = View.VISIBLE
            requestNotificationPermission()
            initStationData()
            displayStationData()
            initVideoPlayer()
            initAudioServiceAndController()
        } else {

            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun initVideoPlayer() {
        videoPlayer = ExoPlayer.Builder(requireContext())
            .setSeekBackIncrementMs(15_000)
            .setSeekForwardIncrementMs(15_000).build()
        binding.videoPlayer.player = videoPlayer
    }

    @OptIn(UnstableApi::class)
    private fun initAudioServiceAndController() {
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
                    currentAudioController = controller

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
                binding.descriptionTextView.text = station.description
                binding.stationNameTxt.text = station.name
                binding.branchNameTxt.text = station.branch
                binding.extraServicesInfo.text =
                    station.extraServices.joinToString("\n") { service ->
                        " • ${service}"
                    }
                if (station.imagesRef.isNotEmpty()) {
                    binding.gallery.adapter = StationImagePagerAdapter(station.imagesRef)
                    val pageMarginPx =
                        resources.getDimensionPixelOffset(R.dimen.gallery_page_margin)
                    binding.gallery.setPageTransformer(MarginPageTransformer(pageMarginPx))
                    binding.gallery.offscreenPageLimit = 1
                }

                if (station.attractionResponseList.isNotEmpty()) {
                    binding.attractionsGallery.adapter = StationAttractionPagerAdapter(
                        { attraction ->
                            viewModel.navigateToAttractionDetails(attraction.id)
                        },
                        station.attractionResponseList
                    )
                    val pageMarginPx =
                        resources.getDimensionPixelOffset(R.dimen.gallery_page_margin)
                    binding.attractionsGallery.setPageTransformer(MarginPageTransformer(pageMarginPx))
                    binding.attractionsGallery.offscreenPageLimit = 1
                }

                if (station.videosRef.isNotEmpty()) {
                    val videoItem = MediaItem.fromUri(station.videosRef[0])
                    videoPlayer?.setMediaItem(videoItem)
                    videoPlayer?.prepare()
                }

                if (station.audiosRef.isNotEmpty()) {
                    playAudioSafely(station.audiosRef[0])
                }

                val iconResId = when (station.branch) {
                    "Сокольническая" -> R.drawable.red_branch_logo
                    "Серпуховско-Тимирязевская" -> R.drawable.gray_branch_logo
                    "Арбатско-Покровская" -> R.drawable.blue_branch_logo
                    "Кольцевая" -> R.drawable.brown_branch_logo
                    else -> 0
                }

                if (iconResId != 0) {
                    binding.branchLogo.setImageResource(iconResId)
                } else {
                    binding.branchLogo.setImageResource(0)
                }

                binding.seeAllTextView.setOnClickListener {
                    val stationData = StationAttractionData(
                        title = station.name,
                        id = station.id,
                        branch = station.branch
                    )
                    viewModel.navigateToStationAttractions(stationData)
                }
            }
        }


    }

    private fun playAudioSafely(audioUrl: String) {
        if (currentAudioController != null) {
            playAudio(currentAudioController!!, audioUrl)
        } else {
            pendingAudioUrl = audioUrl
        }
    }

    private fun playAudio(controller: MediaController, audioUrl: String) {
        val audioItem = MediaItem.fromUri(audioUrl)

        controller.stop()
        controller.setMediaItem(audioItem)
        controller.prepare()
        // controller.playWhenReady = true
    }

    private fun initStationData() {
        val args: StationFragmentArgs by navArgs()
        if (args.STATION != null) {
            val stationName = args.STATION!!.title
            val branchName = when (args.STATION!!.branchNumber) {
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
            return
        }
        viewModel.getCurrentStation()
    }

    @OptIn(UnstableApi::class)
    override fun onDestroyView() {
        super.onDestroyView()

        binding.videoPlayer.player = null
        videoPlayer?.release()
        videoPlayer = null


        binding.audioPlayer.player = null

        currentAudioController = null
        pendingAudioUrl = null

        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        audioControllerFuture?.let { future ->
            MediaController.releaseFuture(future)
        }
        audioControllerFuture = null
    }


}
