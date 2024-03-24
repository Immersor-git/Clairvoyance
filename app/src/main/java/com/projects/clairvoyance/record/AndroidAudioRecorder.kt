package com.clairvoyance.clairvoyance.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream

//implemenation of the interface that holds the functions for the audio recorder
class AndroidAudioRecorder(private val context: Context): AudioRecorder {

    private var recorder: MediaRecorder? = null
    private fun createRecorder(): MediaRecorder? {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            MediaRecorder(context)}
        else MediaRecorder()
    }

    //function to start recording
    override fun start(output: File){
        createRecorder()?.apply{
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(output).fd)


            prepare()
            start()

            recorder = this

        }



    }
//function to stop the recording
    override fun stop(){
       recorder?.stop()
       recorder?.reset()
        recorder = null


    }


}