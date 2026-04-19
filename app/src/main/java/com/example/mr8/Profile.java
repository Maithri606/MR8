package com.example.mr8;

import java.io.Serializable;

public class Profile implements Serializable {

    public String id;
    public String name;

    public Profile() {
        this.id = "";
        this.name = "";
    }

    public Profile(String id, String name) {
        this.id = id == null ? "" : id;
        this.name = name == null ? "" : name.trim();
    }

    @Override
    public String toString() {
        return (name == null || name.trim().isEmpty())
                ? "Unnamed Profile"
                : name.trim();
    }
}
