package com.example.oluwagbenga.reminder.views.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.oluwagbenga.reminder.DetailActivity;
import com.example.oluwagbenga.reminder.models.Reminder;
import com.example.oluwagbenga.reminder.views.settings.SettingsFragment;
import com.example.oluwagbenga.talentbaseitemreminder.R;

public class AlarmReceiver extends BroadcastReceiver{

    public static final String CHANNEL_ID ="CHANEL_ID";
    public static final String EXTRA_DATA ="com.example.oluwagbenga.talentbaseitemreminder.EXTRA_DATA";
    public static final String ACTION_REMINDER ="com.example.oluwagbenga.talentbaseitemreminder.views.utils.ACTION_REMINDER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("ALARMRECEIVER01", "Receiver Alarm");

        String action = intent.getAction();
        if (ACTION_REMINDER.equals(action)) {
            Reminder reminder =(Reminder) intent.getSerializableExtra(EXTRA_DATA);
            // Create an explicit intent for an Activity in your app
            Intent in = new Intent(context, DetailActivity.class);
            in.putExtra(DetailActivity.EXTRA_DATA, reminder);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, in, 0);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            long[] vibTime =new long[] { 1000, 1000, 1000, 1000, 1000 };
            boolean isVibrate = PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(SettingsFragment.VIBRATE, true);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_bubble_notif)
                    .setContentTitle("New Item Remind!")
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setContentIntent(pendingIntent)
                    .setContentText(context.getString(R.string.notif_msg, reminder.getNumberOfItems()))
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            if (isVibrate){
                mBuilder.setVibrate(vibTime);
            }

            createNotificationChannel(context);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, mBuilder.build());
            Log.d("Alarmreceived", "Alarm received!");
        }
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
