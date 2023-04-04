package com.alatheer.zabae7.updateproduct;

import com.alatheer.zabae7.home.home2.AllProductsCity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductModel {
    @SerializedName("product_details")
    @Expose
    private ProductDetails productDetails;

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }
}
