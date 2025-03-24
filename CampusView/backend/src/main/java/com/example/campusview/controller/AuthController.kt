package com.example.campusview.controller

import com.example.campusview.dto.LoginRequest
import com.example.campusview.dto.RegisterRequest
import com.example.campusview.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Map<String, String>> {
        val token = authService.login(loginRequest)
        return ResponseEntity.ok(mapOf("token" to token))
    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<Map<String, String>> {
        authService.register(registerRequest)
        return ResponseEntity.ok(mapOf("message" to "User registered successfully"))
    }
} 