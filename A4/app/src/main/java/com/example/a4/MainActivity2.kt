package com.example.a4

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity2 : AppCompatActivity() {
    lateinit var textViewNumber: TextView
    lateinit var textViewFirstAndLastName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        textViewNumber = findViewById(R.id.textView)
        textViewFirstAndLastName = findViewById(R.id.textView2)
        val myLocalIntent = intent
        val myBundle = myLocalIntent.extras
        val str1 = myBundle!!.getString("val1")
        textViewNumber.text = "Indeks: ".plus(str1)
        var result: String
        if (str1 == "248862") {
            result = "Michał Dunat, proponowana ocena: 5"
            textViewFirstAndLastName.text = "Michał Dunat"
        } else {
            result = "Nie rozpoznano studenta"
            textViewFirstAndLastName.text = result
        }
        myBundle.putString("result", result)
        myLocalIntent.putExtras(myBundle)
        setResult(Activity.RESULT_OK, myLocalIntent)
    }

    fun exit(view: View) {
        finish()
    }

}