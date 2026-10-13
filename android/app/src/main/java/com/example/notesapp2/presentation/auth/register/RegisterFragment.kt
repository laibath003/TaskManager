package com.example.notesapp2.presentation.auth.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notesapp2.R
import com.example.notesapp2.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRegisterBinding.bind(view)

        binding.registerButton.setOnClickListener {

            val name =
                binding.nameEditText.text.toString()

            val email =
                binding.emailEditText.text.toString()

            val password =
                binding.passwordEditText.text.toString()

            viewModel.register(
                name = name,
                email = email,
                password = password
            )
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerFragment_to_loginFragment2
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.registerState.collect { state ->

                when (state) {

                    is RegisterState.Idle -> {
                        showLoading(false)
                    }

                    is RegisterState.Loading -> {
                        showLoading(true)
                    }

                    is RegisterState.Success -> {

                        showLoading(false)

                        Toast.makeText(
                            requireContext(),
                            "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().navigate(
                            R.id.action_registerFragment_to_loginFragment2
                        )
                    }

                    is RegisterState.Error -> {

                        showLoading(false)

                        Toast.makeText(
                            requireContext(),
                            state.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {

        binding.registerProgressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.registerButton.isEnabled =
            !isLoading

        binding.loginButton.isEnabled =
            !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}