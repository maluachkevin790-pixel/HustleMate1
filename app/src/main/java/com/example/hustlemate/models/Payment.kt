package com.example.hustlemate.models

data class Payment(
    val orderId: String = "",
    val method: String = "",
    val status: String = "Pending",
    val transactionId: String? = null
)
