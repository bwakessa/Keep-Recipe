package com.keeprecipes.android.presentationLayer.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keeprecipes.android.databinding.RecipeItemBinding;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    public ArrayList<Recipe> recipes;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeTitle;

        public ViewHolder(RecipeItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            recipeTitle = binding.recipeTitle;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public RecipeAdapter(ArrayList<Recipe> dataSet) {
        recipes = dataSet;
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
        TextView recipeTitle = holder.recipeTitle;
        recipeTitle.setText(recipes.get(position).title);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
