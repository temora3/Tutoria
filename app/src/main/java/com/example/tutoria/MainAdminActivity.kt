package com.example.tutoria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tutoria.activities.TutoringActivity
import com.example.tutoria.ui.theme.TutoriaTheme

<<<<<<< HEAD:app/src/main/java/com/example/tutoria/MainAdminActivity.kt
class MainAdminActivity : ComponentActivity() {

=======
class MainActivity : ComponentActivity() {
>>>>>>> de399db00d0973142c70c3f2c6a4a2aad2d48774:app/src/main/java/com/example/tutoria/MainActivity.kt
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TutoriaTheme {
<<<<<<< HEAD:app/src/main/java/com/example/tutoria/MainAdminActivity.kt
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
                startActivity(Intent(this@MainAdminActivity, AdminActivity::class.java))
            }) {
                Text("Go to Admin Panel")
=======
                Surface {
                    Modifier.fillMaxSize()
                    MaterialTheme.colorScheme.background
                    {
                        TutoringActivity()
                    }
                }
>>>>>>> de399db00d0973142c70c3f2c6a4a2aad2d48774:app/src/main/java/com/example/tutoria/MainActivity.kt
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TutoriaTheme{TutoringActivity()}
}

