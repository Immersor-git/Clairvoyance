package com.clairvoyance.clairvoyance.HelpItems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clairvoyance.clairvoyance.MainActivity

class ThemeInfo : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = MainActivity()

        setContentView(ComposeView(this).apply {
            setContent {
                Theme()
            }
        })
    }
}



@Composable
fun Theme() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Title("Theme Help")

            Text(
                text = "Overview ",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The app allows you to change the theme to personalize your experience. You can choose from different themes to customize the look and feel of the app.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))

            //Theme settings

            Text(
                text = "Changing Themes",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "To change the theme, follow these steps:\n" +
                        "\n" +
                        "Open the app and navigate to the settings page.\n" +
                        "Look for the theme settings \n" +
                        "Select the theme you prefer from the list of available options\n" +
                        "The app will apply the selected theme, and you will see the changes immediately.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}