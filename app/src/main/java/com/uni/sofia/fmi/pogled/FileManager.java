package com.uni.sofia.fmi.pogled;

import static com.uni.sofia.fmi.pogled.Constants.TEMP_FILE_FOLDER;
import static com.uni.sofia.fmi.pogled.Constants.TEMP_FILE_NAME;

import android.widget.Toast;

import java.io.File;

public class FileManager {

    private final File tempFile;

    public FileManager(File filesDir) {
        File imagePath = new File(filesDir, TEMP_FILE_FOLDER);
        imagePath.mkdir();

        tempFile = new File(imagePath.getPath(), TEMP_FILE_NAME);
    }

    public void deleteTempFile() {
        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        } catch (Exception e) {
            Toast.makeText(null, "Can`t take picture", Toast.LENGTH_SHORT).show();
        }
    }

    public File getTempFile() {
        return tempFile;
    }
}
