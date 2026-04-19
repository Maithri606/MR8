package com.example.mr8;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

public class CalendarSync {

    public static void addEvent(Context context, String title, long timeInMillis) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        intent.putExtra(CalendarContract.Events.TITLE, title);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timeInMillis);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, timeInMillis + 60000);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
}
