package com.example.oluwagbenga.reminder;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.oluwagbenga.reminder.models.Reminder;
import com.example.oluwagbenga.reminder.models.ReminderRepo;
import com.example.oluwagbenga.reminder.viewmodels.ReminderViewModel;
import com.example.oluwagbenga.talentbaseitemreminder.R;
import com.example.oluwagbenga.talentbaseitemreminder.databinding.DetailLayoutBinding;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    public static final String EXTRA_DATA ="com.example.oluwagbenga.EXTRA_DATA";
    public static final String TAG = DetailActivity.class.getSimpleName();

    private TextToSpeech tts;
    private Reminder reminder;
    private ReminderViewModel viewModel;

    public static void openDetailPage(Context context, Reminder data){
        Intent in = new Intent(context, DetailActivity.class);
        in.putExtra(EXTRA_DATA, data);
        context.startActivity(in);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bind views to the class
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/fabrica.otf");
        DetailLayoutBinding binding =DataBindingUtil.setContentView(this, R.layout.detail_layout);
        binding.titleDetail.setTypeface(tf);
        //Set data
        reminder = (Reminder) getIntent().getSerializableExtra(EXTRA_DATA);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.detail_items,
                reminder.getDetails().split("\n"));
        binding.detailList.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);

        ActionBar ab = getSupportActionBar();
        tts = new TextToSpeech(this, this);

        Log.d(TAG, reminder.getTitle());
        if (ab != null){
            ab.setTitle(R.string.details);
        }

        viewModel.getDelete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null){
                    if (aBoolean){
                        Toast.makeText(getApplicationContext(), "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "Sorry, I'm unable to delete that item.!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.setReminder(reminder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_delete:
                final ReminderRepo repo = new ReminderRepo(getApplication());

                new AlertDialog.Builder(DetailActivity.this)
                        .setMessage("Do you really want to delete this item?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                repo.delete(reminder, viewModel);
                            }
                        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.action_speak:
                tts.speak(reminder.getDetails(),
                        TextToSpeech.QUEUE_ADD, null);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "Sorry, I can't read now.", Toast.LENGTH_SHORT).show();
            }else{
                Log.d(TAG, "Started reading");
            }
        }else{
            Toast.makeText(this, "Sorry, I can't find my glasses.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
    }


}
