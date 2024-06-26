package com.clairvoyance.clairvoyance

interface TaskClickListener {
    fun editTask(task: Task)
    fun completeTask(task: Task)
    fun expandTask(task: Task)
    fun newSubTask(parent: Task)
}