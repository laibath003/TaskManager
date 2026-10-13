package com.example.notesapp2.data.repository

import com.example.notesapp2.data.remote.AuthApiService
import com.example.notesapp2.data.remote.model.LoginRequest
import com.example.notesapp2.data.remote.model.LoginResponse
import com.example.notesapp2.data.remote.model.RegisterRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) {

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse {

        return api.login(
            LoginRequest(
                email = email,
                password = password
            )
        )
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ) {

        api.register(
            RegisterRequest(
                name = name,
                email = email,
                password = password
            )
        )
    }
}