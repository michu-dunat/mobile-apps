package com.example.birthdaytracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "person")
data class Person(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "first_name") val firstName: String? = "",
    @ColumnInfo(name = "last_name") val lastName: String? = "",
    @ColumnInfo(name = "birthday") val birthday: String? = null,
    @ColumnInfo(name = "phone_number") val phoneNumber: String = "",
    @ColumnInfo(name = "email") val email: String = ""
) : Serializable {
    override fun toString(): String {
        return "Person(id=$id, firstName=$firstName, lastName=$lastName, birthday=$birthday, phoneNumber='$phoneNumber', email='$email')"
    }
}
