package com.clairvoyance.clairvoyance

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.clairvoyance.clairvoyance.databinding.TaskItemBinding
import java.time.format.DateTimeFormatter

// Holds view for task in the to-do list
class TaskViewHolder(
    private val context: Context,
    private val binding: TaskItemBinding,
    private val clickListener: TaskClickListener
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var subTaskRecyclerView : RecyclerView

    fun bindTask(task: Task) {
        val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
        subTaskRecyclerView = itemView.findViewById(R.id.subTaskRecyclerView)
        binding.name.text = task.name

        if (task.isExpanded) {
            binding.subTaskList.visibility = View.VISIBLE
//            binding.dropupButton.visibility = View.VISIBLE
//            binding.dropdownButton.visibility = View.GONE
        } else {
            binding.subTaskList.visibility = View.GONE
//            binding.dropupButton.visibility = View.GONE
//            binding.dropdownButton.visibility = View.VISIBLE
        }

        // Puts strike through text on a task and its due time when marked as done
        if (task.isCompleted) {
            binding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.endTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        // Checks to see if task end time is set
        if (task.endTime != null)
            binding.endTime.text = timeFormat.format(task.Time(task.endTime.toString()))
        else
            binding.endTime.text = ""

        // Changes task image and color when being marked as complete
        binding.completeButton.setImageResource(task.imageResource())
        binding.completeButton.setColorFilter(task.imageColor(context))
        binding.taskCellContainer.setCardBackgroundColor(task.backgroundColor(context))

        // Setting click listeners
        binding.completeButton.setOnClickListener {
            clickListener.completeTask(task)
        }

        binding.taskCellContainer.setOnClickListener{
            clickListener.editTask(task)
        }

        binding.dropdownButton.setOnClickListener{
            clickListener.expandTask(task)
        }

        binding.dropupButton.setOnClickListener{
            clickListener.expandTask(task)
        }

        binding.newSubTaskButton.setOnClickListener{
            clickListener.newSubTask(task)
        }
    }
}