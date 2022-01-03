package com.example.aoppart3chapter01

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.aoppart3chapter01.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var view:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        initFirebase()
        updateResult()
    }


    private fun initFirebase(){

        FirebaseMessaging.getInstance().token.addOnCompleteListener {

            if(it.isSuccessful){
                view.firebaseTokenTextView.text = it.result
                Log.i("FIrebase Token",it.result.toString())
            }

        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        updateResult(true)
    }


    private fun updateResult(isNewIntent:Boolean = false){
        view.resultTextView.text = intent.getStringExtra("notificationType") ?: "앱 런처"+
                if(isNewIntent)
                    "으로 갱신했습니다."
                else
                    "실행했습니다."

    }


}
