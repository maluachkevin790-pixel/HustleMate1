package com.example.hustlemate.network

object MpesaConfig {
    const val BASE_URL = "https://sandbox.safaricom.co.ke/" // Use https://api.safaricom.co.ke/ for production
    
    // Replace these with your Daraja App credentials
    const val CONSUMER_KEY = "TcAaB6WYgtyVa7kIwvaFYHAKG0nrBEjVCpc0RGj0QURKGYvk"
    const val CONSUMER_SECRET = "C3nfOmwN7juaKYjTUxiyFMDaXoloJlPtZWqbX1VGFuCBNBYkTCzYVvGBet0DuRI2"
    const val BUSINESS_SHORT_CODE = "174379" // Default sandbox shortcode
    const val PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919" // Default sandbox passkey
    const val CALLBACK_URL = "https://your-callback-url.com/callback"
}
