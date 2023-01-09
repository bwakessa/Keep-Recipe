package com.keeprecipes.android.ui.addRecipe;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.keeprecipes.android.databinding.IngredientItemBinding;
import com.keeprecipes.android.databinding.PhotoItemBinding;

import java.net.URI;
import java.util.ArrayList;

public class RecipePhotoAdapter extends RecyclerView.Adapter<RecipePhotoAdapter.ViewHolder> {

    public ArrayList<Uri> photoURI;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImageView;
        private Button deletePhotoButton;

        public ViewHolder(PhotoItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeImageView = binding.addRecipeImageView;
            deletePhotoButton = binding.deletePhotoButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public RecipePhotoAdapter(ArrayList<Uri> dataSet) {
        photoURI = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecipePhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        return new RecipePhotoAdapter.ViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipePhotoAdapter.ViewHolder holder, int position) {
        ImageView recipeImage = holder.recipeImageView;
        Button deletePhotoButton = holder.deletePhotoButton;
        recipeImage.setImageURI(photoURI.get(position));
        deletePhotoButton.setOnClickListener(v -> {
            photoURI.remove(position);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return photoURI.size();
    }
}
