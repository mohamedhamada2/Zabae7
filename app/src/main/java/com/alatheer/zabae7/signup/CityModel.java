package com.alatheer.zabae7.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityModel {

    @SerializedName("all_cities")
    @Expose
    private List<AllCity> allCities = null;

    public List<AllCity> getAllCities() {
        return allCities;
    }

    public void setAllCities(List<AllCity> allCities) {
        this.allCities = allCities;
    }
}
