package com.alatheer.zabae7.home.profile;

import android.content.Context;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationModel;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.signup.CityModel;
import com.alatheer.zabae7.signup.SignUpActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel {
    Context context;
    ProfileFragment profileFragment;
    UserSharedPreference userSharedPreference;
    User user;
    String user_id;
    UserModel userModel;
    List<AllCity> allCities;
    List<String> allcitiestitles;
    public ProfileViewModel(Context context, ProfileFragment profileFragment) {
        this.context = context;
        this.profileFragment = profileFragment;
    }

    public void getProfileData() {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<UserModel> call = getDataService.get_profile(user_id);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()){
                        userModel = response.body();
                        profileFragment.setData(userModel);
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                }
            });
        }
    }

    public void getSharedPreferanceData() {
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
                        profileFragment.setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }

    public void getCities() {
        allcitiestitles = new ArrayList<>();
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CityModel> call = getDataService.getCities();
            call.enqueue(new Callback<CityModel>() {
                @Override
                public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                    if (response.isSuccessful()) {
                        allCities = response.body().getAllCities();
                        for (AllCity allCity : allCities) {
                            allcitiestitles.add(allCity.getCityName());
                        }
                        profileFragment.setSpinner(allcitiestitles,allCities);
                    }
                }

                @Override
                public void onFailure(Call<CityModel> call, Throwable t) {

                }
            });
        }
    }

    public void update_user_data(String user_name, String email, String phone, String password, String city_id, Uri user_image) {
        if(user_image == null){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<EditProfile> call = getDataService.edit_profile_without_image(user_id,password,user_name,email,phone,city_id);
            call.enqueue(new Callback<EditProfile>() {
                @Override
                public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            profileFragment.go_to_main();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<EditProfile> call, Throwable t) {

                }
            });
        }else {
            RequestBody rb_user_name = Utilities.getRequestBodyText(user_name);
            RequestBody rb_password = Utilities.getRequestBodyText(password);
            RequestBody rb_email = Utilities.getRequestBodyText(email);
            RequestBody rb_phone = Utilities.getRequestBodyText(phone);
            RequestBody rb_city_id = Utilities.getRequestBodyText(city_id);
            RequestBody rb_user_id = Utilities.getRequestBodyText(user_id);
            MultipartBody.Part photo1 = Utilities.getMultiPart(context, user_image, "m_image");
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<EditProfile> call = getDataService.edit_profile_with_image(rb_user_id,rb_password,rb_user_name,rb_email,rb_phone,rb_city_id,photo1);
            call.enqueue(new Callback<EditProfile>() {
                @Override
                public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            profileFragment.go_to_main();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<EditProfile> call, Throwable t) {

                }
            });

        }
    }

}
