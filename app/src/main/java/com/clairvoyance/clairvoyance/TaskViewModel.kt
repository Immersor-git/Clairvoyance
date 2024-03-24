package com.clairvoyance.clairvoyance

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TaskViewModel : ViewModel()
{
    val taskList: MutableStateFlow<MutableList<Task>> = MutableStateFlow(mutableStateListOf(Task("My First Task")))
    var tasks = MutableLiveData<MutableList<Task>?>()

    fun updateTaskList(task: Task) {
        taskList.update {
            taskList.value.toMutableList().apply { this.add(task) }
        }
    }

    fun updateTaskItem(task: Task) {
        taskList.update {
            taskList.value.toMutableList().apply {
                val currTask = this.find { it.id == task.id }!!
                currTask.name = "hoopla"
            }
        }
    }

    fun setComplete(task: Task) {
        taskList.update {
            taskList.value.toMutableList().apply {
                val currTask = this.find { it.id == task.id }!!
                currTask.isCompleted = !currTask.isCompleted
            }
        }
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
        currTask.isCompleted = !currTask.isCompleted
        currTask.completedDate = LocalDate.now()
        tasks.postValue(list)
    }

    fun expandTask(task: Task) {
        val list = tasks.value
        val currTask = list!!.find { it.id == task.id }!!
        currTask.isExpanded = !currTask.isExpanded
        tasks.postValue(list)
    }
}