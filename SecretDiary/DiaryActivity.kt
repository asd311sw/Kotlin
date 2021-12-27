package com.example.mysecretdiaryapplication

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.example.mysecretdiaryapplication.databinding.ActivityDiaryBinding

class DiaryActivity:AppCompatActivity() {

    private lateinit var view:ActivityDiaryBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        view = ActivityDiaryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val contentPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)


        view.editText.setText(contentPreferences.getString("diary",""))



        val thread = Thread(Runnable {
            kotlin.run {

                getSharedPreferences("diary", Context.MODE_PRIVATE).edit {

                    putString("diary",view.editText.text.toString())

                }

            }
        })




        view.editText.addTextChangedListener {

            contentPreferences.edit {

                putString("diary",view.editText.text.toString())
            }


            handler.removeCallbacks(thread)
            handler.postDelayed(thread,5000)

        }



    }




}