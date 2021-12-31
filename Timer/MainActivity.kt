package com.example.mytimerapplication

import android.annotation.SuppressLint
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import com.example.mytimerapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var view:ActivityMainBinding
    private var currentCountDownTimer:CountDownTimer? = null
    private val soundPool = SoundPool.Builder().build()
    private var tickingSoundId:Int? = null
    private var bellSoundId:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)


        bindViews()
        initSounds()

    }

    override fun onResume() {
        super.onResume()

        soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()

        soundPool.autoPause()

    }



    override fun onDestroy() {
        super.onDestroy()

        soundPool.release()
    }

    private fun bindViews(){

        view.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                if(p2)
                    updateRemainTime(p1*60*1000L)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

                stopCountDown()
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

                if(p0?.progress == 0)
                    stopCountDown()
                else
                    startCountDown()

            }
        })
    }

    private fun stopCountDown(){
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null

        soundPool.autoPause()
    }


    private fun startCountDown(){
        currentCountDownTimer = createCountDownTimer(view.seekBar.progress*60*1000L).start()
        currentCountDownTimer?.start()


        tickingSoundId?.let {
            soundPool.play(it,1F,1F,0,-1,1F)
        }
    }


    private fun initSounds(){
        tickingSoundId = soundPool.load(this,R.raw.timer_ticking,1)
        bellSoundId = soundPool.load(this,R.raw.timer_bell,1)
    }

    private fun createCountDownTimer(initialMillis:Long):CountDownTimer =
        object : CountDownTimer(initialMillis,1000L){
            override fun onTick(p0: Long) {

                updateRemainTime(p0)
                updateSeekbar(p0)
            }
            override fun onFinish() {
                completeCountDown()
            }
        }


    private fun completeCountDown(){
        updateRemainTime(0)
        updateSeekbar(0)

        soundPool.autoPause()
        bellSoundId?.let {
            soundPool.play(it,1F,1F,0,0,1F)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRemainTime(remainMillis:Long){
        val remainSeconds = remainMillis/1000

        view.remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        view.remainSecondTextView.text  = "%02d".format(remainSeconds % 60)

    }


    private fun updateSeekbar(remainMillis: Long){
        view.seekBar.progress = (remainMillis/1000/60).toInt()
    }
}