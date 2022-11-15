package com.example.a5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

//Kod został pierwotnie napisany w javie podczas wczesnego przygotowywania się do laboratorium na
//podstawie przykładu, dlatego jednego zadanie zrealizowano w języku Kotlin a jedno w jęzku Java
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String SMS_REC_ACTION =
            "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SmsBroadcastReceiver.SMS_REC_ACTION)) {
            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();
            String numberToRespond = null;
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    sb.append(smsMessage.getDisplayMessageBody());
                    numberToRespond = smsMessage.getDisplayOriginatingAddress();
                }
            }
            displayToastIfUrgentMessage(context, sb);
            parseNumberAndRespondWithOneNumberHigherIfLowerThanTen(sb, numberToRespond);
        }
    }

    private void parseNumberAndRespondWithOneNumberHigherIfLowerThanTen(StringBuilder sb, String numberToRespond) {
        try {
            int number = Integer.parseInt(sb.toString());
            if (number < 10) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(numberToRespond, null, String.valueOf(number + 1), null, null);
            }
        } catch (Exception ignored) {
        }
    }

    private void displayToastIfUrgentMessage(Context context, StringBuilder sb) {
        if (sb.toString().startsWith("PILNE")) {
            Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
        }
    }
}

