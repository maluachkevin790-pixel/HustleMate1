package com.example.hustlemate.models

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<Cart> = emptyList(),
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "", // mpesa, paypal, cash
    val status: String = "Pending"
)
