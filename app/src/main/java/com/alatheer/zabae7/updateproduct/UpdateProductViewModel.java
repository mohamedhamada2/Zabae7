package com.alatheer.zabae7.updateproduct;

import android.content.Context;
import android.widget.Toast;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.home.home2.AllProductsCity;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;
import com.alatheer.zabae7.notifications.NotificationModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProductViewModel {
    Context context;
    UpdateProductFragment updateProductFragment;

    public UpdateProductViewModel(Context context, UpdateProductFragment updateProductFragment) {
        this.context = context;
        this.updateProductFragment = updateProductFragment;
    }

    public void getProductData(Integer product_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProductModel> call = getDataService.get_product_details(product_id);
            call.enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                    if (response.isSuccessful()){
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        updateProductFragment.setData(response.body().getProductDetails());
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
                        updateProductFragment.setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }
}
