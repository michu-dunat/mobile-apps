package com.example.a1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val duration = Toast.LENGTH_LONG;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "onCreate", duration).show()
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "onStart", duration).show()
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "onResume", duration).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Toast.makeText(this, "onSaveInstanceState", duration).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "onPause", duration).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "onStop", duration).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "onDestroy", duration).show()
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "onRestart", duration).show()
    }
}