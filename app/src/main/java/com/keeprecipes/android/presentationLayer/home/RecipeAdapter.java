package com.keeprecipes.android.presentationLayer.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.databinding.RecipeItemBinding;
import com.keeprecipes.android.databinding.RecipeItemWithoutImageBinding;
import com.keeprecipes.android.presentationLayer.home.HomeFragmentDirections.ActionNavigationHomeToRecipeDetailFragment;
import com.keeprecipes.android.utils.Util;
import com.squareup.picasso.Picasso;

import java.io.File;

public class RecipeAdapter extends ListAdapter<Recipe, RecyclerView.ViewHolder> {

    private static final String TAG = "RecipeAdapter";

    public RecipeAdapter() {
        super(Recipe.DIFF_CALLBACK);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        switch (viewType) {
            case 0:
                return new TextCardViewHolder(RecipeItemWithoutImageBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
            case 1:
                return new ImageCardViewHolder(RecipeItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (getCurrentList().get(position).photos == null) {
            return 0;
        } else {
            return 1;
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = getItem(position);
        Log.d(TAG, "onBindViewHolder: " + holder.getItemViewType());
        switch (holder.getItemViewType()) {
            case 0:
                ((TextCardViewHolder) holder).bind(recipe);
                break;
            case 1:
                ((ImageCardViewHolder) holder).bind(recipe);
                break;
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ImageCardViewHolder extends RecyclerView.ViewHolder {
        private final TextView recipeTitle;
        private final AppCompatImageView recipeImage;

        private final CardView cardView;

        public ImageCardViewHolder(RecipeItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeTitle = binding.recipeTitle;
            recipeImage = binding.recipeImage;
            cardView = binding.cardView;
        }

        public void bind(Recipe recipe) {
            recipeTitle.setText(recipe.title);
            if (!Util.isEmpty(recipe.photos)) {
                File file = new File(recipeImage.getContext().getFilesDir(), recipe.photos.get(0));
//                Picasso.get().setLoggingEnabled(true);
                Picasso.get()
                        .load(file)
                        .fit()
                        .centerCrop()
                        .into(recipeImage);
            } else {
                // If there is now image then remove the imageView space from card
                recipeImage.getLayoutParams().height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                recipeImage.requestLayout();
            }
            // Rewrite for setOnClickListener
            // https://www.digitalocean.com/community/tutorials/android-recyclerview-data-binding
            cardView.setOnClickListener(view -> {
                ActionNavigationHomeToRecipeDetailFragment action = HomeFragmentDirections.actionNavigationHomeToRecipeDetailFragment();
                action.setRecipeId((int) recipe.recipeId);
                Navigation.findNavController(view).navigate(action);
            });
        }
    }

    public static class TextCardViewHolder extends RecyclerView.ViewHolder {
        private final TextView recipeTitle;
        private final TextView recipeInstructions;

        private final CardView cardView;

        public TextCardViewHolder(RecipeItemWithoutImageBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeTitle = binding.recipeTitle;
            recipeInstructions = binding.recipeInstructionsAbbr;
            cardView = binding.cardViewWithoutImage;
        }

        public void bind(Recipe recipe) {
            recipeTitle.setText(recipe.title);
            recipeInstructions.setText(recipe.instructions);
            // Rewrite for setOnClickListener
            // https://www.digitalocean.com/community/tutorials/android-recyclerview-data-binding
            cardView.setOnClickListener(view -> {
                ActionNavigationHomeToRecipeDetailFragment action = HomeFragmentDirections.actionNavigationHomeToRecipeDetailFragment();
                action.setRecipeId((int) recipe.recipeId);
                Navigation.findNavController(view).navigate(action);
            });
        }
    }
}
