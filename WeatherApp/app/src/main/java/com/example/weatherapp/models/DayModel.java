package com.example.weatherapp.models;

import java.util.ArrayList;

public class DayModel {
    public Long datetimeEpoch;
    public Double tempmax;
    public Double tempmin;
    public Double precipprob;
    public Double uvindex;
    public String description;
    public String weatherIcon;
    public ArrayList<HourModel> hourModelArrayList;

    public DayModel() {
    }

    public DayModel(Long datetimeEpoch, Double tempmax, Double tempmin, Double precipprob, Double uvindex, String description, String weatherIcon, ArrayList<HourModel> hourModelArrayList) {
        this.datetimeEpoch = datetimeEpoch;
        this.tempmax = tempmax;
        this.tempmin = tempmin;
        this.precipprob = precipprob;
        this.uvindex = uvindex;
        this.description = description;
        this.weatherIcon = weatherIcon;
        this.hourModelArrayList = hourModelArrayList;
    }
}
