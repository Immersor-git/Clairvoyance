package com.clairvoyance.clairvoyance

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.clairvoyance.clairvoyance.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter

class TaskViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskClickListener
) : RecyclerView.ViewHolder(binding.root) {

    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    fun bindTask(task: Task) {
       binding.name.text = task.name

        // Puts strike through text on a task and its due time when marked as done
        if (task.isCompleted()) {
            binding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.endTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.completeButton.setImageResource(task.imageResource())
        binding.completeButton.setColorFilter(task.imageColor(context))

        // Setting click listeners for editing and marking a task as done
        binding.completeButton.setOnClickListener {
            clickListener.completeTask(task)
        }

        binding.taskCellContainer.setOnClickListener{
            clickListener.editTask(task)
        }

        // Checks to see if task end time is set
        if (task.endTime != null)
            binding.endTime.text = timeFormat.format(task.endTime)
        else
            binding.endTime.text = ""
    }
}