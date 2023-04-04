package com.alatheer.zabae7.home.product;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product",primaryKeys = {"product_id","size_id"})
public class OrderItemList implements Serializable {
    @NonNull
    @ColumnInfo(name = "product_id")
    Integer product_id;
    @ColumnInfo(name = "product_qty")
    String product_qty;
    @ColumnInfo(name = "product_name")
    String product_name;
    @ColumnInfo(name = "product_img")
    String product_img;
    @NonNull
    @ColumnInfo(name = "size_id")
    String size_id;
    @ColumnInfo(name = "size_name")
    String size_name;
    @ColumnInfo(name = "total_price")
    String total_price;

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getSize_price() {
        return size_price;
    }

    public void setSize_price(String size_price) {
        this.size_price = size_price;
    }

    public String getCutting_name() {
        return cutting_name;
    }

    public void setCutting_name(String cutting_name) {
        this.cutting_name = cutting_name;
    }

    public String getPackag_name() {
        return packag_name;
    }

    public void setPackag_name(String packag_name) {
        this.packag_name = packag_name;
    }

    @ColumnInfo(name = "size_price")
    String size_price;
    @ColumnInfo(name = "cutting_id")
    String cutting_id;
    @ColumnInfo(name = "cutting_name")
    String cutting_name;
    @ColumnInfo(name = "cutting_price")
    String cutting_price;
    @ColumnInfo(name = "packag_id")
    String packag_id;
    @ColumnInfo(name = "packag_name")
    String packag_name;
    @ColumnInfo(name = "packag_price")
    String packag_price;
    @ColumnInfo(name = "cutting_head_id")
    String cutting_head_id;

    public String getCutting_head_id() {
        return cutting_head_id;
    }

    public void setCutting_head_id(String cutting_head_id) {
        this.cutting_head_id = cutting_head_id;
    }

    public String getCutting_head_price() {
        return cutting_head_price;
    }

    public void setCutting_head_price(String cutting_head_price) {
        this.cutting_head_price = cutting_head_price;
    }

    public String getCutting_head_name() {
        return cutting_head_name;
    }

    public void setCutting_head_name(String cutting_head_name) {
        this.cutting_head_name = cutting_head_name;
    }

    @ColumnInfo(name = "cutting_head_price")
    String cutting_head_price;
    @ColumnInfo(name = "cutting_head_name")
    String cutting_head_name;

    public String getSize_id() {
        return size_id;
    }

    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public String getCutting_id() {
        return cutting_id;
    }

    public void setCutting_id(String cutting_id) {
        this.cutting_id = cutting_id;
    }

    public String getCutting_price() {
        return cutting_price;
    }

    public void setCutting_price(String cutting_price) {
        this.cutting_price = cutting_price;
    }

    public String getPackag_id() {
        return packag_id;
    }

    public void setPackag_id(String packag_id) {
        this.packag_id = packag_id;
    }

    public String getPackag_price() {
        return packag_price;
    }

    public void setPackag_price(String packag_price) {
        this.packag_price = packag_price;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_img() {
        return product_img;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }
}
