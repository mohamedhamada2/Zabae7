package com.alatheer.zabae7.notificationdata;

import android.content.Context;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDataViewModel {
    Context context;
    NotificationsDataActivity notificationsDataActivity;

    public NotificationDataViewModel(Context context) {
        this.context = context;
        notificationsDataActivity = (NotificationsDataActivity) context;
    }

    public void get_order(String id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<OrderModel> call = getDataService.get_order_details(id);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                    if (response.isSuccessful()){
                        notificationsDataActivity.setData(response.body());
                    }
                }

                @Override
                public void onFailure(Call<OrderModel> call, Throwable t) {

                }
            });
        }
    }
}
