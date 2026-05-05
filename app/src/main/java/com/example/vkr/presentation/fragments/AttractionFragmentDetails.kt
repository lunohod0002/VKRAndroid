package com.example.vkr.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.databinding.FragmentAttractionDetailsBinding
import com.example.vkr.logic.viewmodels.AttractionViewModel
import com.example.vkr.network.dto.Attraction
import com.example.vkr.network.dto.MockAttractions
import com.example.vkr.presentation.adapters.AttractionImagesPagerAdapter
import kotlin.getValue

class AttractionFragmentDetails : Fragment() {

    private val viewModel: AttractionViewModel by viewModels {
        AttractionViewModel.Factory(
        )
    }

    //TODO: Добавить nav args
    private var _binding: FragmentAttractionDetailsBinding? = null
    private val binding get() = _binding!!

    private var audioPlayer: ExoPlayer? = null
    private var videoPlayer: ExoPlayer? = null

    // private var attraction: Attraction= MockAttractions.list.get(2)


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
        distanceTxt.visibility = View.GONE
        addressDetailsTxt.text = attraction.address

        val phone = attraction.phoneNumber?.takeIf { it.isNotBlank() }
        val email = attraction.email?.takeIf { it.isNotBlank() }


        if (phone != null && email != null) {
            referenceInformationDetailsTxt.text = "$phone\n$email"
        } else if (phone != null) {
            referenceInformationDetailsTxt.text = phone
        } else if (email != null) {
            referenceInformationDetailsTxt.text = email
        } else {

            referenceInformationDetailsTxt.visibility = View.GONE
            referenceInformationTxt.visibility = View.GONE
        }
        if (!attraction.workingHours.isNullOrBlank()){
            workingHoursDetailsTxt.text = attraction.workingHours

        }
        else{
            workingHoursDetailsTxt.visibility = View.GONE
            workingHoursTxt.visibility = View.GONE

        }
        if (attraction.price != null && attraction.price!! > 0) {
            ticketsDetailsTxt.text = "От " + attraction.price + " рублей"
        } else {
            ticketsTxt.visibility= View.GONE
            ticketsDetailsTxt.visibility= View.GONE
            buyTicketBtn.visibility = View.GONE

        }
        infoDetailsTxt.text = attraction.description
    }

    private fun setupGallery(attraction: Attraction) {
        binding.gallery.adapter = AttractionImagesPagerAdapter(attraction.images)
    }

    private fun setupBuyButton(attraction: Attraction) {
        binding.buyTicketBtn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW, attraction.url?.toUri()
            )
            startActivity(intent)
        }
    }

    private fun setupBackButton() {
        binding.backText.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun setupAudio(attraction: Attraction) {
        if (!attraction.audios.isNullOrEmpty()) {
            val url = attraction.audios.get(0)
            if (url.isBlank()) {
                binding.audioPlayerAttraction.visibility = View.GONE
                return
            }
            audioPlayer = ExoPlayer.Builder(requireContext()).build().also { player ->
                binding.audioPlayerAttraction.player = player
                player.setMediaItem(MediaItem.fromUri(url))
                player.prepare()
                player.playWhenReady = false
            }
        } else {
            binding.audioPlayerAttraction.visibility = View.GONE
            return
        }
    }

    private fun setupVideo(attraction: Attraction) {
        if (!attraction.videos.isNullOrEmpty()) {
            val url = attraction.videos.get(0)
            if (url.isBlank()) {
                binding.videoPlayerAttraction.visibility = View.GONE
                return
            }
            videoPlayer = ExoPlayer.Builder(requireContext()).build().also { player ->
                binding.videoPlayerAttraction.player = player
                player.setMediaItem(MediaItem.fromUri(url))
                player.prepare()
                player.playWhenReady = false
            }
        } else {
            binding.videoPlayerAttraction.visibility = View.GONE
            return
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayer?.pause()
        videoPlayer?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        audioPlayer?.release()
        videoPlayer?.release()
        audioPlayer = null
        videoPlayer = null
        _binding = null
    }


}