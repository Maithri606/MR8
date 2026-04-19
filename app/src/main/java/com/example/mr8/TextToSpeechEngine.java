package com.example.mr8;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TextToSpeechEngine {

    private static TextToSpeech tts;
    private static boolean ready = false;

    public static void init(Context context) {

        if (ready) return;

        tts = new TextToSpeech(context.getApplicationContext(), status -> {

            if (status == TextToSpeech.SUCCESS) {

                tts.setPitch(1.0f);
                tts.setSpeechRate(0.95f);

                ready = true;
            }
        });
    }

    public static void speak(String text, String langCode) {

        if (tts == null || text == null) return;

        Locale locale;

        switch (langCode) {

            case "hi-IN":
                locale = new Locale("hi", "IN");
                break;

            case "kn-IN":
                locale = new Locale("kn", "IN");
                break;

            default:
                locale = Locale.US;
        }

        tts.setLanguage(locale);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ai_voice");
    }

    public static void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
            ready = false;
        }
    }
}
