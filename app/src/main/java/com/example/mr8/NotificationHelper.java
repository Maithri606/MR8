package com.example.mr8;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper {

    public static final String CHANNEL_ID = "med_channel";

    public static void showMedicineNotification(Context context, String id, String name) {

        createChannel(context);

        // TAKEN
        Intent taken = new Intent(context, ActionReceiver.class);
        taken.setAction("TAKEN");
        taken.putExtra("id", id);
        taken.putExtra("name", name);

        PendingIntent takenPI = PendingIntent.getBroadcast(
                context, 1, taken,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // SNOOZE (5 MIN + 10 MIN)
        Intent snooze5 = new Intent(context, ActionReceiver.class);
        snooze5.setAction("SNOOZE");
        snooze5.putExtra("id", id);
        snooze5.putExtra("name", name);
        snooze5.putExtra("min", 5);

        Intent snooze10 = new Intent(context, ActionReceiver.class);
        snooze10.setAction("SNOOZE");
        snooze10.putExtra("id", id);
        snooze10.putExtra("name", name);
        snooze10.putExtra("min", 10);

        PendingIntent snooze5PI = PendingIntent.getBroadcast(
                context, 2, snooze5,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        PendingIntent snooze10PI = PendingIntent.getBroadcast(
                context, 3, snooze10,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("💊 Medicine Reminder")
                        .setContentText(name)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .addAction(0, "✔ Taken", takenPI)
                        .addAction(0, "⏰ 5 min", snooze5PI)
                        .addAction(0, "⏰ 10 min", snooze10PI);

        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(id.hashCode(), builder.build());
    }

    private static void createChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Medicine Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager nm =
                    context.getSystemService(NotificationManager.class);

            nm.createNotificationChannel(channel);
        }
    }
}
