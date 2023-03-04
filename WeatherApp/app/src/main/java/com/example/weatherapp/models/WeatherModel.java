package com.example.weatherapp.models;

import java.util.ArrayList;

public class WeatherModel {
    public String address;
    public String timezone;
    public int tzoffset;
    public ArrayList<DayModel> dayModelArrayList;
    public CurrentConditionModel currentConditionModel;

    public WeatherModel() { }

    public WeatherModel(String address, String timezone, int tzoffset, ArrayList<DayModel> dayModelArrayList, CurrentConditionModel currentConditionModel) {
        this.address = address;
        this.timezone = timezone;
        this.tzoffset = tzoffset;
        this.dayModelArrayList = dayModelArrayList;
        this.currentConditionModel = currentConditionModel;
    }
}
