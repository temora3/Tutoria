package com.example.tutoria.data

data class SpecialTutor (
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val description: String? = null,
    val image: List<String>
){
    constructor(): this("0","","",0f, image = emptyList())
}
