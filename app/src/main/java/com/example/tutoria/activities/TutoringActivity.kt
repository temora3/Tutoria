package com.example.tutoria.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.tutoria.R

class TutoringActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }

    // Load Lottie animations for each icon
    val compositions = listOf(
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.system_regular_41_home_morph_home_2)),
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.system_regular_42_search_in_search)),
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.system_regular_47_chat_in_chat)),
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.system_regular_8_account_in_account))
    )

    // Progress states for animations
    val progressStates = compositions.mapIndexed { index, composition ->
        animateLottieCompositionAsState(
            composition = composition.value,
            isPlaying = (selectedItem == index),
            iterations = 1,
            speed = 1f,
            restartOnPlay = true
        )
    }

    // Static icons as fallback images
    val staticIcons = listOf(
        R.drawable.system_regular_41_home_morph_home_2, // Replace these with your actual drawable resources
        R.drawable.system_regular_42_search_hover_search,
        R.drawable.system_regular_47_chat_hover_chat,
        R.drawable.system_regular_8_account_hover_account
    )

    val titles = listOf("Home", "Search", "Chat", "Profile")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White
            ) {
                compositions.forEachIndexed { index, composition ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = {
                            Box(modifier = Modifier.size(24.dp)) {
                                if (selectedItem == index && composition.value != null) {
                                    // Show animation when selected
                                    LottieAnimation(
                                        composition = composition.value,
                                        progress = { progressStates[index].value }
                                    )
                                } else {
                                    // Show static icon when not selected
                                    Icon(
                                        painter = painterResource(id = staticIcons[index]),
                                        contentDescription = titles[index]
                                    )
                                }
                            }
                        },
                        label = { Text(titles[index]) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem) {
                0 -> Text("Home Screen")
                1 -> Text("Search Screen")
                2 -> Text("Chat Screen")
                3 -> Text("Profile Screen")
            }
        }
    }
}
