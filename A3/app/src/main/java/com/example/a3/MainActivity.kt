package com.example.a3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
    }

    fun input(view: View) {
        if (view.id == R.id.buttonClear) {
            textView.text = ""
            return
        }

        if (isSignButton(view) && (isSignAlreadyInInput() || textView.text.isEmpty())) return

        if(textView.text.toString().contains('=')) {
            textView.text = ""
        }

        if(view.id == R.id.buttonResult && textView.text.toString().isNotEmpty() && isSignAlreadyInInput()) {
            calculate()
            return
        }

        val button: Button = view as Button
        textView.text = textView.text.toString().plus(button.text)
    }

    private fun isSignButton(view: View) =
        (view.id == R.id.buttonPlus || view.id == R.id.buttonMinus ||
                view.id == R.id.buttonStar || view.id == R.id.buttonSlash)

    private fun isSignAlreadyInInput(): Boolean {
        return textView.text.contains('+') || textView.text.contains('-') ||
                textView.text.contains('*') || textView.text.contains('/')
    }

    private fun calculate() {
        val index: Int = textView.text.indexOfAny(charArrayOf('+', '-', '*', '/'))
        val firstNumber: Int = textView.text.subSequence(0, index).toString().toInt()
        val secondNumber: Int = textView.text.subSequence(index + 1, textView.text.length).toString().toInt()
        if (textView.text[index] == '+') {
            setResultInText(firstNumber + secondNumber)
        } else if (textView.text[index] == '-') {
            setResultInText(firstNumber - secondNumber)
        } else if (textView.text[index] == '*') {
            setResultInText(firstNumber * secondNumber)
        } else if (textView.text[index] == '/') {
            if(secondNumber == 0) {
                textView.text = ""
                return
            }
            setResultInText(firstNumber / secondNumber)
        }
    }

    private fun setResultInText(result: Int) {
        textView.text = textView.text.toString().plus("=").plus(result)
    }
}