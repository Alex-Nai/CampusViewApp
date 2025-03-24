package com.example.campusview.service

import com.example.campusview.dto.LoginRequest
import com.example.campusview.dto.RegisterRequest
import com.example.campusview.entity.User
import com.example.campusview.repository.UserRepository
import com.example.campusview.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: JwtTokenProvider
) {

    @Transactional
    fun register(request: RegisterRequest) {
        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException("Username is already taken")
        }

        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            email = request.email
        )
        userRepository.save(user)
    }

    fun login(request: LoginRequest): String {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        return tokenProvider.generateToken(authentication)
    }
} 