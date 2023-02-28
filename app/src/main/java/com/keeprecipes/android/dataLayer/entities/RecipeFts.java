package com.keeprecipes.android.dataLayer.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;

@Fts4(contentEntity = Recipe.class)
@Entity(tableName = "recipeFts")
public class RecipeFts {

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "instructions")
    private String instructions;

    @ColumnInfo(name = "cuisine")
    private String cuisine;

    @ColumnInfo(name = "collection")
    private String collection;

    public RecipeFts(@NonNull String title, String instructions, String cuisine, String collection) {
        this.title = title;
        this.instructions = instructions;
        this.cuisine = cuisine;
        this.collection = collection;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }
}
