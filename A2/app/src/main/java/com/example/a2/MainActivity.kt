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
    lateinit var ratingBar: RatingBar
    var TAG: String = "A2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "App started running")
    }

    fun calculateAndSetTotalValue(view: View?) {
        initialReceiptValueInput = findViewById(R.id.initialReceiptValueEditText)
        maxTipValueInput = findViewById(R.id.maxTipValueEditText)
        textResult = findViewById(R.id.result)
        ratingBar = findViewById(R.id.ratingBar)
        val initialReceiptValueText = initialReceiptValueInput.text.toString()
        val maxTipValueText = maxTipValueInput.text.toString()
        if (validateInputFields(initialReceiptValueText, maxTipValueText)) return
        val num1 = initialReceiptValueText.toDouble()
        val num2 = maxTipValueText.toDouble()
        val ratingValue = ratingBar.rating.toDouble() / ratingBar.numStars
        val result: Double = calculateTotalValue(num1, num2, ratingValue)
        displayResult(result)
    }

    private fun displayResult(result: Double) {
        Log.i(TAG, "Displayed value to be paid")
        textResult.text = "Całkowita kwota do zapłaty: " + result.toString() + " zł"
    }

    private fun calculateTotalValue(
        num1: Double,
        num2: Double,
        ratingValue: Double
    ): Double {
        Log.i(TAG, "Calculated total value to be paid")
        return num1 + (num1 * ((num2 * ratingValue) / 100.0))
    }

    private fun validateInputFields(
        initialReceiptValueText: String,
        maxTipValueText: String
    ): Boolean {
        if (initialReceiptValueText.isEmpty() || initialReceiptValueText.toDouble() == 0.0) {
            Log.e("TAG", "Empty or wrong initial receipt value")
            initialReceiptValueInput.error = "Wprowadź poprawną wartość!"
            return true;
        }
        if (maxTipValueText.isEmpty() || maxTipValueText.toDouble() == 0.0) {
            Log.e("TAG", "Empty or wrong tip value")
            maxTipValueInput.error = "Wprowadź poprawną wartość!"
            return true;
        }
        return false
    }
}