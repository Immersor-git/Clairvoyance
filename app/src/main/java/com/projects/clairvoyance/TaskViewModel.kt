package com.clairvoyance.clairvoyance

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TaskViewModel : ViewModel()
{
    var tasks = MutableLiveData<MutableList<Task>>()

    init {
        tasks.value = mutableListOf()
    }

    // Adds a new task to the list
    fun addTask(newTask: Task) {
        val list = tasks.value
        list!!.add(newTask)
        tasks.postValue(list)
    }

    // Updates a task
    fun updateTask(id: UUID, name: String, desc: String, image: Bitmap?, startTime: LocalTime?, endTime: LocalTime?, dueTime: LocalTime?, date: LocalDate?) {
        val list = tasks.value
        val task = list!!.find {it.id == id}!!
        task.name = name
        task.desc = desc
        task.image = image
        task.dueTime = dueTime
        task.startTime = startTime
        task.endTime = endTime
        task.date = date
        tasks.postValue(list)
    }

    // Marks a task as complete
    fun setCompleted(task: Task) {
        val list = tasks.value
        val currTask = list!!.find {it.id == task.id}!!
        if (currTask.completedDate == null)
            currTask.completedDate = LocalDate.now()
        tasks.postValue(list)
    }
}