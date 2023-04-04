package com.alatheer.zabae7.home.countries;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.signup.CityModel;
import com.alatheer.zabae7.signup.SignUpActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountriesViewModel {
    Context context;
    List<String> allcitiestitle;
    List<AllCity> allCities;
    CountriesActivity countriesActivity;
    public CountriesViewModel(Context context) {
        this.context = context;
        countriesActivity = (CountriesActivity) context;
    }


    public void getCities() {
            allcitiestitle = new ArrayList<>();
            if (Utilities.isNetworkAvailable(context)) {
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
                            countriesActivity.initSpinner(allcitiestitle,allCities);
                        }
                    }

                    @Override
                    public void onFailure(Call<CityModel> call, Throwable t) {

                    }
                });
            }
        }
}
