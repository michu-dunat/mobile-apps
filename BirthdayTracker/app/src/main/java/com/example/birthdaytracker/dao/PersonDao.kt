package com.example.birthdaytracker.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.birthdaytracker.model.Person

@Dao
interface PersonDao {

    @Query("SELECT * FROM person")
    suspend fun getAllPeople(): List<Person>

    @Insert
    suspend fun insert(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Update
    suspend fun update(person: Person)

    @Query("DELETE FROM person")
    suspend fun nuke()
}