package com.clairvoyance.clairvoyance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.clairvoyance.clairvoyance.databinding.ActivityMainBinding
import com.projects.clairvoyance.TaskClickListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), TaskClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        binding.newTaskButton.setOnClickListener {
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")

        }

        // Checks for todays date and sets it in the main task list
        val formatter = DateTimeFormatter.ofPattern("MMMM-dd")
        val todaysDate = LocalDate.now()
        val formattedDate = todaysDate.format(formatter)

        binding.dailyDate.text = formattedDate

        setRecyclerView()


    }


    private fun setRecyclerView() {
        val mainActivity = this
        taskViewModel.tasks.observe(this) {
            binding.taskListRecylerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskAdapter(it, mainActivity)
            }
        }
    }

    override fun editTask(task: Task) {
        NewTaskSheet(task).show(supportFragmentManager, "newTaskTag")
    }

    override fun completeTask(task: Task) {
        taskViewModel.setCompleted(task)
    }




}