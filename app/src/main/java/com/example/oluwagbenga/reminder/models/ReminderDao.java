package com.example.oluwagbenga.reminder.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {
    @Query("SELECT * FROM reminder_table ORDER by id DESC")
    LiveData<List<Reminder>> getReminders();

    @Query("SELECT * FROM reminder_table")
    List<Reminder> getRemindersTime();

    @Query("SELECT * FROM reminder_table WHERE id=:id")
    LiveData<Reminder> getReminderById(int id);

   // @Query("SELECT * FROM reminder_table WHERE pensionerId=:date")
   // LiveData<Reminder> loadUserByPensionerId(String date);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Reminder reminder);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateUserData(Reminder mUserData);

    @Query("DELETE FROM reminder_table WHERE id = :id")
    int deleteById(int id);

    @Delete
    int delete(Reminder mUserData);
}
