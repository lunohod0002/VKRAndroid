package com.example.vkr.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.addCallback

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationBinding
import com.example.vkr.App
import com.example.vkr.presentation.viewmodels.MapViewModel
import com.example.vkr.presentation.viewmodels.StationViewModel
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import kotlin.getValue


class StationFragment : Fragment(R.layout.fragment_station) {

    private  val viewModel: StationViewModel by viewModels { StationViewModel.Factory(requireContext(),(requireActivity().application as App).getDb().cellDao() )}

    private var _binding: FragmentStationBinding? = null
    private val binding: FragmentStationBinding
        get() = _binding ?: throw RuntimeException()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentStationBinding.bind(view)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack(R.id.screen_map, false)
        }
        displayInitData()
        initStationData()
        displayStationData()
            //TODO: подключить загрузку аудио и видео от бекенда
    }
    private fun initStationData() {
         viewModel.getStationInfo(
            name = binding.stationNameTxt.text.toString(),
            branch = binding.branchNameTxt.text.toString()
        )
    }
    private fun displayStationData(){
            viewModel.resultLive.observe(viewLifecycleOwner,{station ->
                if (station != null) {
                    binding.descriptionTextView.text=station.description
                }
            })


    }
    private fun displayInitData() {
        val args: StationFragmentArgs by navArgs()
        binding.stationNameTxt.text = args.STATION.title
        val branchName = when (args.STATION.branchNumber) {
            1 -> "Сокольническая"
            3 -> "Арбатско-Покровская"
            5 -> "Кольцевая"
            9 -> "Серпуховско-Тимирязевская"
            else -> null
        }
        if (branchName != null) {
            binding.branchNameTxt.text = branchName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}