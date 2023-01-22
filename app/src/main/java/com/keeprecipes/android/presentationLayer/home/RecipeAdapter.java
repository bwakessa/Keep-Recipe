package com.keeprecipes.android.presentationLayer.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.request.RequestOptions;
import com.keeprecipes.android.R;
import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.databinding.RecipeItemBinding;
import com.keeprecipes.android.utils.Util;

public class RecipeAdapter extends ListAdapter<Recipe, RecipeAdapter.ViewHolder> {

    private static final String TAG = "RecipeAdapter";

    protected RecipeAdapter() {
        super(Recipe.DIFF_CALLBACK);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        return new RecipeAdapter.ViewHolder(RecipeItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe recipe = getItem(position);
        holder.bind(recipe);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView recipeTitle;
        private final AppCompatImageView recipeImage;
        private final RequestOptions options = new RequestOptions().set(Downsampler.ALLOW_HARDWARE_CONFIG, true);

        public ViewHolder(RecipeItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeTitle = binding.recipeTitle;
            recipeImage = binding.recipeImage;
            binding.cardView.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_recipeDetailFragment));
        }

        public void bind(Recipe recipe) {
            recipeTitle.setText(recipe.title);
            if (!Util.isEmpty(recipe.photos)) {
                Log.d(TAG, "bind: image path " + recipeImage.getContext().getFilesDir() + recipe.photos.get(0));
                Glide.with(recipeImage.getContext())
                        .load(recipeImage.getContext().getFilesDir() + "/" + recipe.photos.get(0))
                        .apply(options)
                        .into(recipeImage);
            }
        }
    }
}
