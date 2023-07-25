package com.keeprecipes.android.data.entities;

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

    public RecipeFts(@NonNull String title, String instructions, String cuisine) {
        this.title = title;
        this.instructions = instructions;
        this.cuisine = cuisine;
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
}