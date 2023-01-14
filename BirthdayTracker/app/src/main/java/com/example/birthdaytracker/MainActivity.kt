package com.example.birthdaytracker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.birthdaytracker.adapter.ItemAdapter
import com.example.birthdaytracker.adapter.OnItemClickListener
import com.example.birthdaytracker.dao.PersonDao
import com.example.birthdaytracker.database.Database
import com.example.birthdaytracker.databinding.ActivityMainBinding
import com.example.birthdaytracker.model.Person
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var personDao: PersonDao
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var people: List<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setupNavigationMenu()
        }

        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java, "birthday-tracker-database"
        ).build()

        personDao = db.personDao()

        runBlocking {
            people = personDao.getAllPeople()
        }

        binding.recyclerView.adapter = ItemAdapter(this, people, this)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, AddPersonActivity::class.java)
        intent.putExtra("person", people.get(position))
        addPersonActivityLauncher.launch(intent)
    }

    private fun ActivityMainBinding.setupNavigationMenu() {
        toggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.addPerson -> {
                    runAddPersonActivity()
                }
                R.id.importPerson -> {
                    importContact()
                }
                R.id.printPeople -> {
                    printPeopleInConsole()
                }
                R.id.clearDatabase -> {
                    clearDatabase()
                }
            }
            true
        }
    }

    private fun runAddPersonActivity() {
        val intent = Intent(this, AddPersonActivity::class.java)
        addPersonActivityLauncher.launch(intent)
    }

    private val addPersonActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            runBlocking {
                people = personDao.getAllPeople()
            }
            binding.recyclerView.swapAdapter(ItemAdapter(this, people, this), false)
        }
    }

    private fun importContact() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }
        importContactActivityLauncher.launch(intent)
    }

    private val importContactActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val contactUri: Uri? = it.data?.data
            val projection: Array<String> = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Data.DISPLAY_NAME,
            )
            if (contactUri != null) {
                contentResolver.query(contactUri, projection, null, null, null).use { cursor ->
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            val numberIndex =
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val number = cursor.getString(numberIndex)
                            val nameIndex =
                                cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)
                            val name = cursor.getString(nameIndex)
                            runAddPersonActivityFromImport(number, name)
                        }
                        cursor.close()
                    }
                }
            }
        }
    }

    private fun runAddPersonActivityFromImport(number: String, name: String) {
        val intent = Intent(this, AddPersonActivity::class.java)
        intent.putExtra("number", number)
        intent.putExtra("name", name)
        addPersonActivityLauncher.launch(intent)
    }

    private fun printPeopleInConsole() {
        runBlocking {
            for (person in personDao.getAllPeople()) println(person)
        }
    }

    private fun clearDatabase() {
        runBlocking {
            personDao.nuke()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}