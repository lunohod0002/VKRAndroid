package com.example.vkr.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import androidx.activity.addCallback

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ReferenceInfoFragmentBinding


class ReferenceInfoFragment : Fragment(R.layout.fragment_attraction) {
    private var _binding: ReferenceInfoFragmentBinding? = null
    private val binding: ReferenceInfoFragmentBinding
        get() = _binding ?: throw RuntimeException()

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
        _binding = null
    }


}