package com.example.vkr.presentation.fragments

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.vkr.logic.models.Attraction
import com.example.vkr.presentation.adapters.AttractionImagesPagerAdapter
import com.example.vkr.presentation.adapters.StationImagePagerAdapter
import com.example.vkr.presentation.service.AttractionAudioService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttractionFragmentDetails : Fragment() {

    private val viewModel: AttractionViewModel by viewModels ()

    private var _binding: FragmentAttractionDetailsBinding? = null
    private val binding get() = _binding!!

    private var audioUrl: String? = null
    private var videoUrl: String? = null

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
        val audioUrl= attraction.audios?.firstOrNull()?.takeIf { it.isNotBlank() }
        if (audioUrl!=null){
            binding.btnAudioGuideAttraction.setOnClickListener { openAudioGuide(audioUrl) }
        }
        val videoUrl = attraction.videos?.firstOrNull()?.takeIf { it.isNotBlank() }

        binding.btnVideoGuideAttraction.setOnClickListener { openVideoGuide(videoUrl) }
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

    private fun openVideoGuide(videoUrl: String?) {
        if (videoUrl.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Видеогид недоступен", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.openVideoGuide(
            videoUrl = videoUrl
        )

    }
    private fun openAudioGuide(audioUrl : String?) {
        if (audioUrl.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Аудиогид недоступен", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.openAudioGuide(
            audioUrl = audioUrl)

    }



    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}