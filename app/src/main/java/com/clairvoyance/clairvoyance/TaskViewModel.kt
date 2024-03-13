package com.clairvoyance.clairvoyance

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
    fun updateTask(id: UUID, name: String, desc: String, startTime: LocalTime?, endTime: LocalTime?, date: LocalDate?, dataFields: MutableLiveData<MutableList<DataField<*>>>) {
        val list = tasks.value
        val task = list!!.find {it.id == id}!!
        task.name = name
        task.desc = desc
        task.startTime = startTime
        task.endTime = endTime
        task.date = date
        task.dataFields.value = dataFields.value
        println(task.dataFields.value!!.size)
        tasks.postValue(list)
    }

    // Marks a task as complete
    fun setCompleted(task: Task) {
        val list = tasks.value
        val currTask = list!!.find {it.id == task.id}!!
        currTask.completed = !currTask.completed
        currTask.completedDate = LocalDate.now()
        tasks.postValue(list)
    }

    fun expandTask(task: Task) {
        val list = tasks.value
        val currTask = list!!.find{it.id == task.id}!!
        currTask.expanded = !currTask.expanded
        tasks.postValue(list)
    }
}