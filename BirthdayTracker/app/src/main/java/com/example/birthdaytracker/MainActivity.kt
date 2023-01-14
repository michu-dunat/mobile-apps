package com.example.birthdaytracker

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.view.View
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
import java.time.LocalDate

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
        if (isTodayBirthdayOfGivenPerson(position)) {
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("sms",
                        DialogInterface.OnClickListener { dialog, id ->
                            // open sms intent
                        })
                    setNeutralButton("edit",
                        DialogInterface.OnClickListener { dialog, id ->
                            editPerson(position)
                        })
                    setNegativeButton("mail",
                        DialogInterface.OnClickListener { dialog, id ->
                            // open mail intent
                        })
                }

                builder
                    .setTitle(people[position].firstName.plus(" ").plus(people[position].lastName))
                    .setMessage("What would you like to do?")

                builder.create()
            }
            alertDialog?.show()
        } else {
            editPerson(position)
        }
    }

    private fun editPerson(position: Int) {
        val intent = Intent(this@MainActivity, AddPersonActivity::class.java)
        intent.putExtra("person", people[position])
        addPersonActivityLauncher.launch(intent)
    }

    private fun isTodayBirthdayOfGivenPerson(position: Int) =
        (people[position].birthday!!.split("-")[1].toInt() == LocalDate.now().monthValue
                && people[position].birthday!!.split("-")[2].toInt() == LocalDate.now().dayOfMonth)

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
            updateRecyclerView()
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

    fun sortByNameDown(view: View?) {
        people = people.sortedWith(compareBy({ it.firstName }, { it.lastName }))
        updateRecyclerView()
    }

    fun sortByNameUp(view: View?) {
        people = people.sortedWith(compareBy({ it.firstName }, { it.lastName })).reversed()
        updateRecyclerView()
    }

    fun sortByBirthdayUp(view: View?) {
        people = people.sortedBy { it.birthday }
        updateRecyclerView()
    }

    fun sortByBirthdayDown(view: View?) {
        people = people.sortedByDescending { it.birthday }
        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        binding.recyclerView.swapAdapter(ItemAdapter(this, people, this), false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
    }
}