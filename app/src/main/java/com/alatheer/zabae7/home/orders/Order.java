package com.alatheer.zabae7.home.orders;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order implements Parcelable {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("pay_method")
    @Expose
    private String payMethod;
    @SerializedName("address")
    @Expose
    private String address;

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
    @SerializedName("promo_value")
    @Expose
    private String promo_value;

    public String getPromo_value() {
        return promo_value;
    }

    public void setPromo_value(String promo_value) {
        this.promo_value = promo_value;
    }

    @SerializedName("order_details")
    @Expose
    private List<OrderDetail> orderDetails = null;

    protected Order(Parcel in) {
        orderId = in.readString();
        orderDate = in.readString();
        orderTime = in.readString();
        haletTalab = in.readString();
        if (in.readByte() == 0) {
            allSum = null;
        } else {
            allSum = in.readInt();
        }
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(orderDate);
        parcel.writeString(orderTime);
        parcel.writeString(haletTalab);
        if (allSum == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(allSum);
        }
    }
}
