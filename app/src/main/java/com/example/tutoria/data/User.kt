package com.example.tutoria.data


data class User(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val imagePath: String = "",
    val userId: String = "",  // Add userId field
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "imagePath" to imagePath,
            "userId" to userId,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt
        )
    }
}

