package com.keeprecipes.android.data;

import static org.junit.Assert.*;

import com.keeprecipes.android.data.entities.Ingredient;

import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ConvertersTest {

    @Test
    public void timeStampToDate() {
        assertEquals(Converters.timeStampToDate(1690612442L), Instant.parse("2023-07-29T06:34:02Z"));
    }

    @Test
    public void dateToTimestamp() {
        Instant time = Instant.now();
        assertEquals((long) Converters.dateToTimestamp(time), time.getEpochSecond());
    }

    @Test
    public void listToString() {
        List<String> stringList = new ArrayList<>();
        stringList.add("java");
        stringList.add("python");
        stringList.add("c++");
        stringList.add("go");
        assertEquals(Converters.listToString(stringList), "java|python|c++|go");
        assertNotEquals(Converters.listToString(stringList), "javapython|c++|go");
        stringList = new ArrayList<>();
        stringList.add("java");
        assertEquals(Converters.listToString(stringList), "java");
        assertNotEquals(Converters.listToString(stringList), "java|");
        stringList = new ArrayList<>();
        assertNull(Converters.listToString(stringList));
    }

    @Test
    public void stringToList() {
        String string = "java|python|c++|go";
        List<String> stringList = new ArrayList<>();
        stringList.add("java");
        stringList.add("python");
        stringList.add("c++");
        stringList.add("go");
        assertEquals(Converters.stringToList(string), stringList);
        assertNull(Converters.stringToList(""));
        stringList = new ArrayList<>();
        stringList.add("java");
        assertEquals(Converters.stringToList("java"), stringList);
        assertNull(Converters.stringToList(""));
    }

    @Test
    public void ingredientsToString() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Turmeric", "24", "g"));
        ingredients.add(new Ingredient("", "", ""));
        ingredients.add(new Ingredient("Pepper", "", ""));
        ingredients.add(new Ingredient("", "12", "mg"));
        ingredients.add(new Ingredient("", "", "g"));
        ingredients.add(new Ingredient("", "12", ""));
        ingredients.add(new Ingredient("Pepper", "4", ""));
        assertEquals(Converters.ingredientsToString(ingredients), "{\"quantity\":\"g\",\"size\":\"24\",\"name\":\"Turmeric\"}|{\"quantity\":\"\",\"size\":\"\",\"name\":\"\"}|{\"quantity\":\"\",\"size\":\"\",\"name\":\"Pepper\"}|{\"quantity\":\"mg\",\"size\":\"12\",\"name\":\"\"}|{\"quantity\":\"g\",\"size\":\"\",\"name\":\"\"}|{\"quantity\":\"\",\"size\":\"12\",\"name\":\"\"}|{\"quantity\":\"\",\"size\":\"4\",\"name\":\"Pepper\"}");
        ingredients = new ArrayList<>();
        assertNull(Converters.ingredientsToString(ingredients));
    }

    @Test
    public void stringToIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Turmeric", "24", "g"));
        ingredients.add(new Ingredient("", "", ""));
        ingredients.add(new Ingredient("Pepper", "", ""));
        ingredients.add(new Ingredient("", "12", "mg"));
        ingredients.add(new Ingredient("", "", "g"));
        ingredients.add(new Ingredient("", "12", ""));
        ingredients.add(new Ingredient("Pepper", "4", ""));
        assertEquals(Converters.stringToIngredients("{\"quantity\":\"g\",\"size\":\"24\",\"name\":\"Turmeric\"}|{\"quantity\":\"\",\"size\":\"\",\"name\":\"\"}|{\"quantity\":\"\",\"size\":\"\",\"name\":\"Pepper\"}|{\"quantity\":\"mg\",\"size\":\"12\",\"name\":\"\"}|{\"quantity\":\"g\",\"size\":\"\",\"name\":\"\"}|{\"quantity\":\"\",\"size\":\"12\",\"name\":\"\"}|{\"quantity\":\"\",\"size\":\"4\",\"name\":\"Pepper\"}"), ingredients);
        assertNull(Converters.stringToIngredients(""));
    }
}