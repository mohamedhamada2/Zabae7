
package com.alatheer.zabae7.notificationdata;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id_fk")
    @Expose
    private String orderIdFk;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_size_cost")
    @Expose
    private String productSizeCost;
    @SerializedName("product_qty")
    @Expose
    private String productQty;
    @SerializedName("product_price")
    @Expose
    private Object productPrice;
    @SerializedName("vendor_id_fk")
    @Expose
    private Object vendorIdFk;
    @SerializedName("size_id")
    @Expose
    private String sizeId;
    @SerializedName("size_price")
    @Expose
    private String sizePrice;
    @SerializedName("cutting_id")
    @Expose
    private String cuttingId;
    @SerializedName("cutting_price")
    @Expose
    private String cuttingPrice;
    @SerializedName("cutting_head_id")
    @Expose
    private String cuttingHeadId;
    @SerializedName("cutting_head_price")
    @Expose
    private String cuttingHeadPrice;
    @SerializedName("packag_id")
    @Expose
    private String packagId;
    @SerializedName("packag_price")
    @Expose
    private String packagPrice;
    @SerializedName("category_name")
    @Expose
    private Object categoryName;
    @SerializedName("size_name")
    @Expose
    private String sizeName;
    @SerializedName("cutting_title")
    @Expose
    private String cuttingTitle;
    @SerializedName("head_cutting_title")
    @Expose
    private String headCuttingTitle;
    @SerializedName("package_title")
    @Expose
    private String packageTitle;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_image")
    @Expose
    private String productImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderIdFk() {
        return orderIdFk;
    }

    public void setOrderIdFk(String orderIdFk) {
        this.orderIdFk = orderIdFk;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductSizeCost() {
        return productSizeCost;
    }

    public void setProductSizeCost(String productSizeCost) {
        this.productSizeCost = productSizeCost;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public Object getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Object productPrice) {
        this.productPrice = productPrice;
    }

    public Object getVendorIdFk() {
        return vendorIdFk;
    }

    public void setVendorIdFk(Object vendorIdFk) {
        this.vendorIdFk = vendorIdFk;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizePrice() {
        return sizePrice;
    }

    public void setSizePrice(String sizePrice) {
        this.sizePrice = sizePrice;
    }

    public String getCuttingId() {
        return cuttingId;
    }

    public void setCuttingId(String cuttingId) {
        this.cuttingId = cuttingId;
    }

    public String getCuttingPrice() {
        return cuttingPrice;
    }

    public void setCuttingPrice(String cuttingPrice) {
        this.cuttingPrice = cuttingPrice;
    }

    public String getCuttingHeadId() {
        return cuttingHeadId;
    }

    public void setCuttingHeadId(String cuttingHeadId) {
        this.cuttingHeadId = cuttingHeadId;
    }

    public String getCuttingHeadPrice() {
        return cuttingHeadPrice;
    }

    public void setCuttingHeadPrice(String cuttingHeadPrice) {
        this.cuttingHeadPrice = cuttingHeadPrice;
    }

    public String getPackagId() {
        return packagId;
    }

    public void setPackagId(String packagId) {
        this.packagId = packagId;
    }

    public String getPackagPrice() {
        return packagPrice;
    }

    public void setPackagPrice(String packagPrice) {
        this.packagPrice = packagPrice;
    }

    public Object getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(Object categoryName) {
        this.categoryName = categoryName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getCuttingTitle() {
        return cuttingTitle;
    }

    public void setCuttingTitle(String cuttingTitle) {
        this.cuttingTitle = cuttingTitle;
    }

    public String getHeadCuttingTitle() {
        return headCuttingTitle;
    }

    public void setHeadCuttingTitle(String headCuttingTitle) {
        this.headCuttingTitle = headCuttingTitle;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

}
