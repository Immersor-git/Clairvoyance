package com.clairvoyance.clairvoyance.HelpItems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clairvoyance.clairvoyance.MainActivity

class GraphInfo : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = MainActivity()

        setContentView(ComposeView(this).apply {
            setContent {
                Graph()
            }
        })
    }
}



@Composable
fun Graph() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Title("Graph Help")

            Text(
                text = "Overview",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The app allows you to graph data fields set up within a task, providing visual representations of your data. This feature is useful for analyzing trends, tracking progress, and gaining insights from your tasks.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))



            Text(
                text = "Viewing Graphs",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Once you have added data fields to your tasks, you can view graphs of the data within the task details",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
    }


}