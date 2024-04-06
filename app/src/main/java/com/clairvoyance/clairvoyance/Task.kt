package com.clairvoyance.clairvoyance

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class Task (
    // Constructor items
    var name: String,
    var desc: String,
    var startTime: LocalTime?,
    var endTime: LocalTime?,
    var date: LocalDate?,

    // Initialized values
    var completedDate: LocalDate? = null,
    var isExpanded: Boolean = false,
    var isCompleted: Boolean = false,
    val id: UUID = UUID.randomUUID(),
    var children: MutableList<Task> = mutableListOf(),
    var dataFields: MutableList<DataField> = mutableListOf(),
    var tags: MutableList<String> = mutableListOf()
) {
    // Initialized values

    // Empty constructor
    constructor() : this("", "", null, null, null, null)

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
}