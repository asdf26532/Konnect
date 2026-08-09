package com.han.konnect.util

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.IOException

class AudioRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var audioFile: File? = null

    fun startRecording(onStart: () -> Unit = {}) {
        val outputDir = context.cacheDir
        audioFile = File.createTempFile("voice_note_", ".mp3", outputDir)

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile?.absolutePath)
            try {
                prepare()
                start()
                onStart()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun stopRecording(): File? {
        return try {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            audioFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun playAudio(url: String, onComplete: () -> Unit) {
        stopAudio()
        player = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { start() }
            setOnCompletionListener {
                onComplete()
                release()
                player = null
            }
        }
    }

    fun stopAudio() {
        player?.apply {
            if (isPlaying) stop()
            release()
        }
        player = null
    }
}