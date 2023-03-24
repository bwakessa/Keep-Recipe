package com.keeprecipes.android.presentationLayer.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.keeprecipes.android.dataLayer.entities.Recipe;
import com.keeprecipes.android.databinding.RecipeItemBinding;
import com.keeprecipes.android.presentationLayer.home.HomeFragmentDirections.ActionNavigationHomeToRecipeDetailFragment;
import com.keeprecipes.android.utils.Util;
import com.squareup.picasso.Picasso;

import java.io.File;

public class RecipeAdapter extends ListAdapter<Recipe, RecipeAdapter.ViewHolder> {

    private static final String TAG = "RecipeAdapter";

    public RecipeAdapter() {
        super(Recipe.DIFF_CALLBACK);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
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

        private final CardView cardView;

        public ViewHolder(RecipeItemBinding binding) {
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
                Picasso.get()
                        .load(file)
                        .fit()
                        .centerCrop()
                        .into(recipeImage);
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
}
