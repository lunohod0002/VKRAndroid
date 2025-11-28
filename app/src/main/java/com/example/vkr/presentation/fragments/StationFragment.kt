package com.example.vkr.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.addCallback

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStationBinding


class StationFragment : Fragment(R.layout.fragment_station) {
    private var _binding: FragmentStationBinding? = null
    private val binding: FragmentStationBinding
        get() = _binding ?: throw RuntimeException()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentStationBinding.bind(view)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack(R.id.screen_map, false)
        }
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
            //TODO: подключить загрузку аудио и видео от бекенда
    }
        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}