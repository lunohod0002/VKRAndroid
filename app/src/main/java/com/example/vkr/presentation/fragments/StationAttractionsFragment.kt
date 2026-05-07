package com.example.vkr.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationAttractionsBinding
import com.example.vkr.App
import com.example.vkr.logic.viewmodels.StationAttractionsViewModel
import com.example.vkr.logic.viewmodels.StationViewModel
import com.example.vkr.network.dto.AttractionId
import com.example.vkr.network.dto.StationAttractionInfo
import com.example.vkr.presentation.adapters.StationAttractionRecyclerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.getValue


class StationAttractionsFragment : Fragment() {

    private val viewModel: StationAttractionsViewModel by viewModels {
        StationAttractionsViewModel.Factory(requireContext())
    }

    private var _binding: FragmentStationAttractionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: StationAttractionRecyclerAdapter

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
        initStationData()
        displayStationAttractionsData()
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
        viewModel.getStationAttractions(args.STATION.id)

    }
    private fun setupRecyclerView() {
        adapter = StationAttractionRecyclerAdapter { attraction ->
            val action= StationAttractionsFragmentDirections.actionStationAttractionsFragmentToAttractionFragmentDetails(
                ATTRACTION = AttractionId(attraction.id)
            )
            findNavController().navigate(action)
        }
        binding.attractionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.attractionsList.adapter = adapter
    }
    private fun displayStationAttractionsData() {
        viewModel.resultLive.observe(viewLifecycleOwner) { attractions ->
            if (attractions != null) {
                adapter.submitList(attractions)
            }
        }

    }
    //TODO:Сделать фильтрацию в другую сторону при повторном нажатии
    private fun setupFilters() {
        binding.byPriceFilter.setOnClickListener {
            adapter.submitList(adapter.currentList.sortedBy { it.price })
        }
        binding.byDistanceFilter.setOnClickListener {
            adapter.submitList(adapter.currentList.sortedBy { it.distance })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}