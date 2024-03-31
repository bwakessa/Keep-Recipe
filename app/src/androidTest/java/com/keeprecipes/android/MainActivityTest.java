package com.keeprecipes.android;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import static java.util.EnumSet.allOf;

import android.content.Context;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

@LargeTest
@HiltAndroidTest
public class MainActivityTest {

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        hiltRule.inject();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void mainActivityTest2() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.keeprecipes.android", appContext.getPackageName());
        onView(withId(R.id.add_recipe_fab)).check(matches(isDisplayed()));
//
//        ViewInteraction textInputEditText = onView(
//                allOf(withId(R.id.title_text_input_edit_text),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.title_text_input_layout),
//                                        0),
//                                0),
//                        isDisplayed()));
//        textInputEditText.perform(click());
//
//        ViewInteraction textInputEditText2 = onView(
//                allOf(withId(R.id.title_text_input_edit_text),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.title_text_input_layout),
//                                        0),
//                                0),
//                        isDisplayed()));
//        textInputEditText2.perform(replaceText("Recipe 1"), closeSoftKeyboard());
//
//        ViewInteraction textInputEditText3 = onView(
//                allOf(withId(R.id.instructions_text_input_edit_text),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.instructions_text_input_layout),
//                                        0),
//                                0),
//                        isDisplayed()));
//        textInputEditText3.perform(replaceText("Instruction 1"), closeSoftKeyboard());
//
//        ViewInteraction appCompatMultiAutoCompleteTextView = onView(
//                allOf(withId(R.id.cusine_auto_complete_text_view),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.cusine_text_input_layout),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatMultiAutoCompleteTextView.perform(click());
//
//        ViewInteraction appCompatMultiAutoCompleteTextView2 = onView(
//                allOf(withId(R.id.cusine_auto_complete_text_view),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.cusine_text_input_layout),
//                                        0),
//                                0),
//                        isDisplayed()));
//        appCompatMultiAutoCompleteTextView2.perform(replaceText("cuisine1"), closeSoftKeyboard());
//
//        ViewInteraction textInputEditText4 = onView(
//                allOf(withId(R.id.portion_text_input_edit_text),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.portion_text_input_layout),
//                                        0),
//                                0),
//                        isDisplayed()));
//        textInputEditText4.perform(replaceText("24"), closeSoftKeyboard());
//
//        ViewInteraction actionMenuItemView = onView(
//                allOf(withId(R.id.action_save), withContentDescription("Save"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.toolbar),
//                                        2),
//                                0),
//                        isDisplayed()));
//        actionMenuItemView.perform(click());
//
//        ViewInteraction button = onView(
//                allOf(withId(R.id.chip), withText("cuisine1"),
//                        withParent(allOf(withId(R.id.recipe_category_list_view),
//                                withParent(IsInstanceOf.<View>instanceOf(androidx.appcompat.widget.LinearLayoutCompat.class)))),
//                        isDisplayed()));
//        button.check(matches(isDisplayed()));
//
//        ViewInteraction frameLayout = onView(
//                allOf(withId(R.id.card_view_without_image),
//                        withParent(allOf(withId(R.id.recipe_list_view),
//                                withParent(IsInstanceOf.<View>instanceOf(androidx.appcompat.widget.LinearLayoutCompat.class)))),
//                        isDisplayed()));
//        frameLayout.check(matches(isDisplayed()));
//
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.recipe_title), withText("Recipe 1"),
//                        withParent(withParent(withId(R.id.card_view_without_image))),
//                        isDisplayed()));
//        textView.check(matches(withText("Recipe 1")));
//
//        ViewInteraction textView2 = onView(
//                allOf(withId(R.id.recipe_instructions_abbr), withText("Instruction 1"),
//                        withParent(withParent(withId(R.id.card_view_without_image))),
//                        isDisplayed()));
//        textView2.check(matches(withText("Instruction 1")));
//
//        ViewInteraction textView3 = onView(
//                allOf(withId(R.id.recipe_instructions_abbr), withText("Instruction 1"),
//                        withParent(withParent(withId(R.id.card_view_without_image))),
//                        isDisplayed()));
//        textView3.check(matches(withText("Instruction 1")));
    }

//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
//    }
}
