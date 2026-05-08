package com.example.hustlemate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import com.example.hustlemate.ui.theme.Customers.*
import com.example.hustlemate.ui.theme.auth.*
import com.example.hustlemate.ui.theme.core.SplashScreen
import com.example.hustlemate.ui.theme.Selling.*
import com.example.hustlemate.ui.theme.profille.*

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        // 🔐 AUTH
        composable(Routes.SPLASH) { SplashScreen(navController) }
        composable(Routes.LOGIN) { LoginScreen(navController) }
        composable(Routes.REGISTER) { RegisterScreen(navController) }

        // 🏠 MAIN
        composable(Routes.HOME) { HomeScreen(navController) }

        // 🛍️ PRODUCT
        composable(
            route = Routes.PRODUCT_DETAILS,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { 
            ProductDetailsScreen(navController)
        }

        // 🛒 CART FLOW
        composable(Routes.CART) { CartScreen(navController) }
        composable(Routes.CHECKOUT) { CheckoutScreen(navController) }
        composable(Routes.PAYMENT) { PaymentScreen(navController) }

        // 💳 MPESA STATUS
        composable(
            route = Routes.MPESA_PAYMENT_STATUS,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("amount") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            val amount = backStackEntry.arguments?.getInt("amount") ?: 0
            MpesaPaymentStatusScreen(navController, orderId, phoneNumber, amount)
        }

        // 💳 ORDER CONFIRMATION (IMPORTANT)
        composable(
            route = Routes.ORDER_CONFIRMATION,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->

            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderConfirmationScreen(orderId, navController)
        }

        // 📦 ORDERS
        composable(Routes.MY_ORDERS) { MyOrdersScreen(navController) }

        // 🔍 SEARCH + WISHLIST
        composable(Routes.SEARCH) { SearchScreen(navController) }
        composable(Routes.WISHLIST) { WishlistScreen(navController) }

        // 👤 PROFILE
        composable(Routes.PROFILE) { ProfileScreen(navController) }
        composable(Routes.EDIT_PROFILE) { EditProfileScreen(navController) }
        composable(Routes.SETTINGS) { SettingsScreen(navController) }
        composable(Routes.HELP) { HelpCenterScreen(navController) }
        composable(Routes.SERVICE_AREAS) { ServiceAreasScreen(navController) }

        // 🧑‍💼 SELLER
        composable(Routes.ADD_PRODUCT) { AddProductScreen(navController) }
        composable(Routes.SELLER_DASHBOARD) { SellerDashboardScreen(navController) }

        composable(
            "category/{categoryName}"
        ) { backStackEntry ->

            val categoryName =
                backStackEntry.arguments
                    ?.getString("categoryName")
                    ?: ""

            CategoryProductsScreen(
                navController,
                categoryName
            )
        }
    }
}
