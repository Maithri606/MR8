package com.example.mr8;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class VoiceActivity extends AppCompatActivity {

    EditText inputText;
    Button speakBtn;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_voice);

        inputText = findViewById(R.id.inputText);
        speakBtn = findViewById(R.id.speakBtn);

        // init TTS engine
        TextToSpeechEngine.init(this);

        speakBtn.setOnClickListener(v -> {

            String text = inputText.getText().toString().trim();

            if (!text.isEmpty()) {
                VoiceManager.process(this, text, "");
            }
        });
    }
}
