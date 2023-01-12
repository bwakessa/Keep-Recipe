package com.keeprecipes.android.presentationLayer.addRecipe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeViewModel extends ViewModel {
    public MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>();

    public void addIngredient() {
        List<Ingredient> ingredientList = ingredients.getValue() == null ? new ArrayList<>() : new ArrayList<>(ingredients.getValue());
        Ingredient ingredient = new Ingredient(ingredientList.size(), "", 1);
        ingredientList.add(ingredient);
        ingredients.postValue(ingredientList);
    }

    public void removeIngredient() {
        List<Ingredient> ingredientList = ingredients.getValue() == null ? new ArrayList<>() : new ArrayList<>(ingredients.getValue());
        if (ingredientList.size() > 0) {
            ingredientList.remove(ingredientList.size() - 1);
            ingredients.postValue(ingredientList);
        }
    }
}