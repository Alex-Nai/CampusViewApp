package com.example.campusview.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String?
)

data class JwtResponse(
    val token: String,
    val type: String = "Bearer",
    val username: String,
    val roles: Set<String>
) 