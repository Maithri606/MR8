package com.example.mr8;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class AppConfig {

    public static final String CHANNEL_ID = "med_channel";

    public static void initNotificationChannel(Context c) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Medicine Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.enableVibration(true);
            channel.enableLights(true);

            NotificationManager nm =
                    c.getSystemService(NotificationManager.class);

            nm.createNotificationChannel(channel);
        }
    }
}
