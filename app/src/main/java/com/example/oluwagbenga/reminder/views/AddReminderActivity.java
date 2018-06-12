package com.example.oluwagbenga.reminder.views;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.oluwagbenga.reminder.MainActivity;
import com.example.oluwagbenga.reminder.models.Reminder;
import com.example.oluwagbenga.reminder.models.ReminderRepo;
import com.example.oluwagbenga.reminder.viewmodels.ReminderViewModel;
import com.example.oluwagbenga.reminder.views.dialogs.DatePickerFragment;
import com.example.oluwagbenga.reminder.views.utils.AlarmReceiver;
import com.example.oluwagbenga.reminder.views.utils.Keyboard;
import com.example.oluwagbenga.talentbaseitemreminder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.oluwagbenga.reminder.views.utils.AlarmReceiver.ACTION_REMINDER;
import static com.example.oluwagbenga.reminder.views.utils.AlarmReceiver.EXTRA_DATA;
import static com.example.oluwagbenga.reminder.views.utils.AlarmUtil.setAlarmManager;


public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener, Observer<Boolean>, DatePickerDialog.OnDateSetListener{

    private MyEditText mTitleView;
    private MyEditText mTimeView;
    private MyEditText mDateView;
    private MyEditText mDetailView;
   // private MyEditText numOfTime;
    private AppCompatButton mSaveBtn;
    private LinearLayout baseLayout;

    private int dayOfMonth =0;
    private int year =0;
    private int month =0;

    private int hour =0;
    private int minute =0;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    public static final String ACCESS_SET_REMINDERS ="com.gbenga.reminder.ACCESS_SET_REMINDERS";
    public static final String REMINDERS ="com.gbenga.reminder.REMINDERS";

    private ReminderViewModel rVModel;
    public static final String TAG = AddReminderActivity.class.getSimpleName();

    public static void addNewReminder(Context context){
        /**
         * Start this activity
         */
        Intent in = new Intent(context, AddReminderActivity.class);
        context.startActivity(in);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        bindView();

        mSaveBtn.setOnClickListener(this);
        mTimeView.setOnClickListener(this);
        mDateView.setOnClickListener(this);

        //Set initial date
        initialDate();

        rVModel = ViewModelProviders.of(AddReminderActivity.this).get(ReminderViewModel.class);
        rVModel.getIsSaveSuccess().observe(AddReminderActivity.this, this);

        rVModel.getDate().observe(AddReminderActivity.this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String date) {
                if (null != date){
                    mDateView.setText(date);
                    Date d = new Date(date);
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(d.getTime());

                    dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                }
            }
        });

    }

    private void initialDate(){
        long dateInMillis = System.currentTimeMillis();
        SimpleDateFormat spf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        Date date = new Date(dateInMillis);
        mDateView.setText(spf.format(date));

        SimpleDateFormat spfDate = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        mTimeView.setText(spfDate.format(date));

        //Preset some data for time
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        hour = cal.get(Calendar.HOUR);
        minute = cal.get(Calendar.MINUTE);

        //Preset date
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
    }

    private void bindView(){
        mTimeView = findViewById(R.id.time);
        mDateView = findViewById(R.id.date);
        mDetailView = findViewById(R.id.details);
        mTitleView = findViewById(R.id.trip_title);
        mSaveBtn = findViewById(R.id.save_item);
        baseLayout = findViewById(R.id.base_layout);
      //  numOfTime = findViewById(R.id.num_of_items);
    }

    @Override
    public void onClick(View v) {
        if (v == mSaveBtn){
            ReminderRepo repo = new ReminderRepo(getApplication());
            if (mTitleView.isNull() || mDetailView.isNull()) {
                Snackbar.make(baseLayout, "Neither Title nor Details can be empty!", Snackbar.LENGTH_LONG).show();
            }else{
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.HOUR, hour);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.YEAR, year);
                setAlarmManager(cal, getReminderData(), AddReminderActivity.this); //Finally set the alarm

                //Insert data to the database
                repo.insert(getReminderData(), rVModel);
            }
        }else if (v == mTimeView){

            new TimePickerDialog(AddReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    SimpleDateFormat spf = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
                    Calendar cal = Calendar.getInstance();

                    //Set time
                    cal.set(Calendar.HOUR, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);

                    //Get set time
                    AddReminderActivity.this.hour = cal.get(Calendar.HOUR);
                    AddReminderActivity.this.minute = cal.get(Calendar.MINUTE);

                    mTimeView.setText(spf.format(cal.getTime()));
                }
            }, hour, minute, false ).show();

        }else if(v == mDateView){
            //Launch date picker
            DatePickerFragment.newInstance(dayOfMonth, month, year)
                    .show(getSupportFragmentManager(), "");

        }
    }

    private Reminder getReminderData(){
        Reminder rem = new Reminder();
        rem.setDetails(mDetailView.getValue());
        rem.setTitle(mTitleView.getValue());
        rem.setTime(mTimeView.getValue());
        rem.setDate(mDateView.getValue());
        String[] commas = mDetailView.getValue().split("\n");
        int count =commas.length ==0? 1: commas.length;
        rem.setNumberOfItems(getResources().getQuantityString(R.plurals.num_of_item, count, count));
        return rem;
    }

    @Override
    public void onChanged(@Nullable Boolean isSuccess) {
        if (isSuccess != null){
            if (isSuccess){
                Toast.makeText(AddReminderActivity.this, "Reminder was added successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddReminderActivity.this, MainActivity.class));
            }else {
                Keyboard.hide(mSaveBtn,AddReminderActivity.this);
                Snackbar.make(baseLayout, "Reminder was not added!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d(TAG, year +"_"+month+ "_"+dayOfMonth);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}
