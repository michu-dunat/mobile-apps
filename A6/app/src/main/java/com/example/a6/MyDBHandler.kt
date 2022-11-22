package com.example.a6

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

class MyDBHandler(
    context: Context, name: String?,
    factory: SQLiteDatabase.CursorFactory?, version: Int
) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_QUANTITY + " INTEGER," +
                ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "productDB.db"
        val TABLE_PRODUCTS = "products"

        val COLUMN_ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_QUANTITY = "quantity"
    }

    fun addProduct(product: Product) {
        if (product.name!!.lowercase(Locale.ROOT) == "name") {
            return;
        }
        val query =
            "INSERT INTO $TABLE_PRODUCTS ($COLUMN_NAME, $COLUMN_QUANTITY) VALUES (\"${product.name}\",\"${product.quantity}\")"
        val db = this.writableDatabase
        db.execSQL(query)
        db.close()
    }

    fun findProduct(name: String): Product? {
        if (name.lowercase(Locale.ROOT) == "name")
            return null
        val query = "SELECT * FROM $TABLE_PRODUCTS WHERE $COLUMN_NAME =  \"$name\""
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var product: Product? = null
        if (cursor.moveToFirst()) {
            product = Product(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                Integer.parseInt(cursor.getString(2))
            )
            cursor.close()
        }
        db.close()
        return product
    }

    fun findAllProducts(): ArrayList<String> {
        val query = "SELECT * FROM $TABLE_PRODUCTS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        val products = ArrayList<String>()
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id = Integer.parseInt(cursor.getString(0))
                val name = cursor.getString(1)
                val quantity = Integer.parseInt(cursor.getString(2))
                val product = Product(id, name, quantity)
                products.add(product.toString())
                cursor.moveToNext()
            }
            cursor.close()
        }
        db.close()
        return products
    }

    fun deleteProduct(name: String) {
        if (name.lowercase(Locale.ROOT) == "name")
            return
        val query = "DELETE FROM $TABLE_PRODUCTS WHERE $COLUMN_NAME = \"$name\""
        val db = this.writableDatabase
        db.execSQL(query)
        db.close()
    }
}