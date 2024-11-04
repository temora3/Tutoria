package com.example.tutoria.activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.compose.*
import com.example.tutoria.R
import com.example.tutoria.fragments.tutoring.ChatFragment
import com.example.tutoria.fragments.tutoring.HomeFragment
import com.example.tutoria.fragments.tutoring.ProfileFragment
import com.example.tutoria.fragments.tutoring.SearchFragment

class TutoringActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(supportFragmentManager)
        }
    }
}

@Composable
fun MainScreen(supportFragmentManager: FragmentManager) {
    var selectedItem by remember { mutableStateOf(0) }

    // Load Lottie animations for each icon
    val compositions = listOf(
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.system_regular_41_home_morph_home_2)),
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.system_regular_42_search_in_search)),
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.system_regular_47_chat_in_chat)),
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.system_regular_8_account_in_account))
    )

    val progressStates = compositions.mapIndexed { index, composition ->
        animateLottieCompositionAsState(
            composition = composition.value,
            isPlaying = (selectedItem == index),
            iterations = 1,
            speed = 1f,
            restartOnPlay = true
        )
    }

    val staticIcons = listOf(
        R.drawable.system_regular_41_home_morph_home_2,
        R.drawable.system_regular_42_search_hover_search,
        R.drawable.system_regular_47_chat_hover_chat,
        R.drawable.system_regular_8_account_hover_account
    )

    val customFontFamily = FontFamily(
        Font(R.font.opensans, weight = FontWeight.Normal)
    )

    val titles = listOf("Home", "Search", "Chat", "Profile")

    fun showFragment(index: Int) {
        val fragment = when (index) {
            0 -> HomeFragment()
            1 -> SearchFragment()
            2 -> ChatFragment()
            3 -> ProfileFragment()
            else -> HomeFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    LaunchedEffect(selectedItem) {
        showFragment(selectedItem)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Box {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.hsl(180.79F, 0.5588F, 0.7333F)
                ) {
                    compositions.forEachIndexed { index, composition ->
                        NavigationBarItem(
                            selected = selectedItem == index,
                            onClick = { selectedItem = index },
                            icon = {
                                Box(modifier = Modifier.size(24.dp)) {
                                    if (selectedItem == index && composition.value != null) {
                                        LottieAnimation(
                                            composition = composition.value,
                                            progress = { progressStates[index].value }
                                        )
                                    } else {
                                        Icon(
                                            painter = painterResource(id = staticIcons[index]),
                                            contentDescription = titles[index]
                                        )
                                    }
                                }
                            },
                            label = {
                                Text(
                                    titles[index],
                                    style = TextStyle(
                                        fontFamily = customFontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Placeholder for the fragment container
            androidx.compose.ui.viewinterop.AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    FrameLayout(context).apply { id = R.id.fragmentContainer }
                }
            )
        }
    }
}
