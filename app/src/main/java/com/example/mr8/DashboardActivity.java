package com.example.mr8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mr8.ai.GeminiScannerActivity;


public class DashboardActivity extends AppCompatActivity {

    Button addBtn, listBtn, scanBtn, voiceBtn;
    CalendarView calendarView;

    String profileId;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_dashboard);

        profileId = getIntent().getStringExtra("profileId");

        if (profileId == null) {
            Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        addBtn = findViewById(R.id.addBtn);
        listBtn = findViewById(R.id.listBtn);
        scanBtn = findViewById(R.id.scanBtn);
        voiceBtn = findViewById(R.id.voiceBtn);
        calendarView = findViewById(R.id.calendar);

        addBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MedicineFormActivity.class)
                    .putExtra("profileId", profileId));
        });

        listBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MedicineListActivity.class)
                    .putExtra("profileId", profileId));
        });

        scanBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, GeminiScannerActivity.class));
        });

        voiceBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, VoiceActivity.class));
        });



        calendarView.setOnDateChangeListener((view, year, month, day) -> {

            String date = String.format("%04d-%02d-%02d",
                    year, (month + 1), day);

            Toast.makeText(this, "Selected: " + date, Toast.LENGTH_SHORT).show();
        });
    }

    // ✅ FIXED PLACE (OUTSIDE onCreate)
    @Override
    protected void onResume() {
        super.onResume();

        // ensure TTS is always ready
        TextToSpeechEngine.init(this);
    }
}
