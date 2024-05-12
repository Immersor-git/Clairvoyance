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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import kotlin.math.ceil

class CalendarViewFragment : Fragment() {
    private lateinit var mainActivity: MainActivity;
    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater { //Applies theme on loading
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        mainActivity = getActivity() as MainActivity
        val contextThemeWrapper: Context =
            ContextThemeWrapper(requireContext(), mainActivity.getCustomTheme())
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
                // State to hold the current view of the calendar
                var currentView by remember { mutableStateOf(CalendarView.MONTHLY) }

                // Display the appropriate view based on the current view state
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        when (currentView) {
                            CalendarView.DAILY -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween // Aligns items to the start and end of the row
                                ) {
                                    Button(
                                        onClick = { currentView = CalendarView.WEEKLY },
                                        modifier = Modifier.padding(start = 16.dp) // Adjust padding as needed
                                    ) {
                                        Text("Weekly View")
                                    }

                                    Button(
                                        onClick = { currentView = CalendarView.MONTHLY },
                                        modifier = Modifier.padding(end = 16.dp) // Adjust padding as needed
                                    ) {
                                        Text("Monthly View")
                                    }
                                }
                            }
                            CalendarView.WEEKLY -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween // Aligns items to the start and end of the row
                                ) {
                                    Button(
                                        onClick = { currentView = CalendarView.DAILY },
                                        modifier = Modifier.padding(start = 16.dp) // Adjust padding as needed
                                    ) {
                                        Text("Daily View")
                                    }

                                    Button(
                                        onClick = { currentView = CalendarView.MONTHLY },
                                        modifier = Modifier.padding(end = 16.dp) // Adjust padding as needed
                                    ) {
                                        Text("Monthly View")
                                    }
                                }
                            }
                            CalendarView.MONTHLY -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween // Aligns items to the start and end of the row
                                ) {
                                    Button(
                                        onClick = { currentView = CalendarView.DAILY },
                                        modifier = Modifier.padding(start = 16.dp) // Adjust padding as needed
                                    ) {
                                        Text("Daily View")
                                    }

                                    Button(
                                        onClick = { currentView = CalendarView.WEEKLY },
                                        modifier = Modifier.padding(end = 16.dp) // Adjust padding as needed
                                    ) {
                                        Text("Weekly View")
                                    }
                                }
                            }



                        }
                    }

                    when (currentView) {
                        CalendarView.DAILY -> {
                            DailyCalendarView(currentDateState)
                        }
                        CalendarView.WEEKLY -> {
                            WeeklyCalendarView(currentDateState)
                        }
                        CalendarView.MONTHLY -> {
                            MonthlyCalendarView(currentDateState)
                        }
                    }
                }

            }
        }

        return view
    }

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
                for (day in MonthlyView.Weekday.values()) {
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

                        val today = LocalDate.now()
                        val isCurrentMonth = date.month == currentMonth
                        val isCurrentDay = isCurrentMonth && date.isEqual(today)

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


    @Composable
    fun DailyCalendarView(currentDateState: MutableState<LocalDate>) {
        // Use currentDateState to calculate dates for the current week
        val currentDate = currentDateState.value

        // Get the current time
        val currentTime = LocalTime.now()

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
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Date button
                Button(
                    onClick = {
                        // Reset to the current date when the button is clicked
                        currentDateState.value = LocalDate.now()
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(55.dp)
                        .background(color = Color.Yellow, shape = CircleShape),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null
                ) {
                    Text(
                        text = currentDate.format(DateTimeFormatter.ofPattern("MMM d")),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }

                // Title
                Text(
                    text = "Daily Calendar",
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }

            // Previous and next day buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        currentDateState.value = currentDate.minusDays(1) // Go to the previous day
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Previous Day")
                }

                Button(
                    onClick = {
                        currentDateState.value = currentDate.plusDays(1) // Go to the next day
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(text = "Next Day")
                }
            }

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

                        // Add a box for class from 3:00 PM to 4:00 PM on the current day
                        if (index == hoursOfDay.indexOf("3:00 PM") && currentDate == LocalDate.now()) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .background(Color.Cyan)
                                    .padding(vertical = 2.dp)
                                    .clickable {
                                        isClassBoxVisible.value = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Class",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    // Add a horizontal line between rows
                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            }

            // Enlarged box for the class details
            if (isClassBoxVisible.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .clickable {
                            isClassBoxVisible.value = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp)
                            .background(Color.Cyan)
                    ) {
                        // Class details
                        Text(
                            text = "CECS 491B \n Section 02 \n Room 405",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun WeeklyCalendarView(currentDateState: MutableState<LocalDate>) {
        // Use currentDateState to calculate dates for the current week
        val currentDate = currentDateState.value
        val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        // Adjusting to align the dates correctly with the current week
        val dates = (0..6).map {
            currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .plusDays(it.toLong())
        }

        val hoursOfDay = listOf(
            "12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM", "6:00 AM",
            "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM",
            "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM",
            "9:00 PM", "10:00 PM", "11:00 PM"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Title
            Text(
                text = "Weekly Calendar",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )

            // Navigation buttons: Previous Week, Current Week, and Next Week
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { currentDateState.value = currentDate.minusWeeks(1) },
                    modifier = Modifier.padding(6.dp)
                ) {
                    Text("Previous Week")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { currentDateState.value = LocalDate.now() },
                    shape = CircleShape,
                    modifier = Modifier.padding(6.dp)
                ) {
                    Text(
                        text = LocalDate.now().format(DateTimeFormatter.ofPattern("E MM/dd")),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = { currentDateState.value = currentDate.plusWeeks(1) },
                    modifier = Modifier.padding(6.dp)
                ) {
                    Text("Next Week")
                }
            }

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
                            // Adding the blue box for March 19th at 3 PM
                            if (date == LocalDate.of(
                                    2024,
                                    Month.MARCH,
                                    19
                                ) && LocalTime.now() == LocalTime.of(15, 0)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(Color.Blue)
                                )
                            }
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        // Time
                        Box(
                            modifier = Modifier
                                .width(64.dp)
                                .height(60.dp)
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
                                    .height(60.dp).border(1.dp, Color.Gray)
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
}