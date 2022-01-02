package com.example.aop_chapter2_recoder

import android.content.pm.PackageManager
import android.media.MediaParser
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aop_chapter2_recoder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var view: ActivityMainBinding

    private var state = State.BEFORE_RECORDING
        set(value) {
            field = value
            view.resetButton.isEnabled = value==State.AFTER_RECORDING || value == State.ON_PLAYING


            view.recordButton.updateIconWithState(field)
        }

    private var requiredPermissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private val recordingFilePath: String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    companion object {
        private const val REQUEST_CODE = 201
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)


        requestAudioPermission()
        initViews()
        bindViews()
        initVariables()
    }


    private fun requestAudioPermission() {


        requestPermissions(requiredPermissions, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        val audioRecordPermissionGranted = requestCode == REQUEST_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED



        if (!audioRecordPermissionGranted)
            finish()
    }


    private fun initViews() {
        view.recordButton.updateIconWithState(state)
    }


    private fun bindViews(){

        view.soundVistualizerView.onRequestCurrentAmplitude = {

            recorder?.maxAmplitude ?:0

        }

        view.soundVistualizerView.clearVisualization()
        view.recordTextView.clearCountTime()

        view.resetButton.setOnClickListener {
                stopPlaying()
                state = State.BEFORE_RECORDING
                view.soundVistualizerView.clearVisualization()
                view.recordTextView.clearCountTime()
        }

        view.recordButton.setOnClickListener {

            when(state){
                State.BEFORE_RECORDING->startRecording()
                State.ON_RECORDING->stopRecording()
                State.AFTER_RECORDING->startPlaying()
                State.ON_PLAYING->stopPlaying()

            }

        }



    }

    private fun initVariables(){
        state = State.BEFORE_RECORDING
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }

        recorder?.start()
        state = State.ON_RECORDING

        view.soundVistualizerView.startVisualizing(false)
        view.recordTextView.startCountUp()
    }

    private fun startPlaying() {

        player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare()

        }

        player?.setOnCompletionListener {

            stopPlaying()
            state = State.AFTER_RECORDING
        }


        player?.start()

        state = State.ON_PLAYING


        view.soundVistualizerView.startVisualizing(true)
        view.recordTextView.startCountUp()
    }



    private fun stopPlaying() {

        player?.release()

        player = null

        state = State.AFTER_RECORDING


        view.soundVistualizerView.stopVisualizing()
        view.recordTextView.stopCountUp()
    }

    private fun stopRecording() {


        recorder?.run {
            stop()
            release()
        }

        recorder = null
        state = State.AFTER_RECORDING

        view.soundVistualizerView.stopVisualizing()
        view.recordTextView.stopCountUp()
    }

}