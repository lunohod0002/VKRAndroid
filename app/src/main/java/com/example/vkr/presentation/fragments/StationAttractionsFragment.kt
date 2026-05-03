package com.example.vkr.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationAttractionsBinding
import com.example.vkr.App
import com.example.vkr.logic.viewmodels.StationAttractionsViewModel
import com.example.vkr.logic.viewmodels.StationViewModel
import com.example.vkr.network.dto.StationAttractionInfo
import com.example.vkr.presentation.adapters.StationAttractionRecyclerAdapter
import kotlin.getValue


class StationAttractionsFragment : Fragment() {

    private val viewModel: StationAttractionsViewModel by viewModels {
        StationAttractionsViewModel.Factory()
    }

    private var _binding: FragmentStationAttractionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StationAttractionRecyclerAdapter
    private var attractions: List<StationAttractionInfo> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationAttractionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadData()
        setupFilters()
        displayStationLogo()
        displayStationData()

        binding.backText.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
    private fun displayStationData() {
        val args: StationAttractionsFragmentArgs by navArgs()


        val stationName = args.STATION.title
        val branchName = when (args.STATION.branchNumber) {
            1 -> "Сокольническая"
            3 -> "Арбатско-Покровская"
            5 -> "Кольцевая"
            9 -> "Серпуховско-Тимирязевская"
            else -> ""
        }
        binding.stationAttractionStationNameTxt.text = stationName
        binding.stationAttractionBranchNameTxt.text = branchName
    }
    private fun displayStationLogo(){
            val args: StationAttractionsFragmentArgs by navArgs()

            val branchNumber = args.STATION.branchNumber
        val iconResId = when (branchNumber) {
            1 -> R.drawable.red_branch_logo
            9 -> R.drawable.gray_branch_logo
            3 -> R.drawable.blue_branch_logo
            5 -> R.drawable.brown_branch_logo
            else -> 0
        }

        if (iconResId != 0) {
            binding.stationAttractionBranchLogo.setImageResource(iconResId)
        } else {
            binding.stationAttractionBranchLogo.setImageResource(0)
        }
    }
    private fun initStationData() {
        val args: StationAttractionsFragmentArgs by navArgs()
        val stationName = args.STATION.title
        val branchName = when (args.STATION.branchNumber) {
            1 -> "Сокольническая"
            3 -> "Арбатско-Покровская"
            5 -> "Кольцевая"
            9 -> "Серпуховско-Тимирязевская"
            else -> ""
        }

        viewModel.getStationAttractions(args.STATION.id)

    }
    private fun setupRecyclerView() {
        adapter = StationAttractionRecyclerAdapter { attraction ->
            // обработка клика по карточке
        }
        binding.attractionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.attractionsList.adapter = adapter
    }
    private fun displayStationAttractionsData() {
        viewModel.resultLive.observe(viewLifecycleOwner) { station ->
            if (station != null) {
            }
        }
    }
                private fun loadData() {
        attractions = listOf(
            StationAttractionInfo(1, "Зоопарк", 390, "https://s0.rbk.ru/v6_top_pics/media/img/1/14/756594550679141.webp",1500),
            StationAttractionInfo(2, "Музей космонавтики", 850, "https://cdn.iz.ru/sites/default/files/news-2018-12/2880px-Colosseum_in_Rome%2C_Italy_-_April_2007.jpg",800),
            StationAttractionInfo(3, "Парк Горького", 210, "https://safety-rest.ru/upload/iblock/655/655861e57c7196758fe81b8c0f19a436.jpg",0),
            StationAttractionInfo(4, "Третьяковская галерея", 2100,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwyIS3lgTauIU1J_3ECsDxnqF8jyaIlcBQTg&s", 1200),
                    StationAttractionInfo(5, "Бащня", 2100,"https://depositphotos-blog.s3.eu-west-1.amazonaws.com/uploads/2017/07/Depositphotos_5593372_m-2015.jpg", 500)

        )
        adapter.submitList(attractions)
    }
    //TODO:Сделать фильтрацию в другую сторону при повторном нажатии
    private fun setupFilters() {
        binding.byPriceFilter.setOnClickListener {
            adapter.submitList(attractions.sortedBy { it.price })
        }
        binding.byDistanceFilter.setOnClickListener {
            adapter.submitList(attractions.sortedBy { it.distance })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}