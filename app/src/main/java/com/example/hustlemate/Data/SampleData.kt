package com.example.hustlemate.data

import com.example.hustlemate.data.models.Product

fun getSampleProducts(): List<Product> {

    return listOf(

        Product(
            id = "1",
            name = "Shoes",
            description = "Nice running shoes",
            price = 2500.0,
            imageUrl = ""
        ),

        Product(
            id = "2",
            name = "Phone",
            description = "Android smartphone",
            price = 15000.0,
            imageUrl = ""
        ),

        Product(
            id = "3",
            name = "Bag",
            description = "Leather bag",
            price = 1200.0,
            imageUrl = ""
        )
    )
}