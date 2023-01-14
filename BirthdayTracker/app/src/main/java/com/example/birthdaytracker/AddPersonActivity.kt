package com.example.birthdaytracker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.birthdaytracker.dao.PersonDao
import com.example.birthdaytracker.database.Database
import com.example.birthdaytracker.databinding.ActivityAddPersonBinding
import com.example.birthdaytracker.model.Person
import kotlinx.coroutines.runBlocking

class AddPersonActivity : AppCompatActivity() {
    private lateinit var personDao: PersonDao
    private lateinit var binding: ActivityAddPersonBinding
    private var loadedPerson: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "birthday-tracker-database"
        ).build()

        personDao = db.personDao()

        loadedPerson = intent.getSerializableExtra("person") as Person?
        if (loadedPerson != null) {
            binding.firstName.setText(loadedPerson!!.firstName)
            binding.lastName.setText(loadedPerson!!.lastName)
            binding.birthday.updateDate(
                loadedPerson!!.birthday!!.split("-")[0].toInt(),
                loadedPerson!!.birthday!!.split("-")[1].toInt() - 1,
                loadedPerson!!.birthday!!.split("-")[2].toInt()
            )
            binding.phoneNumber.setText(loadedPerson!!.phoneNumber)
            binding.email.setText(loadedPerson!!.email)
            binding.add.visibility = View.INVISIBLE
        } else {
            val name: String? = intent.getStringExtra("name")
            if (name != null) {
                if (name.contains(" ")) {
                    binding.firstName.setText(name.split(" ")[0])
                    binding.lastName.setText(name.split(" ")[1])
                } else {
                    binding.firstName.setText(name)
                }
            }
            val number: String? = intent.getStringExtra("number")
            if (number != null) {
                binding.phoneNumber.setText(number)
            }
            binding.update.visibility = View.INVISIBLE
            binding.delete.visibility = View.INVISIBLE
        }
    }

    fun addPerson(view: View?) {
        val person = Person(
            firstName = binding.firstName.text.toString(),
            lastName = binding.lastName.text.toString(),
            birthday = "${binding.birthday.year}-%02d-%02d".format(
                binding.birthday.month + 1,
                binding.birthday.dayOfMonth
            ),
            phoneNumber = binding.phoneNumber.text.toString(),
            email = binding.email.text.toString(),
        )
        runBlocking {
            personDao.insert(person)
        }
        setResult(RESULT_OK)
        finish()
    }

    fun updatePerson(view: View?) {
        var person = Person(
            firstName = binding.firstName.text.toString(),
            lastName = binding.lastName.text.toString(),
            birthday = "${binding.birthday.year}-%02d-%02d".format(
                binding.birthday.month + 1,
                binding.birthday.dayOfMonth
            ),
            phoneNumber = binding.phoneNumber.text.toString(),
            email = binding.email.text.toString(),
        )
        person.id = loadedPerson!!.id
        runBlocking {
            personDao.update(person)
        }
        setResult(RESULT_OK)
        finish()
    }

    fun deletePerson(view: View?) {
        runBlocking {
            personDao.delete(loadedPerson!!)
        }
        setResult(RESULT_OK)
        finish()
    }
}