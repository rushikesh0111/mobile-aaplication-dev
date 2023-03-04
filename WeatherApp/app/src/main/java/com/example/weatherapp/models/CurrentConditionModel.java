package com.example.weatherapp.models;

public class CurrentConditionModel {
    public Long datetimeEpoch;
    public Long sunriseEpoch;
    public Long sunsetEpoch;
    public Double temp;
    public Double feelslike;
    public Double humidity;
    public Double windgust;
    public Double windspeed;
    public Double winddir;
    public Double visibility;
    public Double cloudcover;
    public Double uvindex;
    public String conditions;
    public String icon;

    public CurrentConditionModel() {
    }

    public CurrentConditionModel(Long datetimeEpoch, Long sunriseEpoch, Long sunsetEpoch, Double temp, Double feelslike, Double humidity, Double windgust, Double windspeed, Double winddir, Double visibility, Double cloudcover, Double uvindex, String conditions, String icon) {
        this.datetimeEpoch = datetimeEpoch;
        this.sunriseEpoch = sunriseEpoch;
        this.sunsetEpoch = sunsetEpoch;
        this.temp = temp;
        this.feelslike = feelslike;
        this.humidity = humidity;
        this.windgust = windgust;
        this.windspeed = windspeed;
        this.winddir = winddir;
        this.visibility = visibility;
        this.cloudcover = cloudcover;
        this.uvindex = uvindex;
        this.conditions = conditions;
        this.icon = icon;
    }
}
