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

class TaskArchiveInfo : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = MainActivity()

        setContentView(ComposeView(this).apply {
            setContent {
                Archive()
            }
        })
    }
}



@Composable
fun Archive() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Title("Archive Help")

            Text(
                text ="Overview",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "The task archive allows you to view past tasks that you have completed or archived. You can also choose to revive archived tasks if you need to revisit them or continue working on them.",
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))

            //Viewing

            Text(
                text ="View Task Archive",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Open the app's main menu.\n" +
                        "Tap on \"Task Archive\" \n" +
                        "You will see a list of archived tasks.",
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))

            //Revive

            Text(
                text ="Reviving Archived Tasks",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "To revive an archived task and bring it back to your active task list, follow these steps:\n" +
                        "\n" +
                        "From the list of archived tasks, tap on the task you want to revive.\n" +
                        "Tap on the option to Revive Task" +
                        "The task will be moved from the archive to your active task list, and you can continue working on it as needed.",
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))

            //Clear Task Archive


            Text(
                text ="Clear Task Archive",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Simply press the clear task archive button at the top of the page",
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(15.dp))

        }
    }
}