package com.alatheer.zabae7.home.home2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllProductsCity implements Parcelable {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_cat")
    @Expose
    private String productCat;
    @SerializedName("product_specification")
    @Expose
    private String productSpecification;
    @SerializedName("product_desc")
    @Expose
    private String productDesc;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("main_category_name")
    @Expose
    private String mainCategoryName;

    public Integer getOfferd() {
        return offerd;
    }

    public void setOfferd(Integer offerd) {
        this.offerd = offerd;
    }

    @SerializedName("offerd")
    @Expose
    private Integer offerd;
    @SerializedName("product_sizes")
    @Expose
    private List<ProductSize> productSizes = null;
    @SerializedName("taghlif_option")
    @Expose
    private List<TaghlifOption> taghlifOption = null;
    @SerializedName("cutting_option")
    @Expose
    private List<CuttingOption> cuttingOption = null;
    @SerializedName("cutting_option_head")
    @Expose
    private List<CuttingOptionHead> cuttingOptionHead = null;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCat() {
        return productCat;
    }

    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }

    public Object getProductSpecification() {
        return productSpecification;
    }

    public void setProductSpecification(String productSpecification) {
        this.productSpecification = productSpecification;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }

    public List<ProductSize> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSize> productSizes) {
        this.productSizes = productSizes;
    }

    public List<TaghlifOption> getTaghlifOption() {
        return taghlifOption;
    }

    public void setTaghlifOption(List<TaghlifOption> taghlifOption) {
        this.taghlifOption = taghlifOption;
    }

    public List<CuttingOption> getCuttingOption() {
        return cuttingOption;
    }

    public void setCuttingOption(List<CuttingOption> cuttingOption) {
        this.cuttingOption = cuttingOption;
    }

    public List<CuttingOptionHead> getCuttingOptionHead() {
        return cuttingOptionHead;
    }

    public void setCuttingOptionHead(List<CuttingOptionHead> cuttingOptionHead) {
        this.cuttingOptionHead = cuttingOptionHead;
    }
    protected AllProductsCity(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productCat = in.readString();
        productSpecification = in.readString();
        productDesc = in.readString();
        image = in.readString();
        mainCategoryName = in.readString();
    }

    public static final Creator<AllProductsCity> CREATOR = new Creator<AllProductsCity>() {
        @Override
        public AllProductsCity createFromParcel(Parcel in) {
            return new AllProductsCity(in);
        }

        @Override
        public AllProductsCity[] newArray(int size) {
            return new AllProductsCity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(productName);
        parcel.writeString(productCat);
        parcel.writeString(productSpecification);
        parcel.writeString(productDesc);
        parcel.writeString(image);
        parcel.writeString(mainCategoryName);
    }
}
