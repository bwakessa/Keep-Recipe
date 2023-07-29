package com.keeprecipes.android.data;

import static org.junit.Assert.*;

import org.junit.Before;
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
    }

    @Test
    public void ingredientsToString() {
    }

    @Test
    public void stringToIngredients() {
    }
}