package com.keeprecipes.android.presentation.recipeDetail;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.keeprecipes.android.databinding.RecipeDetailPhotoItemBinding;
import com.keeprecipes.android.presentation.addRecipe.PhotoDTO;
import com.keeprecipes.android.presentation.photoDetail.PhotoClickListener;
import com.squareup.picasso.Picasso;

public class PhotoAdapter extends ListAdapter<PhotoDTO, PhotoAdapter.ViewHolder> {
    private final PhotoClickListener photoClickListener;

    public PhotoAdapter(PhotoClickListener photoClickListener) {
        super(PhotoDTO.DIFF_CALLBACK);
        this.photoClickListener = photoClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        PhotoAdapter.ViewHolder viewHolder = new PhotoAdapter.ViewHolder(RecipeDetailPhotoItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        viewHolder.recipeImageView.setOnClickListener(v -> photoClickListener.photoClicked(viewHolder.getAbsoluteAdapterPosition()));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
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

        public void bind(PhotoDTO photo) {
            Picasso.get()
                    .load(photo.uri)
                    .fit()
                    .centerCrop()
                    .into(recipeImageView);
        }
    }
}
