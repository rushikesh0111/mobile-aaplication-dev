package com.example.weatherapp.models;

public class HourModel {
    public Long datetimeEpoch;
    public Double temp;
    public String conditions;
    public String icon;

    public HourModel() {
    }

    public HourModel(Long datetimeEpoch, Double temp, String conditions, String icon) {
        this.datetimeEpoch = datetimeEpoch;
        this.temp = temp;
        this.conditions = conditions;
        this.icon = icon;
    }
}
