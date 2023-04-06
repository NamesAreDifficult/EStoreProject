package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;

/**
 * Utility Class for static file related functions
 * 
 * @author Brendan Battisti
 */
public class FileUtility {

    /*
     * Takes a file path and creates the file and it's parent directories if they do
     * not exist
     * 
     * @param path the path of the file
     */
    public static void createFileWithDirectories(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
          
            file.mkdirs();
            file.createNewFile();
        }
    }

}
