package com.keeprecipes.android.presentationLayer.addRecipe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.keeprecipes.android.databinding.IngredientItemBinding;

public class IngredientAdapter extends ListAdapter<Ingredient, com.keeprecipes.android.presentationLayer.addRecipe.IngredientAdapter.ViewHolder> {

    public IngredientAdapter() {
        super(Ingredient.DIFF_CALLBACK);
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextInputEditText ingredientInputTextView;
        private TextInputEditText quantityInputTextView;

        public ViewHolder(IngredientItemBinding binding) {
            super(binding.getRoot());
            // Define click listener for the ViewHolder's View
            ingredientInputTextView = binding.ingredientTextInputEditText;
            quantityInputTextView = binding.qtyTextInputEditText;
        }

        public void bind(Ingredient ingredient) {
            ingredientInputTextView.setText(ingredient.name);
            quantityInputTextView.setText(String.valueOf(ingredient.size));
        }
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
        Ingredient ingredient = getItem(position);
        holder.bind(ingredient);
        Log.d("Ingedient Adapter", "onBindViewHolder: " + getItemCount());
//        qtyInputEditText.setText("");
//        ingredientInputEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                ingredients.get(holder.getAdapterPosition()).name = s.toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        qtyInputEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try {
//                    int qty = Integer.parseInt(s.toString());
//                    ingredients.get(holder.getAdapterPosition()).size = Integer.parseInt(s.toString());
//                } catch (Exception ignored) {
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }
}
