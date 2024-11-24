package com.example.tutoria

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.tutoria.ui.theme.TutoriaTheme
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true) // Enable offline persistence for Firebase Database
        FirebaseAnalytics.getInstance(this) // Initialize Firebase Analytics

        // Set the content of the activity
        setContent {
            TutoriaTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        // Main screen content goes here
        Surface {
            Button(onClick = {
                // Navigate to AdminActivity when the button is clicked
                startActivity(Intent(this@MainActivity, AdminActivity::class.java))
            }) {
                Text("Go to Admin Panel")
            }
        }
    }
}
