package com.example.notesapp2.presentation.auth.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp2.data.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl
) : ViewModel() {

    private val _registerState =
        MutableStateFlow<RegisterState>(
            RegisterState.Idle
        )

    val registerState: StateFlow<RegisterState> =
        _registerState

    fun register(
        name: String,
        email: String,
        password: String
    ) {

        if (
            name.isBlank() ||
            email.isBlank() ||
            password.isBlank()
        ) {

            _registerState.value =
                RegisterState.Error(
                    "All fields are required"
                )

            return
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {

            _registerState.value =
                RegisterState.Error(
                    "Please enter a valid email address"
                )

            return
        }

        if (password.length < 6) {

            _registerState.value =
                RegisterState.Error(
                    "Password must be at least 6 characters"
                )

            return
        }

        viewModelScope.launch {

            _registerState.value =
                RegisterState.Loading

            try {

                repository.register(
                    name = name.trim(),
                    email = email.trim(),
                    password = password
                )

                _registerState.value =
                    RegisterState.Success

            } catch (e: Exception) {

                _registerState.value =
                    RegisterState.Error(
                        "Registration failed. The email may already be registered."
                    )
            }
        }
    }
}

sealed class RegisterState {

    object Idle : RegisterState()

    object Loading : RegisterState()

    object Success : RegisterState()

    data class Error(
        val message: String
    ) : RegisterState()
}