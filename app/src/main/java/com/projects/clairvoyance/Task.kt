package com.clairvoyance.clairvoyance

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

// Creates a task item
class Task (
    // Data fields, only required fields are name and description
    // Description can be left blank
    var name: String,
    var desc: String,
    var image: Bitmap?,
    var startTime: LocalTime?,
    var endTime: LocalTime?,
    var dueTime: LocalTime?,
    var date: LocalDate?,
    var completedDate: LocalDate?,
    var id: UUID = UUID.randomUUID()
) {
    fun isCompleted() = completedDate != null
    fun imageResource(): Int = if (isCompleted()) R.drawable.checked else R.drawable.unchecked
    fun imageColor(context: Context): Int = if(isCompleted()) purple(context) else black(context)

    // Color functions for checkbox
    private fun purple(context: Context) = ContextCompat.getColor(context, R.color.purple_500)
    private fun black(context: Context) = ContextCompat.getColor(context, R.color.black)
}