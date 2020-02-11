package com.eltherbiometric.employee.data.model;

import java.util.ArrayList;

public class Upload {
    private ArrayList<Presence> data = new ArrayList<>();

    public Upload(ArrayList<Presence> data) {
        this.data.clear();
        this.data.addAll(data);
    }
}
