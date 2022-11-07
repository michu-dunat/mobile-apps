package com.example.a4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var editTextContent: EditText
    lateinit var editTextPhone: EditText
    lateinit var editTextLat: EditText
    lateinit var editTextLong: EditText
    lateinit var editText1: EditText
    lateinit var editText2: EditText
    lateinit var button4: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextContent = findViewById(R.id.editTextSMSInput)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextLat = findViewById(R.id.editTextLat)
        editTextLong = findViewById(R.id.editTextLong)
        editText1 = findViewById(R.id.editText1)
        editText2 = findViewById(R.id.editText2)
        button4 = findViewById(R.id.button4)
    }

    fun openContacts(view: View) {
        val intent = Intent(Intent.ACTION_DEFAULT, ContactsContract.Contacts.CONTENT_URI);
        startActivity(intent);
    }

    fun sendSms(view: View) {
        val intent =
            Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:".plus(editTextPhone.text.toString())))
        intent.putExtra("sms_body", editTextContent.text.toString())
        startActivity(intent)
    }

    fun openLocalization(view: View) {
        val geoCode =
            "geo:".plus(editTextLat.text.toString()).plus(",").plus(editTextLong.text.toString())
                .plus("&z=16")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoCode))
        startActivity(intent)
    }

    fun openAnotherActivity(view: View) {
        val myIntentA1A2 = Intent(this@MainActivity, MainActivity2::class.java)
        val myDataBundle = Bundle()
        myDataBundle.putString("val1", editText1.text.toString())
        myDataBundle.putString("val2", editText2.text.toString())
        myIntentA1A2.putExtras(myDataBundle)
        startActivityForResult(myIntentA1A2, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 101 && resultCode == RESULT_OK) {
                val myResultBundle = data?.extras
                val myResult = myResultBundle!!.getString("result")
                button4.text = myResult
            }
        } catch (e: Exception) {
            button4.text = "Problems - $requestCode $resultCode"
        }
    }
}