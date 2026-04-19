package com.example.mr8;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    Button startBtn;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.startBtn);

        // INIT SYSTEMS
        AppConfig.initNotificationChannel(this);
        TextToSpeechEngine.init(this);

        requestNotificationPermission();

        startBtn.setOnClickListener(v -> {

            // 👉 DIRECT FLOW FIX (better UX)
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
    }
}
