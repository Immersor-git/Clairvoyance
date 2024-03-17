package com.clairvoyance.clairvoyance

import android.content.Context
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class Task(
    var name: String,
    var desc: String,
    var startTime: LocalTime?,
    var endTime: LocalTime?,
    var dueTime: LocalTime?,
    var date: LocalDate?,
    var completedDate: LocalDate?,
    var id: UUID? = UUID.randomUUID()
) {
    fun isCompleted() = completedDate != null
    fun imageResource(): Int = if (isCompleted()) R.drawable.checked else R.drawable.unchecked
    fun imageColor(context: Context): Int = if(isCompleted()) purple(context) else black(context)

    private fun purple(context: Context) = ContextCompat.getColor(context, R.color.purple_500)
    private fun black(context: Context) = ContextCompat.getColor(context, R.color.black)
}
