package com.example.mr8;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (context == null || intent == null) return;

        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");

        if (name == null) name = "Medicine";

        // ✅ AI VOICE
        VoiceManager.process(context, name, "");

        // ✅ CLEAN NOTIFICATION CALL
        AppNotification.show(context, id, name);
    }
}
