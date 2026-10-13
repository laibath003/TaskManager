package com.example.notesapp2.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp2.data.local.TokenManager
import com.example.notesapp2.data.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState =
        MutableStateFlow<LoginState>(LoginState.Idle)

    val loginState: StateFlow<LoginState> = _loginState

    fun login(
        email: String,
        password: String
    ) {

        if (email.isBlank() || password.isBlank()) {

            _loginState.value =
                LoginState.Error(
                    "Email and password are required"
                )

            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {

            _loginState.value =
                LoginState.Error(
                    "Please enter a valid email address"
                )

            return
        }

        viewModelScope.launch {

            _loginState.value =
                LoginState.Loading

            try {

                val response =
                    repository.login(
                        email = email.trim(),
                        password = password
                    )

                tokenManager.saveToken(
                    response.token
                )

                _loginState.value =
                    LoginState.Success

            } catch (e: Exception) {

                _loginState.value =
                    LoginState.Error(
                        "Login failed. Please check your email and password."
                    )
            }
        }
    }
}

sealed class LoginState {

    object Idle : LoginState()

    object Loading : LoginState()

    object Success : LoginState()

    data class Error(
        val message: String
    ) : LoginState()
}