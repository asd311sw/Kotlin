package com.example.myframeapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myframeapplication.databinding.ActivityPhotoframeBinding
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity:AppCompatActivity() {


    private lateinit var view:ActivityPhotoframeBinding
    private var pos = 0
    private var timer: Timer? = null
    private val imageUriList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityPhotoframeBinding.inflate(layoutInflater)
        setContentView(view.root)


        getPhotoUri()
        startTimer()
    }
    

    private fun startTimer(){

        timer = timer(period = 5 * 1000){

            runOnUiThread {
                var current = pos
                var next = if (imageUriList.size - 1 <= current) 0 else (current + 1)

                view.backImageView.setImageURI(imageUriList[current])
                view.frontImageView.alpha = 0F
                view.frontImageView.setImageURI(imageUriList[next])
                view.frontImageView.animate()
                    .alpha(1.0F)
                    .setDuration(1000)
                    .start()

                pos = next


            }
        }
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }


    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }



    private fun getPhotoUri(){

        val size = intent.getIntExtra("photoListSize",0)

        for(i in 0 until size)
            intent.getStringExtra("photo$i")?.let {
                imageUriList.add(Uri.parse(it))
            }


    }


}