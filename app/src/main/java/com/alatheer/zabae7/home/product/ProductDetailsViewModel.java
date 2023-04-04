package com.alatheer.zabae7.home.product;

import android.content.Context;
import android.widget.Toast;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.notifications.NotificationModel;
import com.alatheer.zabae7.updateproduct.ProductModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsViewModel {
    Context context;
    ProductDetailsFragment productDetailsFragment;

    public ProductDetailsViewModel(Context context, ProductDetailsFragment productDetailsFragment) {
        this.context = context;
        this.productDetailsFragment = productDetailsFragment;
    }

    public void getNotifications(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<NotificationModel> call = getDataService.get_notifications(user_id);
            call.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    if (response.isSuccessful()){
                        productDetailsFragment.setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }

    public void get_offer(Integer id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_product_details(id);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()){
                        productDetailsFragment.setData2(response.body().getProductDetails());
                    }
                }

                @Override
                public void onFailure(Call<ProductModel> call, Throwable t) {

                }
            });
        }
    }
}
