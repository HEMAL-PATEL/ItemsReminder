package com.example.oluwagbenga.reminder.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "reminder_table")
public class Reminder implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name ="number_of_items")
    private String numberOfItems;

    @ColumnInfo(name ="time")
    private String time;

    @ColumnInfo(name ="title")
    private String title;

    @ColumnInfo(name = "details")
    private String details;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "where_item")
    private String whereItem;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNumberOfItems(String numberOfItems){
        this.numberOfItems = numberOfItems;
    }

    public String getNumberOfItems() {
        return numberOfItems;
    }

    public void setWhereItem(String whereItem){
        this.whereItem = whereItem;
    }

    public String getWhereItem() {
        return whereItem;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void onAddClick(){

    }
}
