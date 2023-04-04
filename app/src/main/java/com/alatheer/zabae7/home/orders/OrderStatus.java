package com.alatheer.zabae7.home.orders;

public class OrderStatus {
    String En_name;
    String ar_name;
    int img;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public OrderStatus(String ar_name,int img) {
        this.img = img;
        this.ar_name = ar_name;
    }

    public String getEn_name() {
        return En_name;
    }

    public void setEn_name(String en_name) {
        En_name = en_name;
    }

    public String getAr_name() {
        return ar_name;
    }

    public void setAr_name(String ar_name) {
        this.ar_name = ar_name;
    }
}
