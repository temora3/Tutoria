package com.example.tutoria.util

sealed class RegisterValidation {
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}

// ValidateEmail.kt
class ValidateEmail(private val email: String) {
    fun isValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

// ValidatePassword.kt
class ValidatePassword(private val password: String) {
    fun isValid(): Boolean {
        return password.length >= 6
    }
}