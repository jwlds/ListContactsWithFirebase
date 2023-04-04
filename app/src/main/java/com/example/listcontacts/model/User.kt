package com.example.listcontacts.model



data class User(
    var id: String = "",
    val email: String = "",
    val name: String = "",
    val numberPhone: String = "",
    val imgUrl: String? = null,
)
