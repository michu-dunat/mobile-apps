package com.example.a5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String ABORT_PHONE_NUMBER = "+48666606362";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);
            if ((phoneNumber != null) && phoneNumber.equals(MyBroadcastReceiver.ABORT_PHONE_NUMBER)) {
                Toast.makeText(context,
                        "NEW_OUTGOING_CALL intercepted to number "
                                + ABORT_PHONE_NUMBER + " - aborting call",
                        Toast.LENGTH_LONG).show();
                if (this.getResultData() != null) {
                    this.setResultData(null);
                }
            }
        }
    }
}
