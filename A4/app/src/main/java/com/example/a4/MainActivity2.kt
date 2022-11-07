package com.example.a4

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity() {
    lateinit var textView1: TextView
    lateinit var textView2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        textView1 = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)
        val myLocalIntent = intent
        val myBundle = myLocalIntent.extras
        val str1 = myBundle!!.getString("val1")
        val str2 = myBundle.getString("val2")
        textView1.text =str1
        textView2.text =str2
        val result = str1.plus(str2)
        myBundle.putString("result", result)
        myLocalIntent.putExtras(myBundle)
        setResult(Activity.RESULT_OK, myLocalIntent)
    }

    fun exit(view: View){
        finish()
    }

}