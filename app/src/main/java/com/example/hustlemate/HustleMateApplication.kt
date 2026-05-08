package com.example.hustlemate

import android.app.Application
import com.google.firebase.FirebaseApp

class HustleMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 🔧 Initialize Firebase once for the entire app lifecycle
        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
