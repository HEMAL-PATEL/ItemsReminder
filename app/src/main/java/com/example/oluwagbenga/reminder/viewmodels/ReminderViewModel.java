package com.example.oluwagbenga.reminder.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.oluwagbenga.reminder.views.adapters.BaseAdapter;

public class ReminderViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> isSaveSuccess;
    private MutableLiveData<String> date;
    private MutableLiveData<String> time;
    private MutableLiveData<BaseAdapter> adapter;
    private MutableLiveData<Boolean> delete;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<BaseAdapter> getAdapter() {
        if (adapter == null){
            adapter = new MutableLiveData<>();
        }
        return adapter;
    }

    public MutableLiveData<Boolean> getDelete() {
        if (delete == null){
            delete = new MutableLiveData<>();
        }
        return delete;
    }

    public MutableLiveData<Boolean> getIsSaveSuccess() {
        if (isSaveSuccess ==null){
            isSaveSuccess = new MutableLiveData<>();
        }
        return isSaveSuccess;
    }

    public LiveData<String> getTime() {
        if (time == null){
            time = new MutableLiveData<>();
        }
        return time;
    }

    public MutableLiveData<String> getDate() {
        if (date == null){
            date = new MutableLiveData<>();
        }
        return date;
    }
}
