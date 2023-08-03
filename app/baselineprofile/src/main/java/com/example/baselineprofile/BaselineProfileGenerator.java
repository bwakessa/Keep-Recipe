package com.example.baselineprofile;

import androidx.benchmark.macro.junit4.BaselineProfileRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import kotlin.Unit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This test class generates a basic startup baseline profile for the target package.
 * <p>
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to the <a href="https://d.android.com/topic/performance/baselineprofiles">baseline profile documentation</a>
 * for more information.
 * <p>
 * You can run the generator with the Generate Baseline Profile run configuration,
 * or directly with {@code generateBaselineProfile} Gradle task:
 * <pre>
 * ./gradlew :app:generateReleaseBaselineProfile -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=BaselineProfile
 * </pre>
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 * <p>
 * Check <a href="https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args">documentation</a>
 * for more information about instrumentation arguments.
 * <p>
 * After you run the generator, you can verify the improvements running the {@link StartupBenchmarks} benchmark.
 **/
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BaselineProfileGenerator {
    @Rule
    public BaselineProfileRule baselineProfileRule = new BaselineProfileRule();

    @Test
    public void generate() {
        baselineProfileRule.collect("com.keeprecipes.android", scope -> {
            // This block defines the app's critical user journey. Here we are interested in
            // optimizing for app startup. But you can also navigate and scroll
            // through your most important UI.

            // Start default activity for your app
            scope.pressHome();
            scope.startActivityAndWait();

            // TODO Write more interactions to optimize advanced journeys of your app.
            // For example:
            // 1. Wait until the content is asynchronously loaded
            // 2. Scroll the feed content
            // 3. Navigate to detail screen

            // Check UiAutomator documentation for more information how to interact with the app.
            // https://d.android.com/training/testing/other-components/ui-automator

            return Unit.INSTANCE;
        });
    }
}