package com.example.hustlemate.network

import android.util.Base64
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MpesaRepository {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(MpesaConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val api = retrofit.create(MpesaApi::class.java)

    suspend fun initiateSTKPush(phoneNumber: String, amount: Int): STKPushResponse? {
        try {
            // 1. Get Access Token
            val auth = Base64.encodeToString(
                "${MpesaConfig.CONSUMER_KEY}:${MpesaConfig.CONSUMER_SECRET}".toByteArray(),
                Base64.NO_WRAP
            )
            val tokenResponse = api.getAccessToken("Basic $auth")
            val accessToken = tokenResponse.body()?.accessToken ?: return null

            // 2. Prepare STK Push Request
            val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
            val password = Base64.encodeToString(
                "${MpesaConfig.BUSINESS_SHORT_CODE}${MpesaConfig.PASSKEY}$timestamp".toByteArray(),
                Base64.NO_WRAP
            )

            val request = STKPushRequest(
                businessShortCode = MpesaConfig.BUSINESS_SHORT_CODE,
                password = password,
                timestamp = timestamp,
                amount = amount,
                partyA = phoneNumber, // User's phone number
                partyB = MpesaConfig.BUSINESS_SHORT_CODE,
                phoneNumber = phoneNumber,
                callBackURL = MpesaConfig.CALLBACK_URL,
                accountReference = "HustleMate Order",
                transactionDesc = "Payment for goods"
            )

            val response = api.sendSTKPush("Bearer $accessToken", request)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
