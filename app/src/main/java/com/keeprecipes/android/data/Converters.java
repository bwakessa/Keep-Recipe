package com.keeprecipes.android.data;

import android.util.Log;

import androidx.room.TypeConverter;

import com.keeprecipes.android.data.entities.Ingredient;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Converters {

    private static final String TAG = "Converters";

    @TypeConverter
    public static Instant timeStampToDate(Long value) {
        return value == null ? null : Instant.ofEpochSecond(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Instant date) {
        return date == null ? null : date.getEpochSecond();
    }

    @TypeConverter
    public static String listToString(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return null;
        } else {
            return stringList.stream().reduce((str, s) -> str + "|" + s).orElse("");
        }
    }

    @TypeConverter
    public static List<String> stringToList(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        } else {
            List<String> recipeId = new ArrayList<>();
            String[] data = s.split("\\|");
            Collections.addAll(recipeId, data);
            return recipeId;
        }
    }

    @TypeConverter
    public static String ingredientsToString(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return null;
        } else {
            return ingredients.stream()
                    .map(ingredient -> {
                        JSONObject serializedIngredient = new JSONObject();
                        try {
                            serializedIngredient.put("name", ingredient.name());
                            serializedIngredient.put("size", ingredient.size());
                            serializedIngredient.put("quantity", ingredient.quantity());
                        } catch (JSONException e) {
//                            Log.d(TAG, "ingredientsToString: " + e);
                            throw new RuntimeException(e);
                        }
//                        Log.d(TAG, "ingredientsToString: " + serializedIngredient.toString());
                        return serializedIngredient.toString();
                    })
                    .reduce((str, s) -> str + "|" + s)
                    .orElse("");
        }
    }

    @TypeConverter
    public static List<Ingredient> stringToIngredients(String s) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (s == null || s.isEmpty()) {
            return null;
        } else {
            String[] i = s.split("\\|");
            for (String x : i) {
                try {
                    JSONObject serializedIngredient = new JSONObject(x);
                    Ingredient ingredient = new Ingredient((String) serializedIngredient.get("name"),
                            (String) serializedIngredient.get("size"),
                            (String) serializedIngredient.get("quantity"));
                    ingredients.add(ingredient);
                } catch (JSONException e) {
                    Log.d(TAG, "stringToIngredients: " + e);
                    throw new RuntimeException(e);
                }
            }
            return ingredients;
        }
    }
}
