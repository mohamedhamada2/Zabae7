package com.alatheer.zabae7.home.home2;

import android.content.Context;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.notifications.NotificationModel;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.signup.CityModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragmentViewModel {
    Context context;
    HomeFragment homeFragment;
    List<AllProductsCity> allProductList;
    List<String> allcitiestitle;
    List<AllCity> allCities;
    HomeActivity homeActivity;
    public HomeFragmentViewModel(Context context, HomeFragment homeFragment) {
        this.context = context;
        this.homeFragment = homeFragment;
        homeActivity = (HomeActivity) context;
    }

    public void getProducts(String city_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_all_products(city_id);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()){
                        allProductList = response.body().getAllProductsCities();
                        homeFragment.initRecycler(allProductList);
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {

                }
            });
        }
    }

    public void getNotifications(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<NotificationModel> call = getDataService.get_notifications(user_id);
            call.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    if (response.isSuccessful()){
                        homeFragment.setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }
    /*public void getCities() {
        allcitiestitle = new ArrayList<>();
        if (Utilities.isNetworkAvailable(homeActivity)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CityModel> call = getDataService.getCities();
            call.enqueue(new Callback<CityModel>() {
                @Override
                public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                    if (response.isSuccessful()) {
                        allCities = response.body().getAllCities();
                        for (AllCity allCity : allCities) {
                            allcitiestitle.add(allCity.getCityName());
                        }
                        homeFragment.initSpinner(allcitiestitle,allCities);
                    }
                }

                @Override
                public void onFailure(Call<CityModel> call, Throwable t) {

                }
            });
        }
    }*/
}
