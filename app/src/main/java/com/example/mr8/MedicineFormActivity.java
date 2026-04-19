package com.example.mr8;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class MedicineFormActivity extends AppCompatActivity {

    EditText nameEdit;
    Spinner foodSpinner, frequencySpinner;

    Button addTimeBtn, dateBtn, saveBtn;
    TextView timeView, dateView;

    CheckBox morningCheck, eveningCheck, nightCheck;

    ArrayList<String> times = new ArrayList<>();
    String startDate = "";
    String selectedFrequency = "Daily";

    String profileId;
    com.example.mr8.Medicine editMedicine = null;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_medicine_form);

        // ================= UI =================
        nameEdit = findViewById(R.id.nameEdit);
        foodSpinner = findViewById(R.id.foodSpinner);
        frequencySpinner = findViewById(R.id.frequencySpinner);

        addTimeBtn = findViewById(R.id.addTimeBtn);
        dateBtn = findViewById(R.id.dateBtn);
        saveBtn = findViewById(R.id.saveBtn);

        timeView = findViewById(R.id.timeView);
        dateView = findViewById(R.id.dateView);

        morningCheck = findViewById(R.id.morningCheck);
        eveningCheck = findViewById(R.id.eveningCheck);
        nightCheck = findViewById(R.id.nightCheck);

        profileId = getIntent().getStringExtra("profileId");

        // ================= EDIT MODE =================
        if (getIntent().hasExtra("medicine")) {

            editMedicine = (com.example.mr8.Medicine) getIntent().getSerializableExtra("medicine");

            if (editMedicine != null) {
                loadEditData();
            }
        }

        setupFoodSpinner();
        setupFrequencySpinner();

        // ================= EVENTS =================
        addTimeBtn.setOnClickListener(v -> pickTime());
        dateBtn.setOnClickListener(v -> pickDate());
        saveBtn.setOnClickListener(v -> saveMedicine());
    }

    // ================= LOAD EDIT DATA =================
    private void loadEditData() {

        nameEdit.setText(editMedicine.name);

        if (editMedicine.times != null) {
            times.addAll(editMedicine.times);
        }

        startDate = editMedicine.date != null ? editMedicine.date : "";
        selectedFrequency = editMedicine.frequency != null ? editMedicine.frequency : "Daily";

        updateUI();
    }

    // ================= FOOD =================
    private void setupFoodSpinner() {

        String[] items = {"Before Food", "After Food"};

        foodSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items
        ));
    }

    // ================= FREQUENCY =================
    private void setupFrequencySpinner() {

        String[] items = {"Daily", "Weekly", "Custom"};

        frequencySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items
        ));

        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedFrequency = items[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // ================= TIME PICK =================
    private void pickTime() {

        Calendar c = Calendar.getInstance();

        new TimePickerDialog(this, (v, h, m) -> {

            String t = String.format(Locale.getDefault(), "%02d:%02d", h, m);

            if (!times.contains(t)) {
                times.add(t);
                updateUI();
            }

        }, c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true).show();
    }

    // ================= DATE PICK =================
    private void pickDate() {

        Calendar c = Calendar.getInstance();

        new DatePickerDialog(this, (v, y, m, d) -> {

            startDate = y + "-" + (m + 1) + "-" + d;
            updateUI();

        }, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)).show();
    }

    // ================= QUICK TIMES =================
    private void applyQuickTimes() {

        if (morningCheck.isChecked() && !times.contains("08:00"))
            times.add("08:00");

        if (eveningCheck.isChecked() && !times.contains("13:00"))
            times.add("13:00");

        if (nightCheck.isChecked() && !times.contains("20:00"))
            times.add("20:00");
    }

    // ================= UI =================
    private void updateUI() {
        timeView.setText("Times: " + times.toString());
        dateView.setText("Date: " + startDate);
    }

    // ================= SAVE =================
    private void saveMedicine() {

        String name = nameEdit.getText().toString().trim();

        if (name.isEmpty()) {
            nameEdit.setError("Enter name");
            return;
        }

        applyQuickTimes();

        if (times.isEmpty()) {
            Toast.makeText(this, "Select at least one time", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteHelper db = new SQLiteHelper(this);

        // ================= ADD MODE =================
        if (editMedicine == null) {

            com.example.mr8.Medicine m = new com.example.mr8.Medicine(
                    UUID.randomUUID().toString(),
                    name,
                    new ArrayList<>(times),
                    startDate,
                    foodSpinner.getSelectedItem().toString(),
                    selectedFrequency,
                    profileId
            );

            db.insertMedicine(m);

            for (String t : times) {
                AlarmHelper.setAlarm(this, m.id, name, t);
            }

            Toast.makeText(this, "Medicine Added", Toast.LENGTH_SHORT).show();
        }

        // ================= EDIT MODE =================
        else {

            // cancel old alarms first
            if (editMedicine.times != null) {
                for (String t : editMedicine.times) {
                    AlarmHelper.cancelAlarm(this, editMedicine.id, t);
                }
            }

            editMedicine.name = name;
            editMedicine.times = new ArrayList<>(times);
            editMedicine.date = startDate;
            editMedicine.food = foodSpinner.getSelectedItem().toString();
            editMedicine.frequency = selectedFrequency;

            db.updateMedicine(editMedicine);

            // reschedule alarms
            for (String t : times) {
                AlarmHelper.setAlarm(this, editMedicine.id, name, t);
            }

            Toast.makeText(this, "Medicine Updated", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
