package com.example.birthdaytracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.birthdaytracker.dao.PersonDao
import com.example.birthdaytracker.model.Person

@Database(entities = [Person::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun personDao(): PersonDao
}