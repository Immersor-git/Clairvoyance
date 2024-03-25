package com.clairvoyance.clairvoyance.playback

import java.io.File

interface AudioPlayer {

    fun play(file: File)
    fun stop()
}