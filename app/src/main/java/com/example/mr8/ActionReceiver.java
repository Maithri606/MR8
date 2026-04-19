package com.example.mr8;

import android.content.*;
import android.widget.Toast;

public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context c, Intent i) {

        String action = i.getAction();
        String id = i.getStringExtra("id");
        String name = i.getStringExtra("name");

        SQLiteHelper db = new SQLiteHelper(c);

        if ("TAKEN".equals(action)) {

            Medicine m = db.getMedicineById(id);

            if (m != null) {
                m.status = "Taken";
                db.updateMedicine(m);
            }

            Toast.makeText(c, "Marked Taken", Toast.LENGTH_SHORT).show();
        }

        else if ("SNOOZE".equals(action)) {

            int min = i.getIntExtra("min", 5);

            AlarmHelper.snooze(c, id, name, min);

            Toast.makeText(c, "Snoozed " + min + " min", Toast.LENGTH_SHORT).show();
        }
    }
}
