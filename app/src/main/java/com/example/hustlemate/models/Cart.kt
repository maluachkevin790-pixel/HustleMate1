package com.example.hustlemate.models

import com.example.hustlemate.data.models.Product

data class Cart(
    val product: Product,
    var quantity: Int = 1

)
