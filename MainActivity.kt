package com.example.mykotlinexample

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinexample.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.concurrent.RunnableFuture
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var view:ActivityMainBinding

    companion object{
        const val WEIGHT = "weight"
        const val HEIGHT = "height"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)



        view.btn.setOnClickListener {
            val weight = view.editText1.text.toString().toIntOrNull() ?: 0
            val height = view.editText2.text.toString().toIntOrNull() ?: 0

            val intent = Intent(this,ResultActivity::class.java)
            intent.putExtra(WEIGHT,weight)
            intent.putExtra(HEIGHT,height)
            startActivity(intent)
        }



    }


}