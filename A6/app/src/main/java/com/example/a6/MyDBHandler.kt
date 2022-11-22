package com.example.a6

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteConstraintException
import java.util.*
import kotlin.collections.ArrayList

class MyDBHandler(
    context: Context, name: String?,
    factory: SQLiteDatabase.CursorFactory?, version: Int
) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_MAJORS_TABLE = ("CREATE TABLE " + TABLE_MAJORS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT UNIQUE,"
                + COLUMN_SPECIALTY + " TEXT" +
                ")")
        db.execSQL(CREATE_MAJORS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MAJORS")
        onCreate(db)
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "productDB.db"
        val TABLE_MAJORS = "majors"

        val COLUMN_ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_SPECIALTY = "specialty"
    }

    fun addMajor(major: Major): String {
        if (major.name!!.lowercase(Locale.ROOT) == "name") {
            return "Forbidden name";
        }
        val query =
            "INSERT INTO $TABLE_MAJORS ($COLUMN_NAME, $COLUMN_SPECIALTY) VALUES (\"${major.name}\",\"${major.specialty}\")"
        val db = this.writableDatabase
        try {
            db.execSQL(query)
        } catch (exception: SQLiteConstraintException) {
            db.close()
            return "Names must be unique"
        }
        db.close()
        return "Product added"
    }

    fun editMajor(major: Major): String {
        if (major.name!!.lowercase(Locale.ROOT) == "name") {
            return "Forbidden name";
        }
        val query = "UPDATE $TABLE_MAJORS SET $COLUMN_NAME = \"${major.name}\", $COLUMN_SPECIALTY = \"${major.specialty}\" WHERE $COLUMN_ID = \"${major.id}\""
        println(query)
        val db = this.writableDatabase
        try {
            db.execSQL(query)
        } catch (exception: SQLiteConstraintException) {
            db.close()
            return "Error while updating"
        }
        db.close()
        return "Updated successfully";
    }

    fun findAllMajors(): ArrayList<String> {
        val query = "SELECT * FROM $TABLE_MAJORS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        val majors = ArrayList<String>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id = Integer.parseInt(cursor.getString(0))
                val name = cursor.getString(1)
                val specialty = cursor.getString(2)
                val major = Major(id, name, specialty)
                majors.add(major.toString())
                cursor.moveToNext()
            }
            cursor.close()
        }
        db.close()
        return majors
    }

    fun deleteMajor(majorId: Int) {
        val query = "DELETE FROM $TABLE_MAJORS WHERE $COLUMN_ID = \"$majorId\""
        val db = this.writableDatabase
        db.execSQL(query)
        db.close()
    }
}