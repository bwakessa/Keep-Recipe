package com.keeprecipes.android.dataLayer.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "collections")
public class Collection {
    @PrimaryKey(autoGenerate = true)
    public long collectionId;

    @ColumnInfo(name = "name")
    public String name;

    public Collection(String name) {
        this.name = name;
    }
}
