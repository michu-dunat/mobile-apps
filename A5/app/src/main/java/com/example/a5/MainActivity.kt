package com.example.a5

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    lateinit var textView1: TextView
    lateinit var textView2: TextView
    lateinit var telephonyManager: TelephonyManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)


        manageReadPhoneStatePermission()
        manageReadCallLogPermission()
        manageProcessOutgoingCallsPermission()
        manageReceiveSmsPermission()

        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        textView1.text = "CallState: ".plus(telephonyManager.callState)
            .plus(", PhoneType: ").plus(telephonyManager.phoneType)
            .plus(", NetworkType: ").plus(telephonyManager.networkType)


    }

    override fun onStart() {
        super.onStart()
        val phoneStateListener: PhoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(
                state: Int, incomingNumber: String
            ) {
                textView2.text = telephonyManager.dataNetworkType.toString()
            }
        }
        telephonyManager.listen(
            phoneStateListener,
            PhoneStateListener.LISTEN_CALL_STATE
        )
        val telephonyOverview: String = telephonyManager.dataNetworkType.toString()
        textView2.text = telephonyManager.dataNetworkType.toString()
    }

    private fun manageReadPhoneStatePermission() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                1
            )
        }
    }

    private fun manageReadCallLogPermission() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CALL_LOG
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CALL_LOG),
                2
            )
        }
    }

    private fun manageProcessOutgoingCallsPermission() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.PROCESS_OUTGOING_CALLS
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.PROCESS_OUTGOING_CALLS),
                3
            )
        }
    }

    private fun manageReceiveSmsPermission() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECEIVE_SMS
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS),
                4
            )
        }
    }

    private fun manageSendSmsPermission() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                5
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission has been denied by user")
                } else {
                    Log.i("TAG", "Permission has been granted by user")
                }
            }
            2 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission has been denied by user")
                } else {
                    Log.i("TAG", "Permission has been granted by user")
                }
            }
            3 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission has been denied by user")
                } else {
                    Log.i("TAG", "Permission has been granted by user")
                }
            }
            4 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission has been denied by user")
                } else {
                    Log.i("TAG", "Permission has been granted by user")
                }
            }
            5 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission has been denied by user")
                } else {
                    Log.i("TAG", "Permission has been granted by user")
                }
            }
        }
    }


}