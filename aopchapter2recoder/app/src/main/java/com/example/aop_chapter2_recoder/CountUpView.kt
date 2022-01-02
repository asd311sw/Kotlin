package com.example.aop_chapter2_recoder

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.min

class CountUpView(context: Context, attrs:AttributeSet):AppCompatTextView(context,attrs) {


    private var startTimeStamp = 0L

    private val countUpAction = object: Runnable{
        override fun run() {


            var currentTimeStamp = SystemClock.elapsedRealtime()

            val countTimeSeconds = ((currentTimeStamp-startTimeStamp)/1000L).toInt()

            updateCountTime(countTimeSeconds)

            handler?.postDelayed(this,1000L)
        }

    }


    fun startCountUp(){

        startTimeStamp = SystemClock.elapsedRealtime()
        handler?.post(countUpAction)
    }

    fun stopCountUp(){
        handler?.removeCallbacks(countUpAction)
    }

    private fun updateCountTime(countTimeSeconds:Int){


        val minutes = countTimeSeconds / 60
        val seconds = countTimeSeconds % 60

        text = "%02d:%02d".format(minutes,seconds)

    }


    fun clearCountTime(){
        updateCountTime(0)
    }

}