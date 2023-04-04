
package com.alatheer.zabae7.notificationdata;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderModel {

    @SerializedName("orders")
    @Expose
    private Orders orders;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
