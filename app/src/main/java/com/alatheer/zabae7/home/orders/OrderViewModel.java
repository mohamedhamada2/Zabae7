package com.alatheer.zabae7.home.orders;

import android.content.Context;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderViewModel {
    Context context;
    OrdersFragment ordersFragment;
    UserSharedPreference userSharedPreference;
    User user;
    String user_id;
    List<Order> orderList;
    List<OrderStatus> orderStatusList;
    public OrderViewModel(Context context, OrdersFragment ordersFragment) {
        this.context = context;
        this.ordersFragment = ordersFragment;
    }

    public void getallorders(String type) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<OrderModel> call = getDataService.get_orders(user_id,type);
            call.enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                    if (response.isSuccessful()) {
                        orderList = response.body().getOrders();
                        ordersFragment.init_recycler(orderList);
                    }
                }

                @Override
                public void onFailure(Call<OrderModel> call, Throwable t) {

                }
            });
        }
    }

    public void getSharedpreferanceData() {
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(context);
        user_id = user.getMember().getUserId();
        getNotifications(user_id);

    }

    private void getNotifications(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<NotificationModel> call = getDataService.get_notifications(user_id);
            call.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    if (response.isSuccessful()){
                        ordersFragment.setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }

    public void get_orders_status() {
        orderStatusList = new ArrayList<>();
        orderStatusList.add(new OrderStatus("الطلبات الحالية", R.drawable.ic_box));
        orderStatusList.add(new OrderStatus("الطلبات السابقة",R.drawable.ic_box_tick));
        ordersFragment.init_order_status(orderStatusList);
    }
}
