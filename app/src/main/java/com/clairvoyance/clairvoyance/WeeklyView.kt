package com.clairvoyance.clairvoyance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeeklyView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentDate = LocalDate.now()
        val currentDateState = mutableStateOf(currentDate)

        setContent {
            WeeklyCalendar(currentDateState)
        }
    }
}

@Composable
fun WeeklyCalendar(currentDateState: MutableState<LocalDate>) {
    // Use currentDateState to calculate dates for the current week
    val currentDate = currentDateState.value
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val dates = (0..6).map { currentDate.plusDays(it.toLong()) }

    val hoursOfDay = listOf(
        "12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM", "6:00 AM",
        "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM",
        "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM",
        "9:00 PM", "10:00 PM", "11:00 PM"
    )

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // Days of the week with dates and time slots
        Row(modifier = Modifier.fillMaxWidth()) {
            // Empty space for time column
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(48.dp)
                    .border(1.dp, Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                // Leave this space empty
            }

            // Days of the week with dates
            for ((dayIndex, day) in daysOfWeek.withIndex()) {
                val date = dates[dayIndex]
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(1.dp, Color.Gray)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = day,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("MM/dd")),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Time slots
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(hoursOfDay.size) { index ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Time
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(48.dp)
                            .border(1.dp, Color.Gray)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = hoursOfDay[index],
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                    // Days of the week
                    for ((dayIndex, _) in daysOfWeek.withIndex()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .border(1.dp, Color.Gray)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            // You can add events or appointments here
                        }
                    }
                }
            }
        }
    }
}