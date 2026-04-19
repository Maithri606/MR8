package com.example.mr8;

import android.app.*;
import android.content.*;
import android.os.Build;
import android.provider.Settings;

import java.util.Calendar;

public class AlarmHelper {

    public static void setAlarm(Context c, String id, String name, String time) {

        try {
            String[] parts = time.split(":");
            int h = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, h);
            cal.set(Calendar.MINUTE, m);
            cal.set(Calendar.SECOND, 0);

            if (cal.getTimeInMillis() < System.currentTimeMillis()) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }

            Intent i = new Intent(c, ReminderReceiver.class);
            i.putExtra("id", id);
            i.putExtra("name", name);

            int req = (id + time).hashCode();

            PendingIntent pi = PendingIntent.getBroadcast(
                    c, req, i,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    !am.canScheduleExactAlarms()) {

                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
                return;
            }

            am.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(),
                    pi
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelAlarm(Context c, String id, String time) {

        try {
            Intent intent = new Intent(c, ReminderReceiver.class);

            int req = (id + time).hashCode();

            PendingIntent pi = PendingIntent.getBroadcast(
                    c,
                    req,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

            if (am != null) am.cancel(pi);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void snooze(Context c, String id, String name, int min) {

        Intent i = new Intent(c, ReminderReceiver.class);
        i.putExtra("id", id);
        i.putExtra("name", name);

        int req = (id + "_snooze").hashCode();

        PendingIntent pi = PendingIntent.getBroadcast(
                c, req, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        long trigger = System.currentTimeMillis() + (min * 60 * 1000);

        am.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                trigger,
                pi
        );
    }
}
