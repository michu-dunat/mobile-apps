package com.example.birthdaytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import androidx.room.Room
import com.example.birthdaytracker.dao.PersonDao
import com.example.birthdaytracker.database.Database
import com.example.birthdaytracker.model.Person
import kotlinx.coroutines.runBlocking

class AddPersonActivity : AppCompatActivity() {
    private lateinit var personDao: PersonDao
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var birthday: DatePicker
    private lateinit var phoneNumber: EditText
    private lateinit var email: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)

        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "birthday-tracker-database"
        ).build()

        personDao = db.personDao()

        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        birthday = findViewById(R.id.birthday)
        phoneNumber = findViewById(R.id.phoneNumber)
        email = findViewById(R.id.email)

        val name: String? = intent.getStringExtra("name")
        if (name != null) {
            if (name.contains(" ")) {
                firstName.setText(name.split(" ")[0])
                lastName.setText(name.split(" ")[1])
            } else {
                firstName.setText(name)
            }
        }
        val number: String? = intent.getStringExtra("number")
        if (number != null) {
            phoneNumber.setText(number)
        }
    }

    fun addPerson(view: View?) {
        val person = Person(
            firstName = firstName.text.toString(),
            lastName = lastName.text.toString(),
            birthday = "${birthday.year}-%02d-%02d".format(birthday.month + 1, birthday.dayOfMonth),
            phoneNumber = phoneNumber.text.toString(),
            email = email.text.toString(),
        )
        runBlocking {
            personDao.insert(person)
        }
        setResult(RESULT_OK)
        finish()
    }
}