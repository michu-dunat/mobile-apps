package com.example.a3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var textViewInput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewInput = findViewById(R.id.textViewInput)
    }

    fun input(view: View) {
        if (view.id == R.id.buttonClear) {
            textViewInput.text = "00:00.00"
            return
        }

        if (view.id == R.id.buttonStart) {
            textViewInput.text = "03:23.00"
            return
        }

//        val button: Button = view as Button
//        textViewInput.text = textViewInput.text.toString().plus(button.text)
    }
}