package com.example.notesapp2.presentation.auth.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notesapp2.R
import com.example.notesapp2.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)

        binding.loginButton.setOnClickListener {

            val email =
                binding.emailEditText.text.toString()

            val password =
                binding.passwordEditText.text.toString()

            viewModel.login(
                email = email,
                password = password
            )
        }

        binding.registerButton.setOnClickListener {

            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment2
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.loginState.collect { state ->

                when (state) {

                    is LoginState.Idle -> {
                        showLoading(false)
                    }

                    is LoginState.Loading -> {
                        showLoading(true)
                    }

                    is LoginState.Success -> {

                        showLoading(false)

                        findNavController().navigate(
                            R.id.action_loginFragment_to_notesListFragment2
                        )
                    }

                    is LoginState.Error -> {

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

        binding.loginProgressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE

        binding.loginButton.isEnabled =
            !isLoading

        binding.registerButton.isEnabled =
            !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}