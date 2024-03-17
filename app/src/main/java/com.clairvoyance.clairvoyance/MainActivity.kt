package com.clairvoyance.clairvoyance

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.clairvoyance.clairvoyance.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
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

        // Checks for today's date and sets it in the main task list
        val formatter = DateTimeFormatter.ofPattern("MMMM-dd")
        val todaysDate = LocalDate.now()
        val formattedDate = todaysDate.format(formatter)

        binding.dailyDate.text = formattedDate

        setRecyclerView()

        // Create notification channel
        createNotificationChannel()

        // Schedule notification for the first task, if available
        scheduleNotificationForFirstTask()
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

    private fun scheduleNotificationForFirstTask() {
        taskViewModel.tasks.observe(this) { tasks ->
            tasks.firstOrNull()?.let { task ->
                task.dueTime?.let { dueTime ->
                    val dueDateTime = LocalDateTime.of(LocalDate.now(), dueTime)
                    val dueTimeInMillis = dueDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    scheduleNotification(this, dueTimeInMillis)
                }
            }
        }
    }



    override fun editTask(task: Task) {
        NewTaskSheet(task).show(supportFragmentManager, "newTaskTag")
    }

    override fun completeTask(task: Task) {
        taskViewModel.setCompleted(task)
    }

    override fun onTaskClicked(task: Task) {
        // Schedule notification based on task due time
        task.dueTime?.let { dueTime ->
            val dueDateTime = LocalDateTime.of(LocalDate.now(), dueTime)
            val dueTimeInMillis = dueDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            scheduleNotification(this, dueTimeInMillis)
        }
    }


    // Create notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "task_channel_id"
            val channelName = "Task Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Notification channel for task reminders"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Schedule notification for a specific time
    private fun scheduleNotification(context: Context, dueTimeInMillis: Long) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val requestCode = 0 // Use a unique request code here
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, dueTimeInMillis, pendingIntent)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
