package com.example.tutoria

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TutoriaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}