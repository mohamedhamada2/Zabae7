package com.alatheer.zabae7.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankTransfer {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("ipan")
    @Expose
    private String ipan;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("publisher")
    @Expose
    private Object publisher;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIpan() {
        return ipan;
    }

    public void setIpan(String ipan) {
        this.ipan = ipan;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Object getPublisher() {
        return publisher;
    }

    public void setPublisher(Object publisher) {
        this.publisher = publisher;
    }

}
