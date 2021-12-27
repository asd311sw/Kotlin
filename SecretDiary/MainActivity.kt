package com.example.mysecretdiaryapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.RED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.example.mysecretdiaryapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var view:ActivityMainBinding

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)


        view.numberPicker1.apply {
            minValue = 0
            maxValue = 9
        }

        view.numberPicker2.apply {
            minValue = 0
            maxValue = 9
        }

        view.numberPicker3.apply {
            minValue = 0
            maxValue = 9
        }

        view.lockBtn.setOnClickListener {

            if(changePasswordMode)
                Toast.makeText(this,"패스워드 변경 중 입니다.",Toast.LENGTH_LONG).show()
            else {
                val pwPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
                val password =
                    "${view.numberPicker1.value}${view.numberPicker2.value}${view.numberPicker3.value}"

                if (pwPreferences.getString("password", "000") == password) {

                    val intent = Intent(this,DiaryActivity::class.java)

                    startActivity(intent)

                }
                else
                    showAlertDialog()
            }

        }


        view.changeBtn.setOnClickListener {


            if(changePasswordMode){
                val pwPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
                val password =
                    "${view.numberPicker1.value}${view.numberPicker2.value}${view.numberPicker3.value}"

                pwPreferences.edit(true) {


                    putString("password",password)
                }

                changePasswordMode = false
                view.changeBtn.setBackgroundColor(Color.BLACK)

                Toast.makeText(this, "패스워드가 변경되었습니다.", Toast.LENGTH_LONG).show()

            }
            else {

                val pwPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
                val password =
                    "${view.numberPicker1.value}${view.numberPicker2.value}${view.numberPicker3.value}"

                if (pwPreferences.getString("password", "000") == password) {
                    changePasswordMode = true

                    Toast.makeText(this, "변경할 패스워드를 입력해주십시오.", Toast.LENGTH_LONG).show()

                    view.changeBtn.setBackgroundColor(Color.RED)
                }
                else
                    showAlertDialog()


            }



        }
        
        


    }

    fun showAlertDialog(){
        AlertDialog.Builder(this).setTitle("오류")
            .setMessage("비밀번호가 일치하지 않습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()


    }
}