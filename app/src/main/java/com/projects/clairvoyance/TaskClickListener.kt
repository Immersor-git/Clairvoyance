package com.projects.clairvoyance

import com.clairvoyance.clairvoyance.Task

interface TaskClickListener {
    fun editTask(task: Task)
    fun completeTask(task: Task)
}