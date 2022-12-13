package com.example.birthdaytracker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.room.Room
import com.example.birthdaytracker.dao.PersonDao
import com.example.birthdaytracker.database.Database
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var personDao: PersonDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "birthday-tracker-database"
        ).build()

        personDao = db.personDao()
    }
    
    fun runAddPersonActivity(view: View?) {
        val intent = Intent(this, AddPersonActivity::class.java)
        addPersonActivityLauncher.launch(intent)
    }

    private val addPersonActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "New person was added!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "New person wasn't added.", Toast.LENGTH_SHORT).show()
        }
    }

    fun displayPeople(view: View?) {
        runBlocking {
            for (person in personDao.getAllPeople()) println(person)
        }
    }
}