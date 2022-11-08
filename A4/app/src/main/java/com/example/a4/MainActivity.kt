package com.example.a4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var editTextLat: EditText
    lateinit var editTextLong: EditText
    lateinit var editTextStudentNumber: EditText
    lateinit var textViewFirstAndLastNameAndMark: TextView
    lateinit var textViewPhoneNumber: TextView
    private val REQUEST_SELECT_PHONE_NUMBER = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextLat = findViewById(R.id.editTextLat)
        editTextLong = findViewById(R.id.editTextLong)
        editTextStudentNumber = findViewById(R.id.editText1)
        textViewFirstAndLastNameAndMark = findViewById(R.id.textView3)
        textViewPhoneNumber = findViewById(R.id.textView4)
    }

    fun openContacts(view: View) {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }
        startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER)
    }

    fun openBluetooth(view: View) {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        startActivity(intent)
    }

    fun openLocalization(view: View) {
        val geoCode =
            "geo:".plus(editTextLat.text.toString()).plus(",").plus(editTextLong.text.toString())
                .plus("?z=16")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoCode))
        startActivity(intent)
    }

    fun openAnotherActivity(view: View) {
        val myIntentA1A2 = Intent(this@MainActivity, MainActivity2::class.java)
        val myDataBundle = Bundle()
        myDataBundle.putString("val1", editTextStudentNumber.text.toString())
        myIntentA1A2.putExtras(myDataBundle)
        startActivityForResult(myIntentA1A2, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
                val contactUri: Uri? = data?.data
                val projection: Array<String> =
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (contactUri != null) {
                    contentResolver.query(contactUri, projection, null, null, null).use { cursor ->
                        if (cursor != null) {
                            if (cursor.moveToFirst()) {
                                val numberIndex =
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                val number = cursor.getString(numberIndex)
                                textViewPhoneNumber.text = number
                            }
                        }
                    }
                }
            }
            if (requestCode == 101 && resultCode == RESULT_OK) {
                val myResultBundle = data?.extras
                val myResult = myResultBundle!!.getString("result")
                textViewFirstAndLastNameAndMark.text = myResult
            }

        } catch (e: Exception) {
            println(e)
        }
    }
}