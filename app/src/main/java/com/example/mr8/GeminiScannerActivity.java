package com.example.mr8.ai;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mr8.R;

public class GeminiScannerActivity extends AppCompatActivity {

    Button scanBtn;
    TextView resultView;

    static final int CAMERA_REQUEST = 101;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_gemini_scanner);

        scanBtn = findViewById(R.id.scanBtn);
        resultView = findViewById(R.id.resultView);

        scanBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && data != null) {

            Bitmap image = (Bitmap) data.getExtras().get("data");

            resultView.setText("Analyzing prescription...");

            GeminiHelper.analyzePrescription(
                    image,
                    result -> runOnUiThread(() ->
                            resultView.setText(result)
                    )
            );
        }
    }
}
