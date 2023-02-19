package com.keeprecipes.android.presentationLayer.addRecipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.request.RequestOptions;
import com.keeprecipes.android.databinding.PhotoItemBinding;

public class RecipePhotoAdapter extends ListAdapter<PhotoDTO, RecipePhotoAdapter.ViewHolder> {

    private final Photo recipePhoto;

    public RecipePhotoAdapter(Photo recipePhoto) {
        super(PhotoDTO.DIFF_CALLBACK);
        this.recipePhoto = recipePhoto;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecipePhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        return new RecipePhotoAdapter.ViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipePhotoAdapter.ViewHolder holder, int position) {
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

        private final RequestOptions options = new RequestOptions().set(Downsampler.ALLOW_HARDWARE_CONFIG, true);
        private final ImageView recipeImageView;
        private final Button deletePhotoButton;

        public ViewHolder(PhotoItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeImageView = binding.addRecipeImageView;
            deletePhotoButton = binding.deletePhotoButton;
        }

        public void bind(PhotoDTO photo, View.OnClickListener onClickListener) {
            Glide.with(recipeImageView.getContext())
                    .load(photo.uri)
                    .apply(options)
                    .into(recipeImageView);
            deletePhotoButton.setOnClickListener(onClickListener);
        }
    }
}
