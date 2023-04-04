package com.alatheer.zabae7.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.signup.AllCity;
import com.google.gson.Gson;

public class CitySharedPreferance {
    Context context;
    SharedPreferences mPrefs;
    private static CitySharedPreferance instance=null;


    public CitySharedPreferance(Context context) {
        this.context = context;
    }

    public CitySharedPreferance() {

    }

    public static CitySharedPreferance getInstance()
    {
        if (instance==null)
        {
            instance = new CitySharedPreferance();
        }
        return instance;
    }

    public void Create_Update_UserData(Context context, AllCity allCity)
    {
        mPrefs = context.getSharedPreferences("city", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(allCity);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("city_data",userData);
        editor.apply();
        Create_Update_Session(context, "login");

    }

    public void Create_Update_Session(Context context, String session)
    {
        mPrefs = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("state",session);
        editor.apply();
    }


    public String getSession(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        String session = preferences.getString("state", "logout");
        return session;
    }


    public AllCity Get_UserData(Context context){

        mPrefs = context.getSharedPreferences("city", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String userData = mPrefs.getString("city_data", "");
        AllCity allCity=gson.fromJson(userData,AllCity.class);
        return allCity;


    }
    public void ClearData(Context context) {
        AllCity allCity = null;
        mPrefs = context.getSharedPreferences("city", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(allCity);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("city_data", userData);
        editor.apply();
        Create_Update_Session(context,"login");
    }
}
