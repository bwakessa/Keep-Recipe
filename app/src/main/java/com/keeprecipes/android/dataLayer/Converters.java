package com.keeprecipes.android.dataLayer;

import androidx.room.TypeConverter;

import com.keeprecipes.android.dataLayer.entities.Ingredient;
import com.keeprecipes.android.dataLayer.entities.Recipe;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Converters {

    @TypeConverter
    public static Instant timeStampToDate(Long value) {
        return value == null ? null : Instant.ofEpochSecond(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Instant date) {
        return date == null ? null : date.getEpochSecond();
    }

//    @TypeConverter
//    public static String instructionsToString(List<String> instructions) {
//        if (instructions == null) {return null;}
//        else {
//            StringBuilder s = new StringBuilder(instructions.get(0));
//            for (int i = 1; i < instructions.size(); i ++){
//                s.append(String.format(",%s", instructions.get(i)));
//            }
//            return s.toString();
//        }
//    }
//
//    @TypeConverter
//    public static ArrayList<String> stringToInstructions(String s) {
//        // Assume that string is comma seperated
//        if (s == null) {return null;}
//        else {
//            String[] inst = s.split(",");
//            List<String> instr = Arrays.asList(inst);
//            return new ArrayList<String>(instr);
//        }
//    }
//Need type converters for Collections: ArrayList<Recipe> to String (fields separated by commas, recipes separated by |)
//Need type converter to convert from String (following above format) to ArrayList<Recipe>
    //PROBLEM: how to instantiate new Recipe objects? (lack of constructor; create a getNewInstance method?)

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
        //precondition: <s> follows same format as specified in above function
        if (s == null) {
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
                    .map(ingredient -> ingredient.name + "`" + ingredient.size)
                    .reduce((str, s) -> str + "|" + s)
                    .orElse("");
        }
    }

    @TypeConverter
    public static List<Ingredient> stringToIngredients(String s) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (s == null) {
            return null;
        } else {
            String[] i = s.split("\\|");
            for (String x : i) {
                String[] y = x.split("`");
                Ingredient ingredient = new Ingredient(y[0], Integer.parseInt(y[1]));
                ingredients.add(ingredient);
            }
            return ingredients;
        }
    }
}
