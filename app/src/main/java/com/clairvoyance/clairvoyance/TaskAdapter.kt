package com.clairvoyance.clairvoyance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clairvoyance.clairvoyance.databinding.TaskItemBinding

class TaskAdapter(
    private val tasks: List<Task>,
    private val clickListener: TaskClickListener,
) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TaskItemBinding.inflate(from, parent, false)
        return TaskViewHolder(parent.context, binding, clickListener)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindTask(tasks[position])

        //val childAdapter = TaskAdapter(tasks[position].children, clickListener)
        //holder.subTaskRecyclerView.adapter = childAdapter
    }
}