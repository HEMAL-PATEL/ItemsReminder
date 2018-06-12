package com.example.oluwagbenga.reminder.views.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
