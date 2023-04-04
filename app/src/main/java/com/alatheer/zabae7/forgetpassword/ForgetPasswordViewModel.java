package com.alatheer.zabae7.forgetpassword;

import android.content.Context;
import android.widget.Toast;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordViewModel {
    Context context;

    public ForgetPasswordViewModel(Context context) {
        this.context = context;
    }

    public void forgetpassword(String email) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ForgetPasswordModel> call = getDataService.forgetpassword(email);
            call.enqueue(new Callback<ForgetPasswordModel>() {
                @Override
                public void onResponse(Call<ForgetPasswordModel> call, Response<ForgetPasswordModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ForgetPasswordModel> call, Throwable t) {

                }
            });
        }
    }
}
