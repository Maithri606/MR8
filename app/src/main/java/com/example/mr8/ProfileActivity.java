package com.example.mr8;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    ListView listView;
    TextView emptyText;
    Button addBtn;

    ArrayList<Profile> list;
    ArrayAdapter<Profile> adapter;

    SQLiteHelper db;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_profile);

        db = new SQLiteHelper(this);

        listView = findViewById(R.id.listView);
        addBtn = findViewById(R.id.addProfileBtn);
        emptyText = findViewById(R.id.emptyText);

        list = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                list
        );

        listView.setAdapter(adapter);

        loadProfiles();

        // ================= ADD PROFILE =================
        addBtn.setOnClickListener(v -> showAddDialog());

        // ================= OPEN PROFILE =================
        listView.setOnItemClickListener((parent, view, pos, id) -> {

            if (pos < 0 || pos >= list.size()) return;

            Profile selected = list.get(pos);

            if (selected == null || TextUtils.isEmpty(selected.id)) return;

            Intent i = new Intent(this, DashboardActivity.class);
            i.putExtra("profileId", selected.id);
            startActivity(i);
        });

        // ================= DELETE PROFILE =================
        listView.setOnItemLongClickListener((parent, view, pos, id) -> {

            if (pos < 0 || pos >= list.size()) return true;

            Profile selected = list.get(pos);

            if (selected == null || TextUtils.isEmpty(selected.id)) return true;

            new AlertDialog.Builder(this)
                    .setTitle("Delete Profile")
                    .setMessage("Delete " + selected.name + "?")
                    .setPositiveButton("Yes", (d, w) -> {

                        db.deleteProfile(selected.id);
                        loadProfiles();

                        Toast.makeText(this,
                                "Profile Deleted",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }

    // ================= LOAD PROFILES =================
    private void loadProfiles() {

        list.clear();

        ArrayList<Profile> data = db.getProfiles();

        if (data != null && !data.isEmpty()) {
            list.addAll(data);
        }

        adapter.notifyDataSetChanged();

        // UI EMPTY STATE FIX
        if (list.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    // ================= ADD PROFILE =================
    private void showAddDialog() {

        EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Add Profile")
                .setView(input)
                .setPositiveButton("Add", (d, w) -> {

                    String name = input.getText().toString().trim();

                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(this,
                                "Enter name",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String id = String.valueOf(System.currentTimeMillis());

                    Profile p = new Profile(id, name);

                    db.insertProfile(p);
                    loadProfiles();

                    Toast.makeText(this,
                            "Profile Added",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
