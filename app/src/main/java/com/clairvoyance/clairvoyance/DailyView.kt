package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

class DailyView : Fragment() {
    private lateinit var mainActivity : MainActivity;
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater { //Applies theme on loading
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        mainActivity = getActivity() as MainActivity
        val contextThemeWrapper: Context = ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
        return inflater.cloneInContext(contextThemeWrapper)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_weekly_view, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.composeView)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val currentDate = LocalDate.now()
                val currentDateState = remember { mutableStateOf(currentDate) }

                DailyCalendarView(currentDateState)
            }
        }

        return view
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
                    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                }
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

    enum class CalendarView {
        WEEKLY, DAILY, MONTHLY
    }

    enum class Weekday {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }
    class MonthlyView {
        @Composable
        fun MonthlyCalendarView(currentDateState: MutableState<LocalDate>) {
            val currentDate = currentDateState.value
            val currentMonth = currentDate.month
            val firstDayOfMonth = currentDate.withDayOfMonth(1)
            val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
            val startOffset = firstDayOfMonth.dayOfWeek.value % 7
            val totalDays = lastDayOfMonth.dayOfMonth
            val totalRows = ceil((totalDays + startOffset) / 7.0).toInt()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Row for the button and the month text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start, // Aligns items to the start of the row
                    verticalAlignment = Alignment.CenterVertically // Aligns items vertically at the center
                ) {
                    // Button to go to the current month
                    Button(
                        onClick = {
                            // Set the day of the current month
                            currentDateState.value =
                                LocalDate.now().withDayOfMonth(currentDate.dayOfMonth)
                        },
                        modifier = Modifier.padding(start = 16.dp) // Adjust padding as needed
                    ) {
                        Text(
                            text = LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd")),
                        )
                    }

                    // Title: Month and Year
                    Text(
                        text = currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }

                // Days of the week
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (day in Weekday.values()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.name.take(3),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Calendar grid
                for (row in 0 until totalRows) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (col in 0 until 7) {
                            val dayOfMonth = row * 7 + col - startOffset + 1
                            val date = firstDayOfMonth.plusDays((dayOfMonth - 1).toLong())

                            val isCurrentMonth = date.month == currentMonth
                            val isCurrentDay =
                                isCurrentMonth && date.dayOfMonth == 19 && date.month == Month.MARCH

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .background(
                                        if (isCurrentDay) Color.Cyan else if (isCurrentMonth) Color.White else Color.Transparent
                                    )
                                    .clickable(enabled = isCurrentMonth) {
                                        currentDateState.value = date
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (dayOfMonth > 0 && dayOfMonth <= totalDays) dayOfMonth.toString() else "",
                                    style = MaterialTheme.typography.bodySmall.copy(color = if (isCurrentMonth) Color.Black else Color.LightGray),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Navigation buttons: Previous Month and Next Month
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { currentDateState.value = currentDate.minusMonths(1) },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Previous Month")
                    }

                    Button(
                        onClick = { currentDateState.value = currentDate.plusMonths(1) },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Next Month")
                    }
                }
            }
        }
    }
}