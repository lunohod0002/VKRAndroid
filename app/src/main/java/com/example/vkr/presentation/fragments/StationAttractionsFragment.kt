import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentAttractionDetailsBinding
import com.example.vkr.network.dto.Attraction
import com.example.vkr.presentation.adapters.AttractionImagesPagerAdapter

class AttractionFragmentDetails : Fragment() {
    //TODO: Добавить nav args
    private var _binding: FragmentAttractionDetailsBinding? = null
    private val binding get() = _binding!!

    private var audioPlayer: ExoPlayer? = null
    private var videoPlayer: ExoPlayer? = null

    private lateinit var attraction: Attraction


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
        bindStaticData()
        setupGallery()
        setupBackButton()
        setupBuyButton()
        setupAudio()
        setupVideo()
    }
    private fun bindStaticData() = with(binding) {
        attractionNameTxt.text = attraction.name
        // distance в новом DTO нет — либо убери TextView, либо считай отдельно
        distanceTxt.visibility = View.GONE
        addressDetailsTxt.text = attraction.address
        referenceInformationDetailsTxt.text =
            "${attraction.phoneNumber}\n${attraction.email}"
        workingHoursDetailsTxt.text = attraction.workingHours
        ticketsDetailsTxt.text = attraction.price ?: "Бесплатно"
        infoDetailsTxt.text = attraction.description
    }

    private fun setupGallery() {
        binding.gallery.adapter = AttractionImagesPagerAdapter(attraction.images)
    }

    private fun setupBuyButton() {
        binding.buyTicketBtn.setOnClickListener {
            val intent = android.content.Intent(
                android.content.Intent.ACTION_VIEW, attraction.urlRef.toUri()
            )
            startActivity(intent)
        }
    }

    private fun setupBackButton() {
        binding.backText.setOnClickListener {
            findNavController().navigateUp()
        }
    }



    private fun setupAudio() {
        val url = attraction.audioUrl
        if (url.isNullOrBlank()) {
            binding.audioPlayerAttraction.visibility = View.GONE
            return
        }
        audioPlayer = ExoPlayer.Builder(requireContext()).build().also { player ->
            binding.audioPlayerAttraction.player = player
            player.setMediaItem(MediaItem.fromUri(url))
            player.prepare()
            player.playWhenReady = false
        }
    }

    private fun setupVideo() {
        val url = attraction.videoUrl
        if (url.isNullOrBlank()) {
            binding.videoPlayerAttraction.visibility = View.GONE
            return
        }
        videoPlayer = ExoPlayer.Builder(requireContext()).build().also { player ->
            binding.videoPlayerAttraction.player = player
            player.setMediaItem(MediaItem.fromUri(url))
            player.prepare()
            player.playWhenReady = false
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