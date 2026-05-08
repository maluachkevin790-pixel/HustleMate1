package com.example.hustlemate.navigation

object Routes {

    const val SPLASH = "splash"

    const val LOGIN = "login"

    const val REGISTER = "register"

    const val HOME = "home"

    const val PRODUCT_DETAILS =
        "product_details/{productId}"

    const val CATEGORY_PRODUCTS =
        "category/{categoryName}"

    const val CART = "cart"

    const val CHECKOUT = "checkout"

    const val PAYMENT = "payment"

    const val MPESA_PAYMENT_STATUS = "mpesa_payment_status/{orderId}/{phoneNumber}/{amount}"

    fun mpesaPaymentStatus(orderId: String, phoneNumber: String, amount: Int) = 
        "mpesa_payment_status/$orderId/$phoneNumber/$amount"

    const val ORDER_CONFIRMATION =
        "order_confirmation/{orderId}"

    fun orderConfirmation(orderId: String) = "order_confirmation/$orderId"

    const val MY_ORDERS = "my_orders"

    const val SEARCH = "search"

    const val WISHLIST = "wishlist"

    const val PROFILE = "profile"

    const val EDIT_PROFILE = "edit_profile"

    const val SETTINGS = "settings"

    const val HELP = "help"

    const val SERVICE_AREAS = "service_areas"

    const val SELLER_DASHBOARD =
        "seller_dashboard"

    const val ADD_PRODUCT = "add_product"
}
