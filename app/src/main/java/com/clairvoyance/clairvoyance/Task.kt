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
    var children: ArrayList<Task>,

    var expanded: Boolean = false,
    var completed: Boolean = false,
    var id: UUID = UUID.randomUUID(),
    var dataFields: MutableLiveData<MutableList<DataField<*>>> = MutableLiveData<MutableList<DataField<*>>>(),
    var parents: MutableLiveData<MutableList<Task>> = MutableLiveData<MutableList<Task>>(),
) {

    init {
        dataFields.value = mutableListOf()
        parents.value = mutableListOf()
    }
    fun isCompleted() = completed
    fun imageResource(): Int = if (this.isCompleted()) R.drawable.checkmark_complete else R.drawable.checkmark_uncomplete
    fun imageColor(context: Context): Int {
        return ContextCompat.getColor(
            context,
            if (this.isCompleted()) R.color.purple_500 else R.color.black)
    }

    fun backgroundColor(context: Context): Int {
        return ContextCompat.getColor(
            context,
            if (this.isCompleted()) androidx.appcompat.R.color.material_grey_100 else R.color.white)
    }

    fun addDataFields(dataFields: MutableLiveData<MutableList<DataField<*>>>) {
        this.dataFields = dataFields
    }
}