package com.example.oluwagbenga.reminder.views.utils;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.oluwagbenga.reminder.models.Reminder;
import com.example.oluwagbenga.reminder.models.ReminderRepo;
import com.example.oluwagbenga.reminder.views.settings.SettingsFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.oluwagbenga.reminder.views.utils.AlarmReceiver.ACTION_REMINDER;
import static com.example.oluwagbenga.reminder.views.utils.AlarmReceiver.EXTRA_DATA;


public class AlarmRescheduleService extends LifecycleService {

    public static final String ACTION_RESCHEDULE ="com.devmike.talentbase.ACTION_RESCHEDULE";
    public static final String TAG =AlarmRescheduleService.class.getSimpleName();

    public static void rescheduleAlarm(Context context){
        Intent in = new Intent(context, AlarmRescheduleService.class);
        context.startService(in);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "Rescheduling Alarm after reboot");

        final long now = System.currentTimeMillis();
        ReminderRepo repo = new ReminderRepo(getApplication());
        repo.getReminders().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(@Nullable List<Reminder> reminders) {
                if (reminders != null){
                    for (Reminder reminder: reminders){
                        try {
                            SimpleDateFormat formater = new SimpleDateFormat("EEE, d MMM yyyy h:mm a",
                                    Locale.ENGLISH);
                            String dateAndTime = reminder.getDate() + " " + reminder.getTime();
                            Date date = formater.parse(dateAndTime);

                            Date nowTime = new Date(now);

                            //Check for passed date and time
                            if (date.after(nowTime)){
                                //Only reschedule for alarm with future date
                                schedule(reminder, date.getTime());
                            }
                            //Log.v(TAG, dateAndTime + "_");
                        }catch (ParseException pE){
                            pE.printStackTrace();
                        }
                    }
                }
            }
        });

        return START_NOT_STICKY;
    }

    /**
     *
     * @param reminder is the serializable reminder entity that carries the data we want to send to the user
     * @param timeInMillis is the alarm time in millis.
     */
    private void schedule(Reminder reminder, long timeInMillis){
        int freqSet = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getString(SettingsFragment.TIME_FREQUENCY, "1"));

        long freq = freqSet * (60 * 1000); //User input mutiply by 1minute

        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra(EXTRA_DATA, reminder);
        int id = (int)System.currentTimeMillis(); //For identifying every pending intent
        intent.setAction(ACTION_REMINDER);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getBaseContext(),
                id, intent, 0);
        try {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                    timeInMillis, freq, alarmIntent);
            Log.v(TAG, "Alarm reschedule...");
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }

    }
}
