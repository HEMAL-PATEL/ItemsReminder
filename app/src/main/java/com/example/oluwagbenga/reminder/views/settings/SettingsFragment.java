package com.example.oluwagbenga.reminder.views.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oluwagbenga.talentbaseitemreminder.R;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String TIME_FREQUENCY ="time_freq";
    public static final String VIBRATE ="vibrate_reminder";
    private EditTextPreference timeFreq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.app_settings);

        timeFreq = (EditTextPreference) findPreference(TIME_FREQUENCY);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setTimeFrequency(pref.getString(TIME_FREQUENCY, "1"));
    }

    @Override
    public void onPause(){
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (TIME_FREQUENCY.equals(key)){
            setTimeFrequency(sharedPreferences.getString(key, "1"));
        }
    }

    private void setTimeFrequency(String time){
        timeFreq.setSummary(getString(R.string.notif_freq, time));
    }
}
