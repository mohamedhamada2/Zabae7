
package com.alatheer.zabae7.notificationdata;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Orders {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("pay_method")
    @Expose
    private String payMethod;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("halet_talab")
    @Expose
    private String haletTalab;
    @SerializedName("all_sum")
    @Expose
    private Integer allSum;
    @SerializedName("order_details")
    @Expose
    private List<OrderDetail> orderDetails = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getHaletTalab() {
        return haletTalab;
    }

    public void setHaletTalab(String haletTalab) {
        this.haletTalab = haletTalab;
    }

    public Integer getAllSum() {
        return allSum;
    }

    public void setAllSum(Integer allSum) {
        this.allSum = allSum;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

}
