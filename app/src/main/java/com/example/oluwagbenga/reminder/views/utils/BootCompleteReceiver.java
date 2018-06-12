package com.example.oluwagbenga.reminder.views.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

    public static final String TAG = BootCompleteReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.BOOT_COMPLETED".equals(action)){
            Log.d(TAG, "Device Restarted.");

            //Set the alarm again.
            AlarmRescheduleService.rescheduleAlarm(context);
        }
    }
}
