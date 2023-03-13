package com.keeprecipes.android.dataLayer.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "collections")
public class Collection {
    @PrimaryKey(autoGenerate = true)
    public int collectionId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "recipes")
    public List<String> recipeId;
}