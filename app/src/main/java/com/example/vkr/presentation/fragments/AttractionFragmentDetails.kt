package com.example.vkr.presentation.fragments

import android.os.Bundle
import android.view.View

import androidx.fragment.app.Fragment
import com.example.myapplication.R


class AttractionFragmentDetails : Fragment(R.layout.fragment_attraction) {
//    private var _binding: StationFragmentBinding? = null
//    private val binding: StationFragmentBinding
//        get() = _binding ?: throw RuntimeException()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        _binding = OnboardFragmentBinding.bind(view)
//
//
//        binding.btnSignIn.setOnClickListener {
//            findNavController().navigate(R.id.action_screen_on_board_to_login)
//
//        }
//
//        binding.btnSignUp.setOnClickListener {
//            findNavController().navigate(R.id.action_screen_on_board_to_register)
//
//        }
//        binding.btnSettings.setOnClickListener {
//            findNavController().navigate(R.id.action_screen_onboard_to_settingsFragment)
//
//        }
//
    }
    override fun onDestroyView() {
        super.onDestroyView()
       // _binding = null
    }


}