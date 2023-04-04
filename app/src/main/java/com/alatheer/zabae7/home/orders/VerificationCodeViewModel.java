package com.alatheer.zabae7.home.orders;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.home.basket.BasketModel;
import com.alatheer.zabae7.home.basket.OrderSuccess;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.login.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeViewModel {
    Context context ;
    VerificationCodeFragment verificationCodeFragment;
    UserSharedPreference userSharedPreference;
    String orderdate,order_time;
    DatabaseClass databaseClass;
    List<OrderItemList> orderItemListList;

    public VerificationCodeViewModel(Context context, VerificationCodeFragment verificationCodeFragment) {
        this.context = context;
        this.verificationCodeFragment = verificationCodeFragment;
    }

    public void Login(String phone, String phone1) {
        userSharedPreference = UserSharedPreference.getInstance();
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = getDataService.login_User(phone, phone1);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(context, "تم استلام الطلب وسيتم التواصل معك قريبا", Toast.LENGTH_LONG).show();
                            User user = response.body();
                            userSharedPreference.Create_Update_UserData(context, user);
                            verificationCodeFragment.go_to_home();
                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    public void addOrder(String name, String phone, String city_id, String address, String payment_method) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        orderdate = df.format(c);
        Log.e("date", orderdate);
        String delegate = "hh:mm aaa";
        order_time = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        databaseClass = Room.databaseBuilder(context.getApplicationContext(), DatabaseClass.class, "my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        BasketModel basketModel = new BasketModel();
        basketModel.setUserId("");
        basketModel.setToken("");
        basketModel.setUserName(name);
        basketModel.setUserCity(city_id);
        basketModel.setUserPhone(phone);
        basketModel.setAddress(address);
        basketModel.setOrderItemList(orderItemListList);
        basketModel.setOrderDate(orderdate);
        basketModel.setOrderTime(order_time);
        basketModel.setPay_method(payment_method);
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<OrderSuccess> call = getDataService.send_order(basketModel);
        call.enqueue(new Callback<OrderSuccess>() {
            @Override
            public void onResponse(Call<OrderSuccess> call, Response<OrderSuccess> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        Toast.makeText(context, "تم استلام الطلب برجاء تفعيل الحساب وسيتم التواصل معك لاستكمال البيانات ...", Toast.LENGTH_LONG).show();
                        databaseClass.getDao().deleteAllproduct();
                        Login(phone,phone);
                        verificationCodeFragment.go_to_home();

                    }
                }
            }

            @Override
            public void onFailure(Call<OrderSuccess> call, Throwable t) {

            }
        });

    }

}
