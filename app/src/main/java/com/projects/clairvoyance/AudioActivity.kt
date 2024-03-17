package com.clairvoyance.clairvoyance

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.clairvoyance.clairvoyance.playback.AndroidAudioPlayer
import com.clairvoyance.clairvoyance.record.AndroidAudioRecorder
import java.io.File


class AudioActivity : AppCompatActivity() {



    private val recorder by lazy{
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy{
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),0
        )


        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(onClick = {
                    File(cacheDir, "audio.mp3").also {
                        recorder.start(it)
                        audioFile = it
                    }
                }) {
                    Text(text = "Start recording")
                }
                OutlinedButton(onClick = {
                    recorder.stop()
                }) {
                    Text(text = "Stop recording")
                }
                OutlinedButton(onClick = {
                    player.play(audioFile ?: return@OutlinedButton)
                }) {
                    Text(text = "Play")
                }
                OutlinedButton(onClick = {
                    player.stop()
                }) {
                    Text(text = "Stop playing")
                }
            }
        }
    }
}


