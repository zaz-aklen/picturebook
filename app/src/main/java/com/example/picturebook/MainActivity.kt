package com.example.picturebook

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var artnamearray= ArrayList<String>()
        var arrayimage=ArrayList<Bitmap>()
        var arrayAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,artnamearray)
        list_view.adapter=arrayAdapter
        try {
            var database=this.openOrCreateDatabase("art",Context.MODE_PRIVATE,null)
            database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR,image BLOB)")
            val cursor=database.rawQuery("SELECT * FROM arts",null)
            val c1=cursor.getColumnIndex("name")
            val c2=cursor.getColumnIndex("image")
            cursor.moveToFirst()
            while(cursor!=null){
                artnamearray.add(cursor.getString(c1))
                var ByteArray=cursor.getBlob(c2)
                var image=BitmapFactory.decodeByteArray(ByteArray,0,ByteArray.size)
                arrayimage.add(image)
                arrayAdapter.notifyDataSetChanged()
            }
            cursor?.close()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        list_view.onItemClickListener=AdapterView.OnItemClickListener { parent, view, position, id ->
        var intent=Intent(applicationContext,Main2Activity::class.java)
            intent.putExtra("name",artnamearray[position])
            intent.putExtra("info","old")
            globals.chosen.image=arrayimage[position]
        startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.add_file,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.item){
            val intent=Intent(applicationContext,Main2Activity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
