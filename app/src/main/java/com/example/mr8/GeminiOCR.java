package com.example.mr8;

public class GeminiOCR {

    public static String extractMedicineName(String rawText) {

        if (rawText == null) return "Unknown Medicine";

        String[] lines = rawText.split("\n");

        for (String l : lines) {

            if (!l.toLowerCase().contains("tablet")
                    && !l.toLowerCase().contains("mg")
                    && l.length() > 3) {

                return l.trim();
            }
        }

        return "Unknown Medicine";
    }
}
