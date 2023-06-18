package com.uni.sofia.fmi.pogled;

import android.content.Context;
import android.content.res.AssetManager;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

public class SpeechProducer

{
    private TextToSpeech textToSpeech;

    private final HashMap<String, String> labels_and_translations;

    public SpeechProducer(Context context, AssetManager assetManager) {
        // create an object textToSpeech and adding features into it
        textToSpeech = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.forLanguageTag("BG"));
            }
        });
        labels_and_translations = new LabelMapHelper().getLabelsAndTranslations(assetManager);

    }

    public void sayObjectLabel(String label) {
        String labelBg = this.labels_and_translations.get(label);
        textToSpeech.speak(labelBg, TextToSpeech.QUEUE_ADD, null, null);
    }

    public void shutDown() {
        textToSpeech.shutdown();
    }
}
