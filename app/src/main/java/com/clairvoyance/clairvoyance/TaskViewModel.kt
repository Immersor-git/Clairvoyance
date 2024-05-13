package com.clairvoyance.clairvoyance

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime

class TaskViewModel(private val appViewModel: AppViewModel) : ViewModel()
{
    private val _taskList: MutableStateFlow<MutableList<Task>> = MutableStateFlow(mutableStateListOf())
    val taskList = _taskList.asStateFlow()

    private val _taskArchive: MutableStateFlow<MutableList<Task>> = MutableStateFlow(mutableStateListOf())
    val taskArchive = _taskArchive.asStateFlow()

    private val _templates: MutableStateFlow<MutableList<TaskTemplate>> = MutableStateFlow(mutableStateListOf())
    val templates = _templates.asStateFlow()
//    var tasks = MutableLiveData<MutableList<Task>?>()

    fun addTaskItem(task: Task) {
        _taskList.update {
            taskList.value.toMutableList().apply { this.add(task) }
        }
        saveTaskToDatabase(task)
    }

    fun updateTaskItem(task: Task, name: String, desc: String, startTime: LocalTime?, endTime:LocalTime?, date: LocalDate?, dataFields: MutableList<DataField>) {
        Log.d("TASK STUFF", name + " 3")
        _taskList.update {
            _taskList.value.toMutableList().apply {
                // Create copy of task item
                val currTask = this.find { it.id == task.id }!!
                val copy = currTask.copy()

                // Edit fields
                copy.name = name
                copy.desc = desc
                copy.startTime = startTime
                copy.endTime = endTime
                copy.date = date
                copy.dataFields = dataFields

                saveTaskToDatabase(copy)
                // Replace task with updated copy to trigger recomposition
                this[indexOf(currTask)] = copy
            }
        }
    }
    fun updateCheckbox(task: Task, dataField : DataField, isChecked : Boolean) {
        _taskList.update {
            _taskList.value.toMutableList().apply {
                // Create copy of task item
                val currTask = this.find { it.id == task.id }!!
                val copy = currTask.copy()


                // Edit fields
                //copy.name = name
                //copy.desc = desc
                //copy.startTime = startTime
                //copy.endTime = endTime
                //copy.date = date
                var dfIndex = -1
                task.dataFields.forEachIndexed { index, element ->
                    if (element.id == dataField.id) {
                        dfIndex = index
                    }
                }
                if (dfIndex > -1) {
                    copy.dataFields[dfIndex] = DataField(DataType.CHECKBOX, Checkbox(isChecked, (dataField.data as Checkbox).desc), dataField.tag, dataField.id)
                }

                //Log.d("TASK STUFF", dataFields.size.toString())
                saveTaskToDatabase(copy)
                // Replace task with updated copy to trigger recomposition
                this[indexOf(currTask)] = copy
            }
        }
    }

    inline fun <reified T> clearMutableTaskList(stateFlow : MutableStateFlow<MutableList<T>>) {
        stateFlow.update {
            stateFlow.value.toMutableList().apply {
                this.clear()
            }
        }
    }

    fun deleteTaskItem(task:Task) {
        _taskList.update {
            _taskList.value.toMutableList().apply {
                this.remove(task)
            }
        }
        archiveTask(task)
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

    fun addTaskTemplate(template: TaskTemplate) {
        _templates.update {
            templates.value.toMutableList().apply { this.add(template) }
        }

        saveTemplateToDatabase(template)
    }

    fun saveTaskToDatabase(task : Task) { //Saves a task to the database - does not update local MutableList
        val db = appViewModel.databaseManager
        db.saveTask(task);
    }

    fun saveTemplateToDatabase(taskTemplate: TaskTemplate) { //Saves a template to the database - does not update local MutableList
        val db = appViewModel.databaseManager
        db.saveTemplate(taskTemplate);
    }

    fun getUserTasks() { //Pulls the tasks for the current user from the database and populates the taskList with them. Clears the list before pulling
        val accountManager = appViewModel.accountManager
        val databaseManager = appViewModel.databaseManager
        if (accountManager.user.userID != "X") {
            databaseManager.getTasks() { userTaskList ->
                clearMutableTaskList(_taskList)
                _taskList.update {
                    taskList.value.toMutableList().clear()
                    taskList.value.toMutableList().apply {
                        for (t in userTaskList) {
                            this.add(t)
                        }
                    }
                }
            }
        }
    }

    fun getTemplates() { //Pulls all templates from the database
        val accountManager = appViewModel.accountManager
        val databaseManager = appViewModel.databaseManager
        if (accountManager.user.userID != "X") {
            databaseManager.getTemplates() { templateList ->
                clearMutableTaskList(_templates)
                _templates.update {
                    templates.value.toMutableList().clear()
                    templates.value.toMutableList().apply {
                        for (t in templateList) {
                            this.add(t)
                        }
                    }
                }
            }
        }
    }

    fun getArchivedTasks() { //Pulls all archive tasks from the database
        val accountManager = appViewModel.accountManager
        val databaseManager = appViewModel.databaseManager
        if (accountManager.user.userID != "X") {
            databaseManager.getTaskArchive() { userTaskList ->
                clearMutableTaskList(_taskArchive)
                _taskArchive.update {
                    taskArchive.value.toMutableList().clear()
                    taskArchive.value.toMutableList().apply {
                        for (t in userTaskList) {
                            this.add(t)
                        }
                    }
                }
            }
        }
    }

    fun archiveTask(task : Task) { //Archives a task in the database - once the database is updated, it re-pulls all user tasks and archived tasks (sucks ass for efficiency but it works)
        val accountManager = appViewModel.accountManager
        val databaseManager = appViewModel.databaseManager
        databaseManager.archiveTask(task) {
            getUserTasks()
            getArchivedTasks()
        }
        _taskList.update {
            taskList.value.toMutableList().apply { this.remove(task) }
        }


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