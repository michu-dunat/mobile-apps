package com.example.birthdaytracker

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.birthdaytracker.adapter.ItemAdapter
import com.example.birthdaytracker.adapter.OnItemClickListener
import com.example.birthdaytracker.dao.PersonDao
import com.example.birthdaytracker.database.Database
import com.example.birthdaytracker.databinding.ActivityMainBinding
import com.example.birthdaytracker.model.Person
import com.example.birthdaytracker.receiver.AlarmReceiver
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.util.*

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

        if (people.isEmpty()) {
            binding.noPeoplePresent.visibility = View.VISIBLE
        }

        binding.recyclerView.adapter = ItemAdapter(this, people, this)

        setupNotifications()
    }

    private fun setupNotifications() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 4)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_IMMUTABLE)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_HALF_DAY, pIntent
        )
        //        alarmManager.setInexactRepeating(
        //            AlarmManager.ELAPSED_REALTIME_WAKEUP, 5000,
        //            60000, pIntent
        //        )
    }

    override fun onItemClick(position: Int) {
        if (isTodayBirthdayOfGivenPerson(position)) {
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("sms",
                        DialogInterface.OnClickListener { dialog, id ->
                            openSmsIntentWithSelectedPersonAndWishes(position)
                        })
                    setNeutralButton("Edycja",
                        DialogInterface.OnClickListener { dialog, id ->
                            editPerson(position)
                        })
                    setNegativeButton("mail",
                        DialogInterface.OnClickListener { dialog, id ->
                            // open mail intent
                            if (people[position].email != "") {
                                openEmailIntentWithSelectedPersonAndWishes(position)
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "E-mail jest pusty!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }

                builder
                    .setTitle(people[position].firstName.plus(" ").plus(people[position].lastName))
                    .setMessage("Co chcesz zrobić?")

                builder.create()
            }
            alertDialog?.show()
        } else {
            editPerson(position)
        }
    }

    private fun openSmsIntentWithSelectedPersonAndWishes(position: Int) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:".plus(people[position].phoneNumber))
            putExtra(
                "sms_body",
                getRandomWish()
            )
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun openEmailIntentWithSelectedPersonAndWishes(position: Int) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf(people[position].email))
            putExtra(Intent.EXTRA_SUBJECT, "Wszystkiego najlepszego z okazji urodzin!")
            putExtra(Intent.EXTRA_TEXT, getRandomWish())
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
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
                R.id.clearDatabase -> {
                    clearDatabase()
                }
                R.id.exportToCalendar -> {
                    exportBirthdaysToCalendar()
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
            if (people.isEmpty()) {
                binding.noPeoplePresent.visibility = View.VISIBLE
            } else {
                binding.noPeoplePresent.visibility = View.INVISIBLE
            }
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

    private fun manageWriteCalendarPermission() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_CALENDAR
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_CALENDAR),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun exportBirthdaysToCalendar() {
        manageWriteCalendarPermission()

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_CALENDAR
        )

        if (permission == PackageManager.PERMISSION_GRANTED) {
            insertEventsIntoCalendarForEachPerson()
            Toast.makeText(this, "Wyeksportowano wydarzenia do kalendarza :)", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "Aplikacja nie ma dostępu do kalendarza!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun insertEventsIntoCalendarForEachPerson() {
        for (person in people) {
            val calID: Long = 1
            val startMillis: Long = Calendar.getInstance().run {
                set(
                    LocalDate.now().year,
                    person.birthday!!.split("-")[1].toInt() - 1,
                    person.birthday.split("-")[2].toInt(),
                    6,
                    0
                )
                timeInMillis
            }

            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.RRULE, "FREQ=YEARLY")
                put(CalendarContract.Events.DURATION, "PT1H")
                put(CalendarContract.Events.TITLE, "Urodziny")
                put(
                    CalendarContract.Events.DESCRIPTION,
                    person.firstName.plus(" ").plus(person.lastName)
                        .plus(" obchodzi dzisiaj urodziny!")
                )
                put(CalendarContract.Events.CALENDAR_ID, calID)
                put(
                    CalendarContract.Events.EVENT_TIMEZONE,
                    TimeZone.getDefault().id
                )
            }
            contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        }
    }

    private fun runAddPersonActivityFromImport(number: String, name: String) {
        val intent = Intent(this, AddPersonActivity::class.java)
        intent.putExtra("number", number)
        intent.putExtra("name", name)
        addPersonActivityLauncher.launch(intent)
    }

    private fun clearDatabase() {
        runBlocking {
            personDao.nuke()
        }
        people = listOf()
        updateRecyclerView()
        binding.noPeoplePresent.visibility = View.VISIBLE
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

    private fun getRandomWish(): String {
        return arrayOf(
            "Z okazji urodzin składam Ci moc życzeń: uśmiechu, zdrowia, radości, mnóstwa prezentów i gości, przyjaźni wielkich i małych, wielu przygód niebywałych i uśmiechu wesołego i wszystkiego, wszystkiego najlepszego!",
            "Aby wszystkie fajne dni w żółwim tempie upływały, by co dzień uśmiechał się do Ciebie świat cały, by nigdy nie było porannej pobudki i wiał wiatr co rozwiewa smutki.",
            "Bardzo wielkiego tortu urodzinowego, świeczek zapalonych, marzeń spełnionych, prezentów bez liku.",
            "Bukiet najwspanialszych życzeń: uśmiechu i szczęścia, radości każdego dnia oraz wszelkiej pomyślności.",
            "Jak najmniej trosk, słońca każdego ranka, dużo zdrowia i uśmiechu.",
            "Jak strumyk bystrej wody po zielonej płynie niwie tak niech płynie wiek Twój młody mile i szczęśliwie.",
            "Kolorowych snów, uśmiechu od ucha do ucha, pięknych bajek na dobranoc, własnego psa i kota, co dzień nowych przygód, butów siedmiomilowych, gwiazdki z nieba, wspaniałych przyjaciół i wesołej rodzinki.",
            "Minęło już tyle lat, a my ciągle za pan brat. Pozwól, ze życzenia złoże, pieniędzy, miłości i więcej szczęścia w przyszłości."
        ).random()
    }
}