package com.example.hustlemate.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {

    object Home : BottomNavItem(
        route = Routes.HOME,
        label = "Home",
        icon = Icons.Default.Home
    )

    object Cart : BottomNavItem(
        route = Routes.CART,
        label = "Cart",
        icon = Icons.Default.ShoppingCart
    )

    object Orders : BottomNavItem(
        route = Routes.MY_ORDERS,   // ✅ MUST match
        label = "Orders",
        icon = Icons.Default.List
    )

    object Profile : BottomNavItem(
        route = Routes.PROFILE,     // ✅ MUST match
        label = "Profile",
        icon = Icons.Default.Person
    )

    object Seller : BottomNavItem(
        route = Routes.SELLER_DASHBOARD, // ✅ MUST match
        label = "Seller",
        icon = Icons.Default.Star
    )
}