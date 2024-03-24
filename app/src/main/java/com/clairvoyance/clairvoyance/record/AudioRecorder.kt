package com.clairvoyance.clairvoyance.record

import java.io.File

//interface that holds the names of the functions for the audio recorder
interface AudioRecorder {
    fun start(output : File)
    fun stop()
}