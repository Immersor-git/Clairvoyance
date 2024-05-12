package com.clairvoyance.clairvoyance.HelpItems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clairvoyance.clairvoyance.MainActivity

class TaskInfo : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = MainActivity()

        setContentView(ComposeView(this).apply {
            setContent {
               Task()
            }
        })
    }
}



@Composable
fun Task() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Title("Task Help")

            //Overview
            Text(
                text = "Overview",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The app allows you to create tasks with various features, including data fields, images, audio attachments, times, and dates. You can also save and load task templates for easy task creation.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))

            //Creating Task

            Text(
                text = "Task Creation",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = " Open the app and click the new task button\n" +
                        "Enter a name for the task.\n" +
                        "Add data fields by tapping on the \"Add Data Field\" button and entering the info if necessary.\n" +
                        "Attach images by tapping on the \"IMG\" button\n" +
                        "Attach audio files by tapping on the \"Add Audio\"\n" +
                        "Set times and dates by tapping on the \"Start Time\" and \"End Time\" button and selecting the desired time",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))

            //Task Templates


            Text(
                text = "Saving and Loading Task Templates",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "To save a task as a template for future use, follow these steps:\n" +
                        "1. After creating a task, tap on the \"Save as Template\" option.\n" +
                        "To load a saved template, tap on the \"Load Template\" option when creating a new task and select the desired template from the list.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}
