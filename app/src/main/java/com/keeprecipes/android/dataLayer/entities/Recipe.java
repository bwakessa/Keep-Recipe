package com.keeprecipes.android.dataLayer.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Date;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;
//  comment: why public instead of private?

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "cuisine")
    private String cuisine;

    @ColumnInfo(name = "instructions")
    private List<String> instructions;

    @ColumnInfo(name = "ingredients")
    private List<String> ingredients;
//  comment: How to deal with storing & retrieving arraylists in SQLite


//    @ColumnInfo(name = "data_created")
//    private Date dateCreated;
//  comment: should or should not include date created?
}
