package com.example.mr8;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedicineListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteHelper db;
    MedicineAdapter adapter;

    String profileId;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_medicine_list);

        profileId = getIntent().getStringExtra("profileId");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new SQLiteHelper(this);

        load();
    }

    private void load() {

        ArrayList<Medicine> list = db.getMedicines(profileId);

        if (list == null) {
            Toast.makeText(this, "No medicines", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new MedicineAdapter(this, list, profileId);
        recyclerView.setAdapter(adapter);
    }
}
