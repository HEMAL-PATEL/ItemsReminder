package com.example.oluwagbenga.reminder.models;

import android.app.Application;
import android.arch.lifecycle.LiveData;


public class ReadReminderRepo extends ReminderRepo {

    public ReadReminderRepo(Application app) {
        super(app);
    }

}
