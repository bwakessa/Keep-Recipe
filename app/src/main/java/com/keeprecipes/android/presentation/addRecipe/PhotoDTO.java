package com.keeprecipes.android.presentation.addRecipe;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class PhotoDTO {
    public static final DiffUtil.ItemCallback<PhotoDTO> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull PhotoDTO oldItem, @NonNull PhotoDTO newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PhotoDTO oldItem, @NonNull PhotoDTO newItem) {
            return oldItem.equals(newItem);
        }
    };
    public final int id;
    public final Uri uri;

    public PhotoDTO(int id, Uri uri) {
        this.id = id;
        this.uri = uri;
    }

    @NonNull
    @Override
    public String toString() {
        return "PhotoDTO{" +
                "id=" + id +
                ", uri=" + uri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoDTO photoDTO = (PhotoDTO) o;
        return id == photoDTO.id && uri.equals(photoDTO.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
