package com.clairvoyance.clairvoyance.HelpItems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clairvoyance.clairvoyance.MainActivity

class NotificationInfo : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = MainActivity()

        setContentView(ComposeView(this).apply {
            setContent {
                Notification()
            }
        })
    }
}

@Composable
fun Title(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun Notification() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Title("Notification Help")

            //Overview Info

            Text(
                text = "Overview",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The app allows you to customize your notification settings, including muting notifications, changing the notification sound, and adjusting the duration of notifications. These settings help you manage how and when you receive notifications from the app.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))

            //Muting

            Text(
                text = "Muting Notifications",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "To mute notifications from the app, follow these steps:\n" +
                        "\n" +
                        "1. Open the app's settings menu.\n" +
                        "2. Tap on \"Notifications.\"\n" +
                        "3. Toggle the \"Mute Notifications\" option to ON.\n" +
                        "4. Notifications from the app will be muted until you toggle this option OFF.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))

            //Sound

            Text(
                text = "Changing the Notification Sound",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "To change the notification sound for the app, follow these steps:\n" +
                        "\n" +
                        "1. Open the app's settings menu.\n" +
                        "2. Tap on Notification Settings\n" +
                        "Tap on Tone\n" +
                        "Select the desired sound from the list of available options.\n" +
                        "The selected sound will be used for notifications from the app.\n",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))

            //Duration

            Text(
                text = "Adjusting Notification Frequency",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Open the app's settings menu.\n" +
                        "Tap on Notification Settings\n" +
                        "Tap on Frequency\n" +
                        "Type in how often before the task is due you would like to be reminded.\n" +
                        "Notifications from the app will be deployed with that interval.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))
            
            
        }
    }
}
