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
        // {whatever operation} BOOLEAN statement ? TRUE result : FALSE result
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
    public static String recipeToString(Recipe recipe) {
        if (recipe == null) {
            return null;
        } else {
            StringBuilder s = new StringBuilder();
            s.append(recipe.recipeId);
            s.append(",");
            s.append(recipe.title);
            s.append(",");
            s.append(recipe.instructions);
            s.append(",");
            s.append(recipe.cuisine);
            s.append(",");
            s.append(listToString(recipe.collection));
            s.append(",");
            s.append(recipe.portionSize);
            s.append(",");
            s.append(ingredientsToString(recipe.ingredients));
            s.append(",");
            s.append(listToString(recipe.photos));
            s.append(",");
            s.append(dateToTimestamp((recipe.dateCreated)));

            return s.toString();
        }
    }

    @TypeConverter
    public static Recipe stringToRecipe(String s) {
        // Precondition: <s> follows format specified in the above function
        if (s == null) {
            return null;
        } else {
            Recipe r = new Recipe();
            String[] data = s.split(",");
            r.recipeId = Integer.parseInt(data[0]);
            r.title = data[1];
            r.instructions = data[2];
            r.cuisine = data[3];
            r.collection = stringToList(data[4]);
            r.portionSize = data[5];
            r.ingredients = stringToIngredients(data[6]);
            r.photos = stringToList(data[7]);
            r.dateCreated = Instant.parse(data[8]);
            return r;
        }
    }

    @TypeConverter
    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        } else {
            return stringList.stream().reduce("", (str, s1) -> str + s1 + "|");
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
            StringBuilder s = new StringBuilder(ingredients.get(0).name + "`" + ingredients.get(0).size);
            for (int i = 1; i < ingredients.size(); i++) {
                s.append(String.format("|%s`%s", ingredients.get(i).name, ingredients.get(i).size));
            }
            return s.toString();
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
