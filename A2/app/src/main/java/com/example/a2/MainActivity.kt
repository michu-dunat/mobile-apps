package com.example.a2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var editNum1: EditText
    lateinit var editNum2: EditText
    lateinit var textResult: TextView
    var TAG: String = "A2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "App started running")
    }

    fun calculate(view: View?) {
        editNum1 = findViewById(R.id.editTextNumberDecimal1)
        editNum2 = findViewById(R.id.editTextNumberDecimal2)
        textResult = findViewById(R.id.result)
        val editNum1Text = editNum1.text.toString()
        val editNum2Text = editNum2.text.toString()
        if (validateInputFields(editNum1Text, editNum2Text)) return
        val num1 = editNum1Text.toDouble()
        val num2 = editNum2Text.toDouble()
        val result: Double = determineTypeOfCalculationAndPerform(view, num1, num2)
        textResult.text = java.lang.Double.toString(result)
    }

    private fun determineTypeOfCalculationAndPerform(
        view: View?,
        num1: Double,
        num2: Double
    ): Double {
        if (view == findViewById(R.id.plusButton)) {
            Log.i(TAG, "Addition")
            return num1 + num2
        } else if (view == findViewById(R.id.minusButton)) {
            Log.i(TAG, "Subtraction")
            return num1 - num2
        } else if (view == findViewById(R.id.multiplyButton)) {
            Log.i(TAG, "Multiplication")
            return num1 * num2
        } else {
            if (num2 == 0.0) {
                Log.e("TAG", "Divided by 0")
                editNum2.error = "Nie można dzielić przez 0!"
                return Double.NaN;
            }
            Log.i(TAG, "Division")
            return num1 / num2
        }
    }

    private fun validateInputFields(
        editNum1Text: String,
        editNum2Text: String
    ): Boolean {
        if (editNum1Text.isEmpty()) {
            Log.e("TAG", "First decimal is empty")
            editNum1.error = "Wprowadź liczbę!"
            return true;
        }
        if (editNum2Text.isEmpty()) {
            Log.e("TAG", "Second decimal is empty")
            editNum2.error = "Wprowadź liczbę!"
            return true;
        }
        return false
    }
}