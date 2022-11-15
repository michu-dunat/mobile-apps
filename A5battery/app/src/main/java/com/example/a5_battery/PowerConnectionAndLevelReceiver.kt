package com.example.a5_battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class PowerConnectionAndLevelReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals(Intent.ACTION_POWER_CONNECTED)) {
            Toast.makeText(context, "Podłączono do ładowania", Toast.LENGTH_LONG).show()
        } else if (intent.action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Toast.makeText(context, "Odłączono od ładowania", Toast.LENGTH_LONG).show()
        } else if (intent.action.equals(Intent.ACTION_BATTERY_LOW)) {
            Toast.makeText(context, "Niski poziom naładowania baterii", Toast.LENGTH_LONG).show()
        } else if (intent.action.equals(Intent.ACTION_BATTERY_OKAY)) {
            Toast.makeText(context, "Bateria okay", Toast.LENGTH_LONG).show()
        }
    }
}