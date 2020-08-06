package com.example.picturebook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.view.*
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest
import kotlin.Exception

class Main2Activity : AppCompatActivity() {
    var selectimage: Bitmap? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var intent=intent
        var info=intent.getStringExtra("info")
        if(info.equals("old")){
            var newname=intent.getStringExtra("name")
            var namestring=newname.toString()
            textView.setText(namestring)
            imageView.setImageBitmap(globals.chosen.images())
            button.visibility=View.INVISIBLE
        }else{
           val bitmap=BitmapFactory.decodeResource(applicationContext.resources,R.drawable.jerry)
            imageView.setImageBitmap(bitmap)
            textView.setText("text view")
          //  imageView.setImageBitmap(bitmap)
            button.visibility=View.VISIBLE
        }
    }
    fun searchi(view: View){
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),2)
        }else{
            var intent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1)
        }}
        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if(requestCode==2) {
    if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
        var intent= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,1)
    }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1&&resultCode== Activity.RESULT_OK){
        val image= data?.data
            try {
             selectimage=MediaStore.Images.Media.getBitmap(this.contentResolver,image)
            imageView.setImageBitmap(selectimage)}
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun save(view: View){
    var selectstring=textView.text.toString()
        var outputStream=ByteArrayOutputStream()
        selectimage?.compress(Bitmap.CompressFormat.PNG,50,outputStream)
        var ByteArray=outputStream.toByteArray()
        try {
        var newart=this.openOrCreateDatabase("art",Context.MODE_PRIVATE,null)
            newart.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR,image BLOB)")
          var sqlstring= "INSERT INTO arts (name,image) VALUES (?,?)"
            var statement=newart.compileStatement(sqlstring)
            statement.bindString(1,selectstring)
            statement.bindBlob(2,ByteArray)
            statement.execute()

        }catch (e: Exception){
            e.printStackTrace()
        }
        var intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}
