package com.example.notesapp2.data.remote.model

data class LoginResponse(
    val message: String,
    val token: String,
    val user: UserResponse
)

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String
)