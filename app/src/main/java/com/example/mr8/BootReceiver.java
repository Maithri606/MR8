package com.example.mr8;

import android.content.*;
import java.util.ArrayList;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context c, Intent i) {

        if (!Intent.ACTION_BOOT_COMPLETED.equals(i.getAction())) return;

        SQLiteHelper db = new SQLiteHelper(c);

        ArrayList<Profile> profiles = db.getProfiles();

        for (Profile p : profiles) {

            ArrayList<Medicine> meds = db.getMedicines(p.id);

            for (Medicine m : meds) {

                if (m.time != null && !m.time.isEmpty()) {

                    String[] times = m.time.split(",");

                    for (String t : times) {
                        AlarmHelper.setAlarm(c, m.id, m.name, t.trim());
                    }
                }
            }
        }
    }
}
