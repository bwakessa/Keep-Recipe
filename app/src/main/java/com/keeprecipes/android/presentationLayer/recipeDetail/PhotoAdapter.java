package com.keeprecipes.android.presentationLayer.recipeDetail;

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
import com.keeprecipes.android.databinding.RecipeDetailPhotoItemBinding;
import com.keeprecipes.android.presentationLayer.addRecipe.PhotoDTO;

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

        private final RequestOptions options = new RequestOptions().set(Downsampler.ALLOW_HARDWARE_CONFIG, true);
        private final ImageView recipeImageView;

        public ViewHolder(RecipeDetailPhotoItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeImageView = binding.addRecipeImageView;
        }

        public void bind(PhotoDTO photo, View.OnClickListener onClickListener) {
            Glide.with(recipeImageView.getContext())
                    .load(photo.uri)
                    .apply(options)
                    .into(recipeImageView);
        }
    }
}
