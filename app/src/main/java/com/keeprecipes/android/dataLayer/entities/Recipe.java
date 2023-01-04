package com.keeprecipes.android.dataLayer.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Date;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "cuisine")
    public String cuisine;

    @ColumnInfo(name = "instructions")
    public List<String> instructions;

    @ColumnInfo(name = "ingredients")
    public List<String> ingredients;

    @ColumnInfo(name = "data_created")
    private Date dateCreated;
}
