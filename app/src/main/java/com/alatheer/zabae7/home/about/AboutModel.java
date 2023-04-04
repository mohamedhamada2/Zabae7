package com.alatheer.zabae7.home.about;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AboutModel {

    @SerializedName("app_info")
    @Expose
    private List<AppInfo> appInfo = null;

    public List<AppInfo> getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(List<AppInfo> appInfo) {
        this.appInfo = appInfo;
    }
}
