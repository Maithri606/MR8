package com.example.mr8;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScanActivity extends AppCompatActivity {

    TextView infoText;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_scan);

        infoText = findViewById(R.id.infoText);

        infoText.setText(
                "📸 Prescription Scanner Ready\n\n" +
                        "Future Upgrade:\n" +
                        "✔ Gemini AI OCR\n" +
                        "✔ Auto medicine detection\n" +
                        "✔ Auto reminder creation"
        );
    }
}
