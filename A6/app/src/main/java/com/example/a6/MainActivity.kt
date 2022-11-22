package com.example.a6

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.a6.adapter.ItemAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var majorIdET: EditText
    private lateinit var nameET: EditText
    private lateinit var specialtyET: EditText
    private lateinit var addBTN: Button
    private lateinit var editBTN: Button
    private lateinit var deleteBTN: Button
    private lateinit var outputTV: TextView
    private lateinit var majorsRV: RecyclerView

    private lateinit var dbHandler : MyDBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        majorIdET = findViewById(R.id.majorId)
        nameET = findViewById(R.id.name)
        specialtyET = findViewById(R.id.specialty)
        addBTN = findViewById(R.id.add)
        editBTN = findViewById(R.id.edit)
        deleteBTN = findViewById(R.id.delete)
        outputTV = findViewById(R.id.output)
        majorsRV = findViewById(R.id.majors)

        dbHandler = MyDBHandler(this, null, null, 1)
        val majors = dbHandler.findAllMajors()
        majorsRV.adapter = ItemAdapter(this, majors, ItemAdapter.OnClickListener {major ->
            Toast.makeText(this@MainActivity, major, Toast.LENGTH_SHORT).show()
            fillEditTexts(major)
        })

        }

    private fun fillEditTexts(major: String) {
        val parts = major.split(",")
        majorIdET.setText(parts[0].split("=")[1])
        nameET.setText(parts[1].split("=")[1])
        specialtyET.setText(parts[2].split("=")[1])
    }

    fun addMajor(view: View) {
        val major = Major(nameET.text.toString(), specialtyET.text.toString())
        outputTV.text = dbHandler.addMajor(major)
        cleanInputFields()
        majorsRV.adapter?.notifyDataSetChanged()
    }

    fun editMajor(view: View) {
        val major = Major(Integer.parseInt(majorIdET.text.toString()), nameET.text.toString(), specialtyET.text.toString())
        outputTV.text = dbHandler.editMajor(major)
        cleanInputFields()
        majorsRV.adapter?.notifyDataSetChanged()
    }

    fun removeMajor(view: View) {
        dbHandler.deleteMajor(Integer.parseInt(majorIdET.text.toString()))
        cleanInputFields()
        majorsRV.adapter?.notifyDataSetChanged()
    }

    private fun cleanInputFields() {
        nameET.setText("")
        majorIdET.setText("")
        specialtyET.setText("")
    }
}