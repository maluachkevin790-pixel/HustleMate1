package com.example.hustlemate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.hustlemate.models.Cart
import com.example.hustlemate.data.models.Product

class CartViewModel : ViewModel() {

    // 🔥 Shared cart state
    private val _cartItems = mutableStateListOf<Cart>()
    val cartItems: List<Cart> = _cartItems

    // 🛒 ADD TO CART
    fun addToCart(product: Product) {

        val existingItem = _cartItems.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity += 1
        } else {
            _cartItems.add(Cart(product = product, quantity = 1))
        }
    }

    // ❌ REMOVE ITEM
    fun removeFromCart(productId: String) {
        _cartItems.removeAll { it.product.id == productId }
    }

    // ➕ INCREASE
    fun increaseQty(productId: String) {
        _cartItems.find { it.product.id == productId }?.let {
            it.quantity += 1
        }
    }

    // ➖ DECREASE
    fun decreaseQty(productId: String) {
        val item = _cartItems.find { it.product.id == productId }

        item?.let {
            if (it.quantity > 1) {
                it.quantity -= 1
            } else {
                removeFromCart(productId)
            }
        }
    }

    // 💰 TOTAL
    fun getTotal(): Double {
        return _cartItems.sumOf {
            it.product.price * it.quantity
        }
    }

    // 🧹 CLEAR CART
    fun clearCart() {
        _cartItems.clear()
    }
}