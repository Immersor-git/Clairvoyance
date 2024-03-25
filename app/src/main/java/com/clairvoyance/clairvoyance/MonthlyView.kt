package com.clairvoyance.clairvoyance

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
class MonthlyView : Fragment() {
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

                MonthlyCalendarView(currentDateState)
            }
        }

        return view;
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