package com.alatheer.zabae7.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.countries.CountriesActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel {
    Context context;
    UserSharedPreference userSharedPreference;
    public LoginViewModel(Context context) {
        this.context = context;
    }

    public void SignInData(String phone, String password,String phone_no) {
       /*Intent i = new Intent(context,VerificationCodeActivity.class);
        i.putExtra("phone",phone);
        i.putExtra("password",password);
        i.putExtra("phone_no",phone_no);
        context.startActivity(i);*/
        userSharedPreference = UserSharedPreference.getInstance();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = getDataService.login_User(phone,password);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            User user= response.body();
                            userSharedPreference.Create_Update_UserData(context,user);
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("flag",1);
                            context.startActivity(intent);
                        }else {
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
}
