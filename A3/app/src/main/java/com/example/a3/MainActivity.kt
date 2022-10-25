package com.example.a3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var textViewInput: TextView
    lateinit var textViewHistory: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewInput = findViewById(R.id.textViewInput)
        textViewHistory = findViewById(R.id.textViewHistory)
    }

    fun input(view: View) {
        if (view.id == R.id.buttonClear) {
            textViewInput.text = ""
            return
        }

        if (isSignButton(view) && (isSignAlreadyInInput() || textViewInput.text.isEmpty())) return

        if(textViewInput.text.toString().contains('=')) {
            textViewHistory.text = textViewHistory.text.toString().plus("\n").plus(textViewInput.text)
            textViewInput.text = ""

        }

        if(view.id == R.id.buttonResult && textViewInput.text.toString().isNotEmpty() && isSignAlreadyInInput()) {
            calculate()
            return
        }

        val button: Button = view as Button
        textViewInput.text = textViewInput.text.toString().plus(button.text)
    }

    private fun isSignButton(view: View) =
        (view.id == R.id.buttonPlus || view.id == R.id.buttonMinus ||
                view.id == R.id.buttonStar || view.id == R.id.buttonSlash)

    private fun isSignAlreadyInInput(): Boolean {
        return textViewInput.text.contains('+') || textViewInput.text.contains('-') ||
                textViewInput.text.contains('*') || textViewInput.text.contains('/')
    }

    private fun calculate() {
        val index: Int = textViewInput.text.indexOfAny(charArrayOf('+', '-', '*', '/'))
        val firstNumber: Int = textViewInput.text.subSequence(0, index).toString().toInt()
        val secondNumber: Int = textViewInput.text.subSequence(index + 1, textViewInput.text.length).toString().toInt()
        if (textViewInput.text[index] == '+') {
            setResultInText(firstNumber + secondNumber)
        } else if (textViewInput.text[index] == '-') {
            setResultInText(firstNumber - secondNumber)
        } else if (textViewInput.text[index] == '*') {
            setResultInText(firstNumber * secondNumber)
        } else if (textViewInput.text[index] == '/') {
            if(secondNumber == 0) {
                textViewInput.text = ""
                return
            }
            setResultInText(firstNumber / secondNumber)
        }
    }

    private fun setResultInText(result: Int) {
        textViewInput.text = textViewInput.text.toString().plus("=").plus(result)
    }
}