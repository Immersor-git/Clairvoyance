package com.clairvoyance.clairvoyance

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class Task (
    var name: String,
    var desc: String,
    var startTime: LocalTime?,
    var endTime: LocalTime?,
    var date: LocalDate?,
    var completedDate: LocalDate?,
    var children: ArrayList<Task> = ArrayList<Task>(),
    var isExpanded: Boolean = false,
    var isCompleted: Boolean = false,
    var id: UUID = UUID.randomUUID(),
    var dataFields: ArrayList<DataField> = ArrayList<DataField>(),
) {

    // Empty constructor
    constructor(name: String) : this(name, "", null, null, null, null, ArrayList<Task>())
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