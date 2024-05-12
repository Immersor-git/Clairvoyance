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

class ContactUs : AppCompatActivity() {
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = MainActivity()

        setContentView(ComposeView(this).apply {
            setContent {
                Contact()
            }
        })
    }
}



@Composable
fun Contact() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Title("Contact Us")

            Text(
                text = "Customer Support",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "For any questions, feedback, or issues regarding the app, please contact our customer support team. We are here to assist you and ensure that you have the best experience with our app.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Email: support@clairvoyance.com",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Hours: Monday to Friday, 9:00am - 5:00pm",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )


            Spacer(modifier = Modifier.height(15.dp))


            Text(
                text = "Technical Support",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "If you encounter any technical issues or bugs while using the app, please contact our technical support team. Our experts will work diligently to resolve the issue and ensure that you can continue using the app smoothly.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Email: techsupport@clairvoyance.com",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Hours: Monday to Friday, 9:00am - 5:00pm",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(15.dp))


            Text(
                text = "Feedback and Comments ",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "We value your feedback and suggestions for improving the app. If you have any ideas or features you'd like to see implemented, please let us know. Your input helps us make the app better for everyone.",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Email: feedback@clairvoyance.com",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )


        }
    }
}