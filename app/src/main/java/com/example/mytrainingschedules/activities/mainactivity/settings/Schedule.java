package com.example.mytrainingschedules.activities.mainactivity.settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Schedule implements Serializable {

    private String title;
    private String description;
    private String creator;
    private String categoria1;
    private String categoria2;
    private String requireEquipment;

    public Schedule(JSONObject schedule) {
        try {
            this.title = schedule.getString("title");
            this.description = schedule.getString("description");
            this.requireEquipment = schedule.getString("equipment");;
            this.creator=schedule.getString("creator");
            this.categoria1=schedule.getString("categoria1");
            this.categoria2=schedule.getString("categoria2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCategoria1() {
        return categoria1;
    }

    public void setCategoria1(String categoria1) {
        this.categoria1 = categoria1;
    }

    public String getCategoria2() {
        return categoria2;
    }

    public void setCategoria2(String categoria2) {
        this.categoria2 = categoria2;
    }

    public String getRequireEquipment() {
        return requireEquipment;
    }

    public void setRequireEquipment(String requireEquipment) {
        this.requireEquipment = requireEquipment;
    }
}
