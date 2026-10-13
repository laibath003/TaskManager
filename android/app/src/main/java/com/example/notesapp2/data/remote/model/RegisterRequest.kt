package com.example.notesapp2.data.remote.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)