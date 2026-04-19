package com.example.mr8.ai;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.*;

public class GeminiHelper {

    public interface GeminiCallback {
        void onResult(String result);
    }

    private static final String API_KEY = "API-KEY";

    public static void analyzePrescription(Bitmap image, GeminiCallback callback) {

        new Thread(() -> {

            try {

                String base64Image = bitmapToBase64(image);

                OkHttpClient client = new OkHttpClient();

                String json = "{"
                        + "\"contents\":[{"
                        + "\"parts\":["
                        + "{\"text\":\"You are a medical AI. Extract medicine name, dosage, timing clearly.\"},"
                        + "{\"inlineData\":{\"mimeType\":\"image/jpeg\",\"data\":\"" + base64Image + "\"}}"
                        + "]"
                        + "}]"
                        + "}";

                RequestBody body = RequestBody.create(
                        json,
                        MediaType.get("application/json")
                );

                Request request = new Request.Builder()
                        .url("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + API_KEY)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                String result = response.body() != null
                        ? response.body().string()
                        : "No response";

                callback.onResult(result);

            } catch (IOException e) {
                callback.onResult("Error: " + e.getMessage());
            }
        }).start();
    }

    private static String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
    }
}
