package com.example.oluwagbenga.reminder;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.oluwagbenga.reminder.models.Reminder;
import com.example.oluwagbenga.reminder.models.ReminderRepo;
import com.example.oluwagbenga.reminder.viewmodels.ReminderViewModel;
import com.example.oluwagbenga.reminder.views.AddReminderActivity;
import com.example.oluwagbenga.reminder.views.adapters.BaseAdapter;
import com.example.oluwagbenga.reminder.views.settings.SettingsActivity;
import com.example.oluwagbenga.talentbaseitemreminder.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class MainActivity extends AppCompatActivity implements BaseAdapter.OnDeleteReminderListener{

    private List<Reminder> mReminderList;
    private RecyclerView reminderView;
    private FloatingActionButton addRemBtn;
    private ReminderViewModel viewModel;
    private BaseAdapter adapter;
    //private ActivityMainBinding activityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //Initialize settings
        PreferenceManager.setDefaultValues(this, R.xml.app_settings, false);

        mReminderList = new ArrayList<>();
        viewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);

        reminderView = findViewById(R.id.all_reminder);

        //Bind recyclerview
        bindRecyclerView();

        addRemBtn = findViewById(R.id.fab);
        addRemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReminderActivity.addNewReminder(MainActivity.this);
            }
        });

        adapter.setOnDeleteReminderListener(this);

        viewModel.getDelete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isDeleted) {
                if (isDeleted == null)return;
                if (isDeleted){
                    Snackbar.make(findViewById(R.id.main_layout), "Item deleted successfully", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    private void bindRecyclerView(){
        adapter = new BaseAdapter(mReminderList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        reminderView.setHasFixedSize(false);
        reminderView.setItemAnimator(new SlideInLeftAnimator());
        reminderView.setLayoutManager(manager);
        reminderView.setAdapter(adapter);

        ReminderRepo mRepo = new ReminderRepo(getApplication());
        mRepo.getReminders().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(@Nullable List<Reminder> reminders) {
                if (reminders == null) return;
                mReminderList.clear();
                for (Reminder remd : reminders) {
                    Log.d("DATE_OF_MONTH", remd.getDate() +" "+ remd.getTime());
                    mReminderList.add(remd);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Reminder reminder) {
                DetailActivity.openDetailPage(MainActivity.this, reminder);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void showMenuDialog(final Reminder reminder){
        final ReminderRepo repo = new ReminderRepo(getApplication());
        AlertDialog dialog = new AlertDialog.Builder(this).
                setMessage("Do you really want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        repo.delete(reminder, viewModel);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onDeleteReminder(Reminder reminder, int position) {
        final ReminderRepo repo = new ReminderRepo(getApplication());
        repo.delete(reminder, viewModel);
       // adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, mReminderList.size());
        //showMenuDialog(reminder);
    }
}
