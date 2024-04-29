package com.clairvoyance.clairvoyance

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskViewModel : ViewModel()
{
    private val _taskList: MutableStateFlow<MutableList<Task>> = MutableStateFlow(mutableStateListOf())
    val taskList = _taskList.asStateFlow()
//    var tasks = MutableLiveData<MutableList<Task>?>()

    fun addTaskItem(task: Task) {
        _taskList.update {
            taskList.value.toMutableList().apply { this.add(task) }
        }
    }

    fun updateTaskItem(task: Task, name: String, desc: String, dataFields: MutableList<DataField>) {
        _taskList.update {
            _taskList.value.toMutableList().apply {
                // Create copy of task item
                val currTask = this.find { it.id == task.id }!!
                val copy = currTask.copy()

                // Edit fields
                copy.name = name
                copy.desc = desc
                copy.dataFields = dataFields

                Log.d("TASK STUFF", dataFields.size.toString())

                // Replace task with updated copy to trigger recomposition
                this[indexOf(currTask)] = copy
            }
        }
    }

    fun setComplete(task: Task) {
        _taskList.update {
            _taskList.value.toMutableList().apply {
                // Create copy of task item
                val currTask = this.find { it.id == task.id }!!
                val copy = currTask.copy()

                // Edit fields
                copy.isCompleted = !currTask.isCompleted

                // Replace task with updated copy to trigger recomposition
                this[indexOf(currTask)] = copy
            }
        }
    }



     fun createNotification(context: Context, taskName: String) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        // Create a notification channel (required for Android Oreo and higher)
         val channel = NotificationChannel(
             "task_notification_channel",
             "Task Notifications",
             NotificationManager.IMPORTANCE_DEFAULT
         )
         notificationManager.createNotificationChannel(channel)

         val notification = NotificationCompat.Builder(context, "task_notification_channel")
             .setContentTitle("Task Reminder")
             .setContentText("Don't forget to complete task: $taskName")
             .build()

         notificationManager.notify(1, notification)
     }



//    // Adds a new task to the list
//    fun addTask(newTask: Task) {
//        val list = tasks.value
//        list!!.add(newTask)
//        tasks.postValue(list)
//    }
//
//    // Updates a task
//    fun updateTask(id: UUID, name: String, desc: String, startTime: LocalTime?, endTime: LocalTime?, date: LocalDate?, dataFields: MutableLiveData<MutableList<DataField>>) {
//        val list = tasks.value
//        val task = list!!.find {it.id == id}!!
//        task.name = name
//        task.desc = desc
//        task.startTime = startTime
//        task.endTime = endTime
//        task.date = date
//        tasks.postValue(list)
//    }
//
//    // Marks a task as complete
//    fun setCompleted(task: Task) {
//        val list = tasks.value
//        val currTask = list!!.find {it.id == task.id}!!
//        currTask.isCompleted = !currTask.isCompleted
//        currTask.completedDate = LocalDate.now()
//        tasks.postValue(list)
//    }
//
//    fun expandTask(task: Task) {
//        val list = tasks.value
//        val currTask = list!!.find { it.id == task.id }!!
//        currTask.isExpanded = !currTask.isExpanded
//        tasks.postValue(list)
//    }
}