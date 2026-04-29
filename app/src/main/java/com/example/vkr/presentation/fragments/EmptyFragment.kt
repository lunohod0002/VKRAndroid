package com.example.vkr.presentation.fragments

import android.os.Bundle
import android.view.View

import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.EmptyFragmentBinding


class EmptyFragment : Fragment(R.layout.empty_fragment) {
    private var _binding: EmptyFragmentBinding? = null
    private val binding: EmptyFragmentBinding
        get() = _binding ?: throw RuntimeException()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        _binding = EmptyFragmentBinding.bind(view)
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
        _binding = null
    }


}