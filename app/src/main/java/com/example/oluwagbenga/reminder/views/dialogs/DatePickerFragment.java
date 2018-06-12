package com.example.oluwagbenga.reminder.views.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.oluwagbenga.reminder.viewmodels.ReminderViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private Context mContext;
    public static final String MONTH ="SOMEMONTH";
    public static final String DAY = "DAY_OF_MONTH";
    public static final String YEAR ="YEAR";

    private int year;
    private int month;
    private int day;

    public static DatePickerFragment newInstance(int dayOfMonth, int month, int year){
        DatePickerFragment dateFrag = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MONTH, month);
        bundle.putInt(DAY, dayOfMonth);
        bundle.putInt(YEAR, year);
        dateFrag.setArguments(bundle);
        return dateFrag;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final String date = year +"-" +month + "-" +dayOfMonth;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);

        SimpleDateFormat spf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);

        ReminderViewModel vModel = ViewModelProviders.of(getActivity()).get(ReminderViewModel.class);
        vModel.getDate().setValue(spf.format(cal.getTime()));
       // if (mDateListener == null)return;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        if (getArguments() != null) {
             year = getArguments().getInt(YEAR);
             month = getArguments().getInt(MONTH);
             day = getArguments().getInt(DAY);
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


}
