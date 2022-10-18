package com.example.a2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var initialReceiptValueInput: EditText
    lateinit var maxTipValueInput: EditText
    lateinit var textResult: TextView
    lateinit var ratingBar : RatingBar
    var TAG: String = "A2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "App started running")
    }

    fun calculateAndSetTotalValue(view: View?) {
        initialReceiptValueInput = findViewById(R.id.editTextNumberDecimal1)
        maxTipValueInput = findViewById(R.id.editTextNumberDecimal2)
        textResult = findViewById(R.id.result)
        ratingBar = findViewById(R.id.ratingBar)
        val editNum1Text = initialReceiptValueInput.text.toString()
        val editNum2Text = maxTipValueInput.text.toString()
        if (validateInputFields(editNum1Text, editNum2Text)) return
        val num1 = editNum1Text.toDouble()
        val num2 = editNum2Text.toDouble()
        val ratingValue = ratingBar.rating.toDouble() / ratingBar.numStars
        val result: Double = calculateTotalValue(num1, num2, ratingValue)
        displayResult(result)
    }

    private fun displayResult(result: Double) {
        textResult.text = "Całkowita kwota do zapłaty: " + result.toString() + " zł"
    }

    private fun calculateTotalValue(
        num1: Double,
        num2: Double,
        ratingValue: Double
    ): Double {
        return num1 + (num1 * ((num2 * ratingValue)/100.0))
    }

    private fun validateInputFields(
        editNum1Text: String,
        editNum2Text: String
    ): Boolean {
        if (editNum1Text.isEmpty() || editNum1Text.toDouble() == 0.0) {
            Log.e("TAG", "Empty or wrong initial receipt value")
            initialReceiptValueInput.error = "Wprowadź poprawną wartość!"
            return true;
        }
        if (editNum2Text.isEmpty() || editNum2Text.toDouble() == 0.0) {
            Log.e("TAG", "Empty or wrong tip value")
            maxTipValueInput.error = "Wprowadź poprawną wartość!"
            return true;
        }
        return false
    }
}