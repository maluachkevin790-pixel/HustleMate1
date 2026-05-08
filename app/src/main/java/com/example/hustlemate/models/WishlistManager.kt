package com.example.hustlemate.data.models

import androidx.compose.runtime.mutableStateListOf

object WishlistManager {

    private val wishlist = mutableStateListOf<Product>()

    fun getItems(): List<Product> = wishlist

    fun add(product: Product) {
        if (!wishlist.contains(product)) {
            wishlist.add(product)
        }
    }

    fun remove(product: Product) {
        wishlist.remove(product)
    }

    fun clear() {
        wishlist.clear()
    }
}