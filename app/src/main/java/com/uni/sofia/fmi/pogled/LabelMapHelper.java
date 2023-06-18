package com.uni.sofia.fmi.pogled;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LabelMapHelper {

    private static final String LABELS_AND_TRANSLATIONS_FILE = "labels_translation_bg.txt";

    public HashMap<String,String> getLabelsAndTranslations(AssetManager assets) {

        HashMap<String,String> fileContents = new HashMap<>();
        try {
            InputStream inputStream = assets.open(LABELS_AND_TRANSLATIONS_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                fileContents.put(parts[0],parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContents;
    }
}
