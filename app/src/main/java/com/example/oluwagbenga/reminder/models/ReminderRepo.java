package com.example.oluwagbenga.reminder.models;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.example.oluwagbenga.reminder.viewmodels.ReminderViewModel;

import java.util.List;

public class ReminderRepo {

    private ReminderDao mReminderDao;
    private LiveData<List<Reminder>> reminders;
   // private List<Reminder> remList;


    public static final String TAG = ReminderDao.class.getSimpleName();

    public ReminderRepo(Application app){
        ReminderDatabase db =ReminderDatabase.getDatabase(app);
        mReminderDao = db.getReminderDao();
        reminders = mReminderDao.getReminders();
       // remList = mReminderDao.getRemindersTime();
    }


    public void insert(Reminder reminder, ReminderViewModel mViewModel){
        new InsertReminderTask(mReminderDao, InsertReminderTask.INSERT_FLAG, mViewModel).execute(reminder);
    }

    public LiveData<List<Reminder>> getReminders() {
        return reminders;
    }

    /*  public void update(Reminder reminder, ReminderViewModel mViewModel){
        new InsertReminderTask(mReminderDao, InsertReminderTask.UPDATE_FLAG, mViewModel).execute(reminder);
    }*/

    public void delete(Reminder reminder, ReminderViewModel mViewModel){
        new InsertReminderTask(mReminderDao, InsertReminderTask.DELETE_FLAG, mViewModel).execute(reminder);
    }

    private static class InsertReminderTask extends AsyncTask<Reminder, Void, Long>{

        private ReminderDao mReminderDao;
        public static final int DELETE_FLAG =1;
        public static final int INSERT_FLAG =0;
        public static final int UPDATE_FLAG = 2;

        private int flag =-1;

        private ReminderViewModel mViewModel;

        InsertReminderTask(ReminderDao remDao, int flag, ReminderViewModel mViewModel){
            this.mReminderDao = remDao;
            this.flag = flag;
            this.mViewModel = mViewModel;
        }

        @Override
        protected Long doInBackground(Reminder... reminders) {
            Log.v(TAG, "STARTED SAVING REMINDER...");
            long id =-1;
            switch (flag){
                case DELETE_FLAG:
                    id =mReminderDao.deleteById(reminders[0].getId());
                    break;
                case INSERT_FLAG:
                    id = mReminderDao.insert(reminders[0]);
                    break;
                case UPDATE_FLAG:
                  //  id = mReminderDao.updateUserData(reminders[0]);
                    break;
            }
            return id;
        }

        @Override
        public void onPostExecute(Long id){
            Log.d(TAG, "COMPLETE OPERATION! " + id);
            switch (flag){
                case DELETE_FLAG:
                    mViewModel.getDelete().setValue(id >0);
                    break;
                case INSERT_FLAG:
                    mViewModel.getIsSaveSuccess().setValue(id >0);
                    break;
            }
        }
    }
}
