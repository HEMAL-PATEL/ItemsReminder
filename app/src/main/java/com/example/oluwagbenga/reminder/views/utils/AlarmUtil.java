package com.example.oluwagbenga.reminder.views.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.oluwagbenga.reminder.models.Reminder;
import com.example.oluwagbenga.reminder.views.AddReminderActivity;

import java.util.Calendar;

import static com.example.oluwagbenga.reminder.views.utils.AlarmReceiver.ACTION_REMINDER;
import static com.example.oluwagbenga.reminder.views.utils.AlarmReceiver.EXTRA_DATA;

public class AlarmUtil {


    /**
     *
     * @param calendar carries date and time for alarm
     * @param reminder is the database entity
     * @param context is activity or fragment context.
     */
    public static void setAlarmManager(Calendar calendar, Reminder reminder, Context context){
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_DATA, reminder);
        int id = (int)System.currentTimeMillis(); //For identifying every pending intent
        intent.setAction(ACTION_REMINDER);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
                id, intent, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

    }
}
