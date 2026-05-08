package com.example.hustlemate.data.repository

import com.example.hustlemate.data.models.Product
import com.google.firebase.firestore.FirebaseFirestore

object WishlistRepository {

    private val db = FirebaseFirestore.getInstance()

    fun add(userId: String, product: Product) {

        val data = hashMapOf(
            "id" to product.id,
            "name" to product.name,
            "price" to product.price,
            "description" to product.description
        )

        db.collection("users")
            .document(userId)
            .collection("wishlist")
            .document(product.id)
            .set(data)
    }

    fun remove(userId: String, productId: String) {

        db.collection("users")
            .document(userId)
            .collection("wishlist")
            .document(productId)
            .delete()
    }

    fun listen(
        userId: String,
        onChange: (List<Product>) -> Unit
    ) {

        db.collection("users")
            .document(userId)
            .collection("wishlist")
            .addSnapshotListener { snapshot, _ ->

                val items = snapshot?.documents?.mapNotNull {

                    Product(
                        id = it.getString("id") ?: "",
                        name = it.getString("name") ?: "",
                        price = it.getDouble("price") ?: 0.0,
                        description = it.getString("description") ?: ""
                    )
                } ?: emptyList()

                onChange(items)
            }
    }
}