package com.keeprecipes.android.presentationLayer.addRecipe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.keeprecipes.android.databinding.IngredientItemBinding;

public class IngredientAdapter extends ListAdapter<IngredientDTO, IngredientAdapter.ViewHolder> {

    public IngredientAdapter() {
        super(IngredientDTO.DIFF_CALLBACK);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        return new IngredientAdapter.ViewHolder(IngredientItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull com.keeprecipes.android.presentationLayer.addRecipe.IngredientAdapter.ViewHolder holder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        IngredientDTO ingredient = getItem(position);
        holder.bind(ingredient);
        Log.d("Ingredient Adapter", "onBindViewHolder: " + getItemCount());
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final IngredientItemBinding ingredientItemBinding;

        public ViewHolder(IngredientItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            ingredientItemBinding = binding;
        }

        public void bind(IngredientDTO ingredient) {
            ingredientItemBinding.setIngredient(ingredient);
        }
    }
}
