package org.smart.backend.Utils;

import java.io.File;

public class Utils {
    public static void ensureDirectoryExists() {
        File directory = new File("C:/var/log/applications/API/dataprocessing");
        if (!directory.exists()) {
            boolean dirsCreated = directory.mkdirs();
            if (!dirsCreated) {
                throw new RuntimeException("Failed to create directory for Excel file.");
            }
        }
    }
}
