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

class CalendarInfo : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = MainActivity()

        setContentView(ComposeView(this).apply {
            setContent {
                Cal()
            }
        })
    }
}



@Composable
fun Cal() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Title("Calendar Help")

            Text(
                text = "Overview",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The app provides three different calendar views to help you manage and visualize your tasks. Each view offers a unique perspective on your schedule, making it easier to stay organized and on track.",
                color = Color.White,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Monthly View",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The Monthly View displays a traditional calendar layout, showing an entire month at a glance. Each day is represented by a cell, and tasks are indicated on the respective dates. This view is ideal for getting a broad overview of your upcoming tasks and commitments.",
                color = Color.White,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Weekly View",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The Weekly View provides a more detailed look at your schedule, showing a week's worth of tasks in a vertical layout. Each day is divided into time slots, and tasks are displayed chronologically. This view is useful for planning your week and managing your time effectively.",
                color = Color.White,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Daily View",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The Daily View focuses on a single day, showing all tasks scheduled for that day in chronological order. This view provides a detailed look at your daily schedule, helping you stay organized and focused on your tasks for the day.",
                color = Color.White,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Switching Between Views",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "You can easily switch between calendar views by tapping on the corresponding option in the app's menu. Each view offers a unique perspective on your tasks, allowing you to choose the one that best suits your needs at any given time.",
                color = Color.White,
                fontSize = 20.sp,
            )
        }
    }
}

