package com.example.mr8;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AppNotification {

    public static final String CHANNEL_ID = "med_channel";

    // ================= CHANNEL =================
    public static void createChannel(Context c) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Medicine Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.enableVibration(true);
            channel.enableLights(true);

            NotificationManager nm = c.getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }
    }

    // ================= SHOW MEDICINE NOTIFICATION =================
    public static void show(Context c, String id, String name) {

        createChannel(c);

        // ===== TAKEN ACTION =====
        Intent taken = new Intent(c, ActionReceiver.class);
        taken.setAction("TAKEN");
        taken.putExtra("id", id);
        taken.putExtra("name", name);

        PendingIntent takenPI = PendingIntent.getBroadcast(
                c,
                (id + "taken").hashCode(),
                taken,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // ===== SNOOZE 5 =====
        Intent snooze5 = new Intent(c, ActionReceiver.class);
        snooze5.setAction("SNOOZE");
        snooze5.putExtra("id", id);
        snooze5.putExtra("name", name);
        snooze5.putExtra("min", 5);

        PendingIntent snooze5PI = PendingIntent.getBroadcast(
                c,
                (id + "s5").hashCode(),
                snooze5,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // ===== SNOOZE 10 =====
        Intent snooze10 = new Intent(c, ActionReceiver.class);
        snooze10.setAction("SNOOZE");
        snooze10.putExtra("id", id);
        snooze10.putExtra("name", name);
        snooze10.putExtra("min", 10);

        PendingIntent snooze10PI = PendingIntent.getBroadcast(
                c,
                (id + "s10").hashCode(),
                snooze10,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(c, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("💊 Medicine Reminder")
                        .setContentText("Time to take: " + name)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setAutoCancel(true)
                        .addAction(0, "✔ Taken", takenPI)
                        .addAction(0, "⏰ 5 min", snooze5PI)
                        .addAction(0, "⏰ 10 min", snooze10PI);

        NotificationManager nm =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        if (nm != null) {
            nm.notify(id.hashCode(), builder.build());
        }
    }
}
