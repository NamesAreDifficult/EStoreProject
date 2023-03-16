package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;

public class FileUtility {

    public static void createFileWithDirectories(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

}
