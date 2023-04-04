package com.alatheer.zabae7.home.messaging;

import android.content.Context;
import android.widget.Toast;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMessageViewModel {
    Context context;
    AddMessageFragment addMessageFragment;
    UserSharedPreference userSharedPreference;
    User user;
    String user_id;
    public AddMessageViewModel(Context context, AddMessageFragment addMessageFragment) {
        this.context = context;
        this.addMessageFragment = addMessageFragment;
    }

    public void send_message(String phone,String title, String content,String name) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SuccessMessageModel> call = getDataService.sendMessage(user_id,name,phone,title,content);
            call.enqueue(new Callback<SuccessMessageModel>() {
                @Override
                public void onResponse(Call<SuccessMessageModel> call, Response<SuccessMessageModel> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<SuccessMessageModel> call, Throwable t) {

                }
            });
        }
    }

    public void getSharedpreferancedata() {
        try {
            userSharedPreference = UserSharedPreference.getInstance();
            user = userSharedPreference.Get_UserData(context);
            user_id = user.getMember().getUserId();
            addMessageFragment.sendUser(user);
        }catch (Exception e){
            user_id = null;
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
                        addMessageFragment.setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }
}
