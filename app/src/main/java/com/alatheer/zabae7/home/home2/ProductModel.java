package com.alatheer.zabae7.home.home2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModel {

    @SerializedName("all_products_cities")
    @Expose
    private List<AllProductsCity> allProductsCities = null;

    public List<AllProductsCity> getAllProductsCities() {
        return allProductsCities;
    }

    public void setAllProductsCities(List<AllProductsCity> allProductsCities) {
        this.allProductsCities = allProductsCities;
    }

}
