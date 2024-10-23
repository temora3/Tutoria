package com.example.tutoria.activities

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.tutoria.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class tutoringActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutoring)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Setup Lottie icons for each menu item
        bottomNavigationView.menu?.let { menu ->
            val homeMenuItem = menu.findItem(R.id.homeFragment)
            homeMenuItem.actionView = createLottieView(R.raw.system_regular_41_home_morph_home_2)  // Replace with your Lottie JSON

            val searchMenuItem = menu.findItem(R.id.searchFragment)
            searchMenuItem.actionView = createLottieView(R.raw.system_regular_42_search_in_search)  // Replace with your Lottie JSON

            val chatMenuItem = menu.findItem(R.id.chatFragment)
            chatMenuItem.actionView = createLottieView(R.raw.system_regular_47_chat_in_chat)  // Replace with your Lottie JSON

            val profileMenuItem = menu.findItem(R.id.profileFragment)
            profileMenuItem.actionView = createLottieView(R.raw.system_regular_8_account_in_account)  // Replace with your Lottie JSON
        }
    }

    // Helper function to create a LottieAnimationView
    private fun createLottieView(animationRes: Int): LottieAnimationView {
        val lottieView = LottieAnimationView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setAnimation(animationRes)
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
        return lottieView
    }
}