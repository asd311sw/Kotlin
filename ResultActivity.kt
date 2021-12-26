package com.example.mykotlinexample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import com.example.mykotlinexample.databinding.ActivitiyResultBinding
import org.w3c.dom.Text
import kotlin.random.Random

class ResultActivity:AppCompatActivity() {

    private lateinit var view:ActivitiyResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ActivitiyResultBinding.inflate(layoutInflater)
        setContentView(R.layout.activitiy_result)


        val weight = intent.getIntExtra(MainActivity.HEIGHT,0)
        val height = intent.getIntExtra(MainActivity.WEIGHT,0)

        val bmi = weight / ((height / 100F)*(height / 100F))

        val result = when{
            bmi>=35.0->"고도 비만"
            bmi>=30.0->"중정도 비만"
            bmi>=25.0->"경도 비만"
            bmi>=23.0->"과체중"
            bmi>=18.5->"정상체중"
            else->"저체중"
        }

        val bmiTextView = findViewById<TextView>(R.id.bmiTextView2)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        bmiTextView.text = bmi.toString()
        resultTextView.text = "결과는 ${result}입니다"

        
        val btn = findViewById<Button>(R.id.btn)

        btn.setOnClickListener {

            finish()

        }


    }




}
