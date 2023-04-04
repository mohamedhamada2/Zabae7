package com.alatheer.zabae7.home.messaging;

import android.content.Context;
import android.widget.Toast;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingViewModel {
    Context context;
    MessagingFragment messagingFragment;
    UserSharedPreference userSharedPreference;
    User user;
    String user_id;

    public MessagingViewModel(Context context, MessagingFragment messagingFragment) {
        this.context = context;
        this.messagingFragment = messagingFragment;
    }

    public void get_messaging() {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<List<MessageModel>> call = getDataService.get_messages(user_id);
            call.enqueue(new Callback<List<MessageModel>>() {
                @Override
                public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                    if (response.isSuccessful()){
                        if (!response.body().isEmpty()){

                            messagingFragment.init_recycler(response.body());
                        }else {
                            messagingFragment.show_msg();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<MessageModel>> call, Throwable t) {

                }
            });
        }
    }

    public void getSharedPreferanceData() {
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(context);
        user_id = user.getMember().getUserId();
    }

    public void getNotifications(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<NotificationModel> call = getDataService.get_notifications(user_id);
            call.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    if (response.isSuccessful()){
                        messagingFragment.setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }
}
