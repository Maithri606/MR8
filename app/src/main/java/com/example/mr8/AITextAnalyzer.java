package com.example.mr8;

public class AITextAnalyzer {

    // Detect language using Unicode + smart fallback
    public static String detectLanguage(String text) {

        if (text == null) return "en";

        // Hindi
        if (text.matches(".*[\\u0900-\\u097F].*")) {
            return "hi";
        }

        // Kannada
        if (text.matches(".*[\\u0C80-\\u0CFF].*")) {
            return "kn";
        }

        return "en";
    }

    // AI-style enhancement (clean speech output)
    public static String enhanceText(String text) {

        if (text == null) return "";

        text = text.replaceAll("\\d+mg", "dosage")
                .replaceAll("tablet", "medicine")
                .replaceAll("capsule", "medicine");

        return "Reminder: " + text;
    }
}
