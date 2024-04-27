package com.clairvoyance.clairvoyance

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskViewModel(private val appViewModel: AppViewModel) : ViewModel()
{
    private val _taskList: MutableStateFlow<MutableList<Task>> = MutableStateFlow(mutableStateListOf())
    val taskList = _taskList.asStateFlow()
//    var tasks = MutableLiveData<MutableList<Task>?>()

    fun addTaskItem(task: Task) {
        _taskList.update {
            taskList.value.toMutableList().apply { this.add(task) }
        }
        saveTaskToDatabase(task)
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
                saveTaskToDatabase(copy)
                // Replace task with updated copy to trigger recomposition
                this[indexOf(currTask)] = copy
            }
        }
    }

    fun saveTaskToDatabase(task : Task) {
        val account = appViewModel.accountManager.getDatabaseReference();
        val db = appViewModel.databaseManager
        db.writeDoc(account+"/Tasks/${task.name}",task);
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
        saveTaskToDatabase(task)
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