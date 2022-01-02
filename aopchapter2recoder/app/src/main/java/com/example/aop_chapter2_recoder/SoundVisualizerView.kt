package com.example.aop_chapter2_recoder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SoundVisualizerView(context:Context,attrs:AttributeSet): View(context,attrs) {


    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.teal_200)

        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    private var drawingWidth = 0
    private var drawingHeight = 0
    private var drawingAmplitudes = emptyList<Int>()
    private var isReplaying = false
    private var replayingPosition = 0


    var onRequestCurrentAmplitude:(()->Int)? = null

    private val visualizeRepeatAction:Runnable = object:Runnable{
        override fun run() {

            if(!isReplaying) {
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            }
            else
                replayingPosition++

            invalidate()
            handler.postDelayed(this, ACTION_INTERVAL.toLong())
        }

    }





    companion object{
        const val LINE_WIDTH = 10F
        const val LINE_SPACE = 15F
        const val ACTION_INTERVAL = 20
        const val MAX_AMPLITUDE = Short.MAX_VALUE
    }

    fun startVisualizing(isReplaying:Boolean){

        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)

    }

    fun stopVisualizing(){


        replayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun clearVisualization(){
        drawingAmplitudes = emptyList()
        invalidate()

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)


        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val centerY = drawingHeight / 2F
        var offsetX = drawingWidth.toFloat()

        drawingAmplitudes?.let {


            if(isReplaying)
                it.takeLast(replayingPosition)
            else
                it

        }.forEach {
            val lineLength = it / MAX_AMPLITUDE * drawingHeight * 0.8F

            offsetX -= LINE_SPACE
            if (offsetX < 0) return@forEach

            canvas?.drawLine(
                offsetX, centerY - lineLength / 2F,
                offsetX, centerY + lineLength / 2F, amplitudePaint
            )
        }



    }


}