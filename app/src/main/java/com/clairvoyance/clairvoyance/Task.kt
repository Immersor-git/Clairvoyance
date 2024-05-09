package com.clairvoyance.clairvoyance

import android.content.Context
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Task (
    // Constructor items
    var name: String,
    var desc: String,
    var startTime: String?, //LocalTime?,
    var endTime: String?, //LocalTime?,
    var date: LocalDate?,

    // Initialized values
    var completedDate: LocalDate? = null,
    var isExpanded: Boolean = false,
    var isCompleted: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
    var children: MutableList<Task> = mutableListOf(),
    var dataFields: MutableList<DataField> = mutableListOf(),
    var tags: MutableList<String> = mutableListOf(),
    var templateName : String = "",
) {
    // Initialized values

    // Empty constructor
    constructor() : this("", "", null, null, null, null)

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "desc" to desc,
            "startTime" to startTime,
            "endTime" to endTime,
            "date" to date,
            "completedDate" to completedDate,
            "dataFields" to dataFields
        )
    }

    fun imageResource(): Int = if (this.isCompleted) R.drawable.checkmark_complete else R.drawable.checkmark_uncomplete
    fun imageColor(context: Context): Int {
        return ContextCompat.getColor(
            context,
            if (this.isCompleted) R.color.purple_500 else R.color.black
        )
    }

    fun backgroundColor(context: Context): Int {
        return ContextCompat.getColor(
            context,
            if (this.isCompleted) androidx.appcompat.R.color.material_grey_100 else R.color.white
        )
    }
    fun Time(time : String) : LocalTime {
        return LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME)
    }

    fun Date(date : String) : LocalDate {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}