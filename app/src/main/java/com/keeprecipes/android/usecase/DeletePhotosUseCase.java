package com.keeprecipes.android.usecase;

import java.io.IOException;
import java.util.concurrent.Executors;

public class DeletePhotosUseCase {
    public void deleteFiles(String appFilePath) {
        Executors.newSingleThreadExecutor().execute(() -> {
            String deleteCommand = "rm -rf " + appFilePath;
            Runtime runtime = Runtime.getRuntime();
            Process process;
            try {
                process = runtime.exec(deleteCommand);
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
