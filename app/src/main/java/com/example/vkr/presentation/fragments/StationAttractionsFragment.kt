package com.example.vkr.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentStationAttractionsBinding
import com.example.vkr.network.dto.StationAttractionResponse
import com.example.vkr.presentation.adapters.StationAttractionRecyclerAdapter


class StationAttractionsFragment : Fragment() {

    private var _binding: FragmentStationAttractionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StationAttractionRecyclerAdapter
    private var attractions: List<StationAttractionResponse> = emptyList()

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

        binding.backText.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        adapter = StationAttractionRecyclerAdapter { attraction ->
            // обработка клика по карточке
        }
        binding.attractionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.attractionsList.adapter = adapter
    }

    private fun loadData() {
        attractions = listOf(
            StationAttractionResponse(1, "Зоопарк", 390, "https://s0.rbk.ru/v6_top_pics/media/img/1/14/756594550679141.webp",1500),
            StationAttractionResponse(2, "Музей космонавтики", 850, "https://cdn.iz.ru/sites/default/files/news-2018-12/2880px-Colosseum_in_Rome%2C_Italy_-_April_2007.jpg",800),
            StationAttractionResponse(3, "Парк Горького", 210, "https://safety-rest.ru/upload/iblock/655/655861e57c7196758fe81b8c0f19a436.jpg",0),
            StationAttractionResponse(4, "Третьяковская галерея", 2100,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwyIS3lgTauIU1J_3ECsDxnqF8jyaIlcBQTg&s", 1200),
                    StationAttractionResponse(5, "Бащня", 2100,"https://depositphotos-blog.s3.eu-west-1.amazonaws.com/uploads/2017/07/Depositphotos_5593372_m-2015.jpg", 500)

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