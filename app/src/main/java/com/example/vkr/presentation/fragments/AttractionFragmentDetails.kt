package com.example.vkr.presentation.fragments

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAttractionDetailsBinding
import com.example.vkr.logic.viewmodels.AttractionViewModel
import com.example.vkr.network.dto.Attraction
import com.example.vkr.presentation.adapters.AttractionImagesPagerAdapter
import com.example.vkr.presentation.adapters.StationImagePagerAdapter
import com.example.vkr.presentation.service.AttractionAudioService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class AttractionFragmentDetails : Fragment() {

    private val viewModel: AttractionViewModel by viewModels {
        AttractionViewModel.Factory(requireContext())
    }

    private var _binding: FragmentAttractionDetailsBinding? = null
    private val binding get() = _binding!!

    // Аудио теперь идёт через сервис
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var audioController: MediaController? = null

    // Видео остаётся локальным
    private var videoPlayer: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttractionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAttractionData()
        getAttraction()
    }

    private fun getAttraction() {
        viewModel.resultLive.observe(viewLifecycleOwner) { attraction ->
            if (attraction != null) {
                bindData(attraction)
                setupGallery(attraction)
                setupBackButton()
                setupBuyButton(attraction)
                setupAudio(attraction)
                setupVideo(attraction)
            }
        }
    }

    private fun initAttractionData() {
        val args: AttractionFragmentDetailsArgs by navArgs()
        viewModel.getAttraction(args.ATTRACTION.id)
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(attraction: Attraction) = with(binding) {
        attractionNameTxt.text = attraction.name
        addressDetailsTxt.text = attraction.address

        val phone = attraction.phoneNumber?.takeIf { it.isNotBlank() }
        val email = attraction.email?.takeIf { it.isNotBlank() }

        when {
            phone != null && email != null -> referenceInformationDetailsTxt.text = "$phone\n$email"
            phone != null -> referenceInformationDetailsTxt.text = phone
            email != null -> referenceInformationDetailsTxt.text = email
            else -> {
                referenceInformationDetailsTxt.visibility = View.GONE
                referenceInformationTxt.visibility = View.GONE
            }
        }

        if (!attraction.workingHours.isNullOrBlank()) {
            workingHoursDetailsTxt.text = attraction.workingHours
        } else {
            workingHoursDetailsTxt.visibility = View.GONE
            workingHoursTxt.visibility = View.GONE
        }

        if (attraction.price != null && attraction.price!! > 0) {
            ticketsDetailsTxt.text = "От " + attraction.price + " рублей"
        } else {
            ticketsTxt.visibility = View.GONE
            ticketsDetailsTxt.visibility = View.GONE
            buyTicketBtn.visibility = View.GONE
        }
        infoDetailsTxt.text = attraction.description
    }

    private fun setupGallery(attraction: Attraction) {
        binding.gallery.adapter = StationImagePagerAdapter(attraction.images)
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.gallery_page_margin)
        binding.gallery.setPageTransformer(MarginPageTransformer(pageMarginPx))
        binding.gallery.offscreenPageLimit = 1
    }

    private fun setupBuyButton(attraction: Attraction) {
        binding.buyTicketBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, attraction.url?.toUri())
            startActivity(intent)
        }
    }

    private fun setupBackButton() {
        binding.backText.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupAudio(attraction: Attraction) {
        val url = attraction.audios?.firstOrNull()?.takeIf { it.isNotBlank() }
        if (url == null) {
            binding.audioPlayerAttraction.visibility = View.GONE
            return
        }

        // Подключаемся к сервису через MediaController
        val token = SessionToken(
            requireContext(),
            ComponentName(requireContext(), AttractionAudioService::class.java)
        )

        controllerFuture = MediaController.Builder(requireContext(), token)
            .buildAsync()
            .also { future ->
                future.addListener({
                    if (!isAdded || _binding == null) return@addListener
                    val controller = future.get()
                    audioController = controller
                    binding.audioPlayerAttraction.player = controller

                    val mediaItem = MediaItem.Builder()
                        .setUri(url)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setTitle(attraction.name)
                                .setArtist("Аудиогид")
                                .build()
                        )
                        .build()

                    // Если в сервисе уже играет тот же трек — не пересоздаём
                    val currentUri = controller.currentMediaItem?.localConfiguration?.uri?.toString()
                    if (currentUri != url) {
                        controller.setMediaItem(mediaItem)
                        controller.prepare()
                        controller.playWhenReady = false
                    }
                }, MoreExecutors.directExecutor())
            }
    }

    private fun setupVideo(attraction: Attraction) {
        val url = attraction.videos?.firstOrNull()?.takeIf { it.isNotBlank() }
        if (url == null) {
            binding.videoPlayerAttraction.visibility = View.GONE
            return
        }
        videoPlayer = ExoPlayer.Builder(requireContext())
            .setSeekBackIncrementMs(15_000)
            .setSeekForwardIncrementMs(15_000).build().also { player ->
            binding.videoPlayerAttraction.player = player
            player.setMediaItem(MediaItem.fromUri(url))
            player.prepare()
            player.playWhenReady = false
        }
    }

    override fun onPause() {
        super.onPause()
        // Аудио НЕ ставим на паузу — пусть играет в фоне.
        videoPlayer?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding?.audioPlayerAttraction?.player = null
        audioController?.release()
        audioController = null
        controllerFuture?.let { MediaController.releaseFuture(it) }
        controllerFuture = null

        videoPlayer?.release()
        videoPlayer = null

        _binding = null
    }
}