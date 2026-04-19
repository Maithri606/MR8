package com.example.mr8;

import java.io.Serializable;
import java.util.ArrayList;

public class Medicine implements Serializable {

    public String id;
    public String name;

    public String time;
    public ArrayList<String> times;
    public String status;
    public String date;
    public String food;
    public String frequency;
    public String profileId;

    // EMPTY CONSTRUCTOR (required for SQLite + intents)
    public Medicine() {
        this.times = new ArrayList<>();
        this.status = "Pending";
    }

    // FULL CONSTRUCTOR (YOU NEED THIS)
    public Medicine(String id, String name, ArrayList<String> times,
                    String date, String food,
                    String frequency, String profileId) {

        this.id = id;
        this.name = name;
        this.times = times != null ? times : new ArrayList<>();
        this.date = date;
        this.food = food;
        this.frequency = frequency;
        this.profileId = profileId;
        this.status = "Pending";
    }
}
