package com.example.vkr.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationBinding
import com.example.vkr.logic.viewmodels.StationViewModel
import com.example.vkr.network.dto.StationAttractionData
import com.example.vkr.presentation.adapters.StationAttractionPagerAdapter
import com.example.vkr.presentation.adapters.StationImagePagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationFragment : Fragment(R.layout.fragment_station) {

    private val viewModel: StationViewModel by viewModels()

    private var _binding: FragmentStationBinding? = null
    private val binding: FragmentStationBinding
        get() = _binding ?: throw RuntimeException()

    private var videoPlayer: ExoPlayer? = null

    // Данные для pop-up плеера
    private var audioUrl: String? = null
    private var videoUrl: String? = null

    private var stationName: String? = null
    private var branchName: String? = null
    private var branchLogoRes: Int = 0

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startScreen()
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

    private fun checkLocationPermission() {
        val hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            startScreen()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun startScreen() {
        binding.root.visibility = View.VISIBLE
        requestNotificationPermission()
        initStationData()
        displayStationData()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

//    private fun initVideoPlayer() {
//        videoPlayer = ExoPlayer.Builder(requireContext())
//            .setSeekBackIncrementMs(15_000)
//            .setSeekForwardIncrementMs(15_000)
//            .build()
//        binding.videoPlayer.player = videoPlayer
//    }

    private fun displayStationData() {
        viewModel.resultLive.observe(viewLifecycleOwner) { station ->
            if (station == null) return@observe

            stationName = station.name
            branchName = station.branch

            binding.descriptionTextView.text = station.description
            binding.stationNameTxt.text = station.name
            binding.branchNameTxt.text = station.branch

            if (station.imagesRef.isNotEmpty()) {
                binding.gallery.adapter = StationImagePagerAdapter(station.imagesRef)
                val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.gallery_page_margin)
                binding.gallery.setPageTransformer(MarginPageTransformer(pageMarginPx))
                binding.gallery.offscreenPageLimit = 1
            }


//            if (station.videosRef.isNotEmpty()) {
//                videoPlayer?.setMediaItem(MediaItem.fromUri(station.videosRef[0]))
//                videoPlayer?.prepare()
//            }

            // Сохраняем URL аудио для pop-up
            audioUrl = station.audiosRef.firstOrNull()
            videoUrl = station.videosRef.firstOrNull()

            branchLogoRes = when (station.branch) {
                "Сокольническая" -> R.drawable.red_branch_logo
                "Серпуховско-Тимирязевская" -> R.drawable.gray_branch_logo
                "Арбатско-Покровская" -> R.drawable.blue_branch_logo
                "Кольцевая" -> R.drawable.brown_branch_logo
                else -> 0
            }
            binding.branchLogo.setImageResource(branchLogoRes)


        }

        binding.btnAudioGuide.setOnClickListener { openAudioGuide() }
        binding.btnVideoGuide.setOnClickListener { openVideoGuide() }

    }
    private fun openVideoGuide() {
        val url = videoUrl
        if (url.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Видеогид недоступен", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.openVideoGuide(
            videoUrl = url
           )

    }
    private fun openAudioGuide() {
        val url = audioUrl
        if (url.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Аудиогид недоступен", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.openAudioGuide(
            audioUrl = url)

    }

    private fun initStationData() {
        val args: StationFragmentArgs by navArgs()
        val station = args.STATION
        if (station != null) {
            val branch = when (station.branchNumber) {
                1 -> "Сокольническая"
                3 -> "Арбатско-Покровская"
                5 -> "Кольцевая"
                9 -> "Серпуховско-Тимирязевская"
                else -> ""
            }
            viewModel.getStationInfo(name = station.title, branch = branch)
            return
        }
        viewModel.getCurrentStation()
    }

    @OptIn(UnstableApi::class)
    override fun onDestroyView() {
        super.onDestroyView()
       // binding.videoPlayer.player = null
        videoPlayer?.release()
        videoPlayer = null
        _binding = null
    }
}