package com.example.mylottoapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.mylottoapplication.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var view:ActivityMainBinding
    private val pickNumberSet = mutableSetOf<Int>()
    private var isRun = false
    private val list = mutableListOf<Int>()
    private val textViewList = mutableListOf<TextView>()

    var idx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        textViewList.add(view.textView1)
        textViewList.add(view.textView2)
        textViewList.add(view.textView3)
        textViewList.add(view.textView4)
        textViewList.add(view.textView5)
        textViewList.add(view.textView6)


        view.numberPicker.maxValue = 45
        view.numberPicker.minValue = 1
        
        view.startBtn.setOnClickListener {

            list.clear()
            isRun = true

            list.apply {

                for(i in 1..45)
                    list.add(i)
            }

            list.shuffle()


            var numberList = pickNumberSet.toList() + list.subList(0,6-pickNumberSet.size)

            numberList = numberList.sorted()

            numberList.forEachIndexed { index, i ->

                textViewList[index].isVisible = true
                textViewList[index].text = i.toString()
            }

            isRun = false
        }


        
        view.addBtn.setOnClickListener{

            if(isRun)
                Toast.makeText(this,"번호가 생성중입니다.",Toast.LENGTH_LONG).show()
            else if(pickNumberSet.size>=6)
                Toast.makeText(this,"이미 6개의 번호를 모두 선택했습니다.",Toast.LENGTH_LONG).show()
            else if(pickNumberSet.contains(view.numberPicker.value))
                Toast.makeText(this,"이미 선택한 번호입니다.",Toast.LENGTH_LONG).show()
            else {
                pickNumberSet.add(view.numberPicker.value)
                textViewList[idx].isVisible = true
                textViewList[idx++].text = view.numberPicker.value.toString()
            }
        }



        view.clearBtn.setOnClickListener {

            idx = 0

            for(i in 0 until 6)
                textViewList[i].isVisible = false

            pickNumberSet.clear()
            list.clear()


        }

        


    }
}