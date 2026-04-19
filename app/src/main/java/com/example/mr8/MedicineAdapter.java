package com.example.mr8;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.*;


import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.VH> {

    Context c;
    ArrayList<com.example.mr8.Medicine> list;
    String profileId;

    public MedicineAdapter(Context c, ArrayList<com.example.mr8.Medicine> list, String profileId) {
        this.c = c;
        this.list = list;
        this.profileId = profileId;
    }

    class VH extends RecyclerView.ViewHolder {

        TextView name, time, status;

        VH(View v) {
            super(v);
            name = v.findViewById(R.id.t1);
            time = v.findViewById(R.id.t2);
            status = v.findViewById(R.id.t3);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(c).inflate(R.layout.item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int pos) {

        com.example.mr8.Medicine m = list.get(pos);

        if (m == null) return;

        // ================= TIME FORMAT =================
        StringBuilder timeText = new StringBuilder();

        if (m.times != null && !m.times.isEmpty()) {
            for (String t : m.times) {
                if (t != null) timeText.append(t).append(" ");
            }
        } else {
            timeText.append("No Time Set");
        }

        h.name.setText(m.name != null ? m.name : "No Name");
        h.time.setText("Time: " + timeText.toString().trim());
        h.status.setText("Status: " + (m.status != null ? m.status : "Pending"));

        // ================= CLICK =================
        h.itemView.setOnClickListener(v -> {

            int position = h.getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            com.example.mr8.Medicine current = list.get(position);

            CharSequence[] options = {"Edit", "Mark Taken", "Delete"};

            new AlertDialog.Builder(c)
                    .setTitle(current.name != null ? current.name : "Medicine")
                    .setItems(options, (dialog, which) -> {

                        SQLiteHelper db = new SQLiteHelper(c);

                        // ================= EDIT =================
                        if (which == 0) {

                            Intent i = new Intent(c, MedicineFormActivity.class);
                            i.putExtra("medicine", current);
                            i.putExtra("profileId", profileId);
                            c.startActivity(i);
                        }

                        // ================= MARK TAKEN =================
                        else if (which == 1) {

                            current.status = "Taken";
                            db.updateMedicine(current);

                            notifyItemChanged(position);

                            Toast.makeText(c,
                                    "Marked Taken",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ================= DELETE =================
                        else if (which == 2) {

                            if (current.times != null) {
                                for (String t : current.times) {
                                    if (t != null) {
                                        AlarmHelper.cancelAlarm(c, current.id, t);
                                    }
                                }
                            }

                            db.deleteMedicine(current.id);

                            if (position >= 0 && position < list.size()) {
                                list.remove(position);
                                notifyItemRemoved(position);
                            }

                            Toast.makeText(c,
                                    "Deleted",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });
    }
    private void showSnoozeDialog(Context context, Medicine m) {

        EditText input = new EditText(context);
        input.setHint("Enter minutes (e.g. 5)");

        new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Snooze Medicine")
                .setView(input)
                .setPositiveButton("Snooze", (d, w) -> {

                    int min = Integer.parseInt(input.getText().toString());

                    AlarmHelper.snooze(context, m.id, m.name, min);


                    Toast.makeText(context,
                            "Snoozed for " + min + " minutes",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}
