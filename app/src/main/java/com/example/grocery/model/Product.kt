package com.example.grocery.model

data class Product(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val originalPrice: Double,
    val discount: Int,
    val rating: Float,
    val isInStock: Boolean
)