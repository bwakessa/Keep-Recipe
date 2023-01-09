package com.keeprecipes.android.dataLayer;

import androidx.room.TypeConverter;

import com.keeprecipes.android.dataLayer.entities.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Converters {

    @TypeConverter
    public static Date timeStampToDate(Long value) {
        return value == null ? null : new Date(value);
        // {whatever operation} BOOLEAN statement ? TRUE result : FALSE result
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String instructionsToString(List<String> instructions) {
        if (instructions == null) {return null;}
        else {
            StringBuilder s = new StringBuilder(instructions.get(0));
            for (int i = 1; i < instructions.size(); i ++){
                s.append(String.format(",%s", instructions.get(i)));
            }
            return s.toString();
        }
    }

    @TypeConverter
    public static ArrayList<String> stringToInstructions(String s) {
        // Assume that string is comma seperated
        if (s == null) {return null;}
        else {
            String[] inst = s.split(",");
            List<String> instr = Arrays.asList(inst);
            return new ArrayList<String>(instr);
        }
    }
//Need type converters for Collections: ArrayList<Recipe> to String (fields separated by commas, recipes separated by |)
//Need type converter to convert from String (following above format) to ArrayList<Recipe>
    //PROBLEM: how to instantiate new Recipe objects? (lack of constructor; create a getNewInstance method?)

    @TypeConverter
    public static String recipeToString(Recipe recipe) {
        if (recipe == null) {return null;}
        else {
            StringBuilder s = new StringBuilder();
            s.append(String.valueOf(recipe.getId()));
            s.append(",");
            s.append(String.valueOf(recipe.getTitle()));
            s.append(",");
            s.append(String.valueOf(recipe.getCuisine()));
            s.append(",");
            s.append((instructionsToString(recipe.getInstructions())));
            s.append(",");
            s.append((instructionsToString(recipe.getIngredients())));
            s.append(",");
            s.append(String.valueOf(dateToTimestamp((recipe.getDateCreated()))));

            return s.toString();
        }
    }

    @TypeConverter
    public static Recipe stringToRecipe(String s) {
    // Precondition: <s> follows format specified in the above function
        if (s == null) {return null;}
        else {
            Recipe r = new Recipe();
            String[] data = s.split(",");
            r.setId(Integer.parseInt(data[0]));
            r.setTitle(data[1]);
            r.setCuisine(data[2]);
            r.setInstructions(stringToInstructions(data[3]));
            r.setIngredients(stringToInstructions(data[4]));
            r.setDateCreated(new Date(Long.parseLong(data[5])));

            return r;
        }
    }

    @TypeConverter
    public static String collectionToString(ArrayList<Recipe> recipes) {
        if (recipes == null) {return null;}
        else {
            StringBuilder s = new StringBuilder();
            s.append(recipeToString(recipes.get(0)));
            for (int i = 1; i < recipes.size(); i ++) {
                s.append("|");
                s.append(recipeToString(recipes.get(i)));
            }
            return s.toString();
        }
    }

    @TypeConverter
    public static ArrayList<Recipe> stringToCollection(String s) {
    //precondition: <s> follows same format as specified in above function
        if (s == null) {return null;}
        else {
            ArrayList<Recipe> recipes = new ArrayList<Recipe>();
            String[] data = s.split("\\|");
            for (String recipeData : data) {
                recipes.add(stringToRecipe(recipeData));
            }
            return recipes;
        }
    }



//    @TypeConverter
//    public static String ingredientsToString(List<String> ingredients) {
//        if (ingredients == null) {return null;}
//        else {
//            StringBuilder s = new StringBuilder(ingredients.get(0));
//            for (int i = 1; i < ingredients.size(); i ++){
//                s.append(String.format("|%s", ingredients.get(i)));
//            }
//            return s.toString();
//        }
//    }
//
//    @TypeConverter
//    public static List<String> stringToIngredients(String s) {
//        // Assume string is | seperated
//        if (s == null) {return null;}
//        else {
//            String[] ing = s.split("");
//            List<String> ingr = Arrays.asList(ing);
//            return new ArrayList<String>(ingr);
//        }
//    }
}
