package com.example.notesapp2.data.remote

import com.example.notesapp2.data.remote.model.LoginRequest
import com.example.notesapp2.data.remote.model.LoginResponse
import com.example.notesapp2.data.remote.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    )
}