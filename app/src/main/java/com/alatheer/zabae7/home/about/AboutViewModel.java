package com.alatheer.zabae7.home.about;

import android.content.Context;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutViewModel {
    Context context ;
    AboutFragment aboutFragment;
    UserSharedPreference userSharedPreference;
    User user;
    String user_id;

    public AboutViewModel(Context context, AboutFragment aboutFragment) {
        this.context = context;
        this.aboutFragment = aboutFragment;
    }

    public void getAbout() {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<AboutModel> call= getDataService.get_app_info();
            call.enqueue(new Callback<AboutModel>() {
                @Override
                public void onResponse(Call<AboutModel> call, Response<AboutModel> response) {
                    if (response.isSuccessful()){
                        aboutFragment.initrecycler(response.body().getAppInfo());
                    }
                }

                @Override
                public void onFailure(Call<AboutModel> call, Throwable t) {

                }
            });
        }
    }

    public void get_user_data() {
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
                        aboutFragment.setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }
}
