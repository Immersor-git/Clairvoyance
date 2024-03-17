package com.clairvoyance.clairvoyance

interface TaskClickListener {
    fun editTask(task: Task)
    fun completeTask(task: Task)
    fun onTaskClicked(task: Task)
}
