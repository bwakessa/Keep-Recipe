package com.keeprecipes.android.dataLayer.entities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "categories")
public class Categories {
    public static final DiffUtil.ItemCallback<Categories> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Categories oldItem, @NonNull Categories newItem) {
            return Objects.equals(oldItem.categoriesId, newItem.categoriesId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Categories oldItem, @NonNull Categories newItem) {
            return oldItem.equals(newItem);
        }
    };

    @PrimaryKey(autoGenerate = true)
    public long categoriesId;

    @ColumnInfo(name = "name")
    public String name;

    public Categories(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categories categories = (Categories) o;
        return Objects.equals(categoriesId, categories.categoriesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoriesId);
    }
}
