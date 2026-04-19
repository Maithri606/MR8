package com.example.mr8;

import android.content.Context;

public class VoiceManager {

    public static void process(Context context, String text, String profileId) {

        if (text == null || text.trim().isEmpty()) return;

        TextToSpeechEngine.init(context);

        String lang = AITextAnalyzer.detectLanguage(text);

        String finalText = AITextAnalyzer.enhanceText(text);

        switch (lang) {

            case "hi":
                TextToSpeechEngine.speak(finalText, "hi-IN");
                break;

            case "kn":
                TextToSpeechEngine.speak(finalText, "kn-IN");
                break;

            default:
                TextToSpeechEngine.speak(finalText, "en-US");
        }
    }

    // simple direct speak
    public static void speak(Context context, String text) {
        TextToSpeechEngine.init(context);
        TextToSpeechEngine.speak(text, "en-US");
    }
}
