package com.example.birthdaytracker.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.example.birthdaytracker.R
import com.example.birthdaytracker.database.Database
import com.example.birthdaytracker.model.Person
import kotlinx.coroutines.runBlocking
import org.apache.commons.lang3.RandomStringUtils
import java.time.LocalDate
import kotlin.random.Random


class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = RandomStringUtils.random(20)

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)

        val db = Room.databaseBuilder(
            context,
            Database::class.java, "birthday-tracker-database"
        ).build()

        val personDao = db.personDao()
        var people: List<Person>
        runBlocking {
            people = personDao.getAllPeople()
        }

        for (person in people) {
            if (isTodayBirthdayOfGivenPerson(person)) {
                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_cake_24)
                    .setContentTitle("Urodziny!")
                    .setContentText(
                        person.firstName.plus(" ").plus(person.lastName)
                            .plus(" ma dzisiaj urodziny, złóż życzenia!")
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(context)) {
                    notify(Random.nextInt(), builder.build())
                }
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        val name = "birthday-tracker-notification-channel"
        val descriptionText =
            "Birthday tracker notification channel for sending reminders of people birthdays"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun isTodayBirthdayOfGivenPerson(person: Person) =
        (person.birthday!!.split("-")[1].toInt() == LocalDate.now().monthValue
                && person.birthday!!.split("-")[2].toInt() == LocalDate.now().dayOfMonth)
}