//package com.clairvoyance.clairvoyance
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.clairvoyance.clairvoyance.databinding.ActivityMainBinding
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//
//class ToDoFragmentNew : AppCompatActivity(), TaskClickListener {
//
//    private lateinit var binding : ActivityMainBinding
//    private lateinit var taskViewModel: TaskViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
//
//        binding.newTaskButton.setOnClickListener {
//            TaskSheet(null, null).show(supportFragmentManager, "newTaskTag")
//        }
//
//        // Checks for today's date and sets it in the main task list
//        val formatter = DateTimeFormatter.ofPattern("MMMM dd")
//        val todaysDate = LocalDate.now()
//        val formattedDate = todaysDate.format(formatter)
//        binding.dailyDate.text = formattedDate
//
//        setRecyclerView()
//    }
//
//    private fun setRecyclerView() {
//        val mainActivity = this
//        taskViewModel.tasks.observe(this) {
//            binding.taskListRecylerView.apply {
//                layoutManager = LinearLayoutManager(applicationContext)
//                adapter = TaskAdapter(it, mainActivity)
//            }
//        }
//    }
//
//    override fun editTask(task: Task) {
//        TaskSheet(task, null).show(supportFragmentManager, "newTaskTag")
//    }
//
//    override fun completeTask(task: Task) {
//        taskViewModel.setCompleted(task)
//    }
//
//    override fun expandTask(task: Task) {
//        taskViewModel.expandTask(task)
//    }
//
//    override fun newSubTask(parent: Task) {
//        TaskSheet(null, parent).show(supportFragmentManager, "newTaskTag")
//    }
//}