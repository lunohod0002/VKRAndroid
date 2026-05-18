package com.example.vkr.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.vkr.logic.viewmodels.LoginViewModel
import com.example.vkr.presentation.fragments.MapFragmentDirections
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            viewModel.login(
                binding.loginEditTextTxt.text.toString(),
                binding.passwordEditTextTxt.text.toString()
            )
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginViewModel.State.Error -> renderError(state.message)
                is LoginViewModel.State.Success -> {
                    val action= LoginFragmentDirections.actionLoginFragmentToAddAttractionFragment(
                    )
                    findNavController().navigate(action)

                }
            }
        }
    }





    private fun renderError(message: String) {
        val snackbar = Snackbar.make(binding.root, "Не удалось выполнить запрос, ошибка: $message", Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}