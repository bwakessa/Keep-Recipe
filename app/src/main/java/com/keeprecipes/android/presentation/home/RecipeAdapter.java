package com.keeprecipes.android.presentation.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.keeprecipes.android.data.entities.Recipe;
import com.keeprecipes.android.databinding.RecipeItemBinding;
import com.keeprecipes.android.databinding.RecipeItemWithoutImageBinding;
import com.keeprecipes.android.presentation.home.HomeFragmentDirections.ActionNavigationHomeToRecipeDetailFragment;
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
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case 0 ->
                    viewHolder = new TextCardViewHolder(RecipeItemWithoutImageBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
            case 1 ->
                    viewHolder = new ImageCardViewHolder(RecipeItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
            default -> throw new IllegalStateException("Unexpected value: " + viewType);
        }
        viewHolder.itemView.setOnClickListener(v -> {
            ActionNavigationHomeToRecipeDetailFragment action = HomeFragmentDirections.actionNavigationHomeToRecipeDetailFragment();
            action.setRecipeId((int) getCurrentList().get(viewHolder.getAbsoluteAdapterPosition()).recipeId);
            Navigation.findNavController(viewHolder.itemView).navigate(action);
        });
        return viewHolder;
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

        public ImageCardViewHolder(RecipeItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeTitle = binding.recipeTitle;
            recipeImage = binding.recipeImage;
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
        }
    }

    public static class TextCardViewHolder extends RecyclerView.ViewHolder {
        private final TextView recipeTitle;
        private final TextView recipeInstructions;

        public TextCardViewHolder(RecipeItemWithoutImageBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeTitle = binding.recipeTitle;
            recipeInstructions = binding.recipeInstructionsAbbr;
        }

        public void bind(Recipe recipe) {
            recipeTitle.setText(recipe.title);
            recipeInstructions.setText(recipe.instructions);
        }
    }
}
