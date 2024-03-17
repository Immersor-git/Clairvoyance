package com.clairvoyance.clairvoyance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate

class DailyView {
    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContent {
                val currentDate = LocalDate.now()
                val currentDateState = remember { mutableStateOf(currentDate) }
                DailyCalendarView(currentDateState)
            }
        }
    }


    @Composable
    fun DailyCalendarView(currentDateState: MutableState<LocalDate>) {
        // Use currentDateState to calculate dates for the current week
        val currentDate = currentDateState.value

        // List of hours in a day
        val hoursOfDay = listOf(
            "12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM", "6:00 AM",
            "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM",
            "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM",
            "9:00 PM", "10:00 PM", "11:00 PM"
        )

        // State to manage the visibility of the enlarged box for the class
        val isClassBoxVisible = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize().background(Color.White)
        ) {
            // Title
            Text(
                text = "Daily Calendar",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )

            // Time slots
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(hoursOfDay.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp), // Fixed height for each row
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Time
                        val (time, period) = hoursOfDay[index].split(" ")
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = time,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = period,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                    // Add a horizontal line between rows
                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            }


        }
    }
}