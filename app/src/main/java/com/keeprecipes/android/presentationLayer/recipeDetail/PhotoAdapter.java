package com.keeprecipes.android.presentationLayer.recipeDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.keeprecipes.android.databinding.RecipeDetailPhotoItemBinding;
import com.keeprecipes.android.presentationLayer.addRecipe.PhotoDTO;
import com.squareup.picasso.Picasso;

public class PhotoAdapter extends ListAdapter<PhotoDTO, PhotoAdapter.ViewHolder> {

    private final Photo recipePhoto;

    public PhotoAdapter(Photo recipePhoto) {
        super(PhotoDTO.DIFF_CALLBACK);
        this.recipePhoto = recipePhoto;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        return new PhotoAdapter.ViewHolder(RecipeDetailPhotoItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position), view -> recipePhoto.removeItem(holder.getAdapterPosition()));
    }

    public interface Photo {
        void removeItem(int position);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView recipeImageView;

        public ViewHolder(RecipeDetailPhotoItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeImageView = binding.addRecipeImageView;
        }

        public void bind(PhotoDTO photo, View.OnClickListener onClickListener) {
            Picasso.get()
                    .load(photo.uri)
                    .fit()
                    .centerCrop()
                    .into(recipeImageView);
        }
    }
}
