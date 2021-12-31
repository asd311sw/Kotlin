package com.example.myframeapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragAndDropPermissions
import android.view.DragEvent
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myframeapplication.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity(){

    private lateinit var view:ActivityMainBinding
    private val imageUrIList = mutableListOf<Uri>()
    private var imageViewList = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)

        imageViewList.add(view.firstImage1)
        imageViewList.add(view.firstImage2)
        imageViewList.add(view.firstImage3)
        imageViewList.add(view.secondImage1)
        imageViewList.add(view.secondImage2)
        imageViewList.add(view.secondImage3)

        initAddPhotoButton()
        initStartPhotoButton()
    }


    private fun initAddPhotoButton(){


        view.addBtn.setOnClickListener {
            when{
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
                ->navigatePhotos()

                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ->showPermissionContextPopUp()

                else
                -> requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)


            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        when(requestCode){
            1000->{
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    navigatePhotos()
                else
                    Toast.makeText(this,"권한을 거부하셨습니다.",Toast.LENGTH_LONG).show()

            }
            else->{


            }

        }

    }

    private fun navigatePhotos(){

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        startActivityForResult(intent,2000)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode!=Activity.RESULT_OK)
            return

        when(requestCode){
            2000->{

                val imageUrI = data?.data

                if(imageUrI!=null){

                    if(imageUrIList.size >= 6) {
                        Toast.makeText(this, "공간이 부족합니다.", Toast.LENGTH_LONG).show()
                        return
                    }

                    imageUrIList.add(imageUrI)
                    imageViewList[imageUrIList.size - 1].setImageURI(imageUrI)
                }
                else{
                    Toast.makeText(this,"사진을 가져오지 못했습니다.",Toast.LENGTH_LONG).show()
                }


            }
            else->{
                Toast.makeText(this,"사진을 가져오지 못했습니다.",Toast.LENGTH_LONG).show()

            }

        }

    }



    private fun showPermissionContextPopUp(){
        AlertDialog.Builder(this).setTitle("권한이 팔요합니다.")
            .setMessage("사진을 불러오기 위해 권한이 필요합니다.")
            .setPositiveButton("동의하기"){_,_ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
            .setNegativeButton("취소하기"){_,_->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
            }
            .create()
            .show()
    }




    private fun initStartPhotoButton(){
        Log.i("startActivity","${imageUrIList.size}")

        view.startBtn.setOnClickListener {

            val intent = Intent(this,PhotoFrameActivity::class.java)

            imageUrIList.forEachIndexed { index, uri ->
                intent.putExtra("photo$index",uri.toString())
            }

            intent.putExtra("photoListSize",imageUrIList.size)




            startActivity(intent)
        }

    }
}