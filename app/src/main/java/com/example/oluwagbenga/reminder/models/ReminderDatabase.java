package com.example.oluwagbenga.reminder.models;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Reminder.class}, version = 2)
public abstract class ReminderDatabase extends RoomDatabase{

    public abstract ReminderDao getReminderDao();

    private static ReminderDatabase mInstance;

    public static ReminderDatabase getDatabase(final Application application) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(application.getApplicationContext(),
                    ReminderDatabase.class, "reminder_database.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

}
