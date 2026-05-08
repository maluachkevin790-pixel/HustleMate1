package com.example.hustlemate.data.models

import androidx.compose.runtime.mutableStateListOf

object CartManager {

    private val items = mutableStateListOf<Product>()

    fun add(product: Product) {
        items.add(product)
    }

    fun getItems(): List<Product> = items

    fun getTotal(): Double = items.sumOf { it.price }

    fun clear() {
        items.clear()
    }
}