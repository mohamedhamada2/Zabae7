package com.alatheer.zabae7.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.signup.SignupModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeViewModel {
    Context context;
    UserSharedPreference userSharedPreference;
    VerificationCodeActivity verificationCodeActivity;
    User user;
    public VerificationCodeViewModel(Context context) {
        this.context = context;
        verificationCodeActivity = (VerificationCodeActivity) context;
    }

    public void Login(String phone, String password) {
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

    public void sendRegisterRequestwithImage(String name, String email, String phone, String password, Uri filepath, String city_id, String address) {
        userSharedPreference = UserSharedPreference.getInstance();
        RequestBody rb_name = Utilities.getRequestBodyText(name+"");
        RequestBody rb_email = Utilities.getRequestBodyText(email);
        RequestBody rb_phone = Utilities.getRequestBodyText(phone+ "");
        RequestBody rb_password = Utilities.getRequestBodyText(password+"");
        RequestBody rb_city_id= Utilities.getRequestBodyText(city_id+"");
        RequestBody rb_address= Utilities.getRequestBodyText(address+"");
        MultipartBody.Part img = Utilities.getMultiPart(context, filepath, "m_image");
        if (Utilities.isNetworkAvailable(context)) {
            ProgressDialog pd = new ProgressDialog(verificationCodeActivity);
            pd.setMessage("تحميل ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = getDataService.signup_user_with_img(rb_name,rb_phone,rb_email,rb_city_id,rb_password,rb_address,img);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        if(response.body().getSuccess()==1){
                            Toast.makeText(context, "تم تسجيلك بنجاح", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            user = response.body();
                            userSharedPreference.Create_Update_UserData(context,user);
                            //Toast.makeText(context, "login successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("flag",1);
                            context.startActivity(intent);
                            //Animatoo.animateFade(context);
                            verificationCodeActivity.finish();
                           // SignupActivity.fa.finish();
                        }else {
                            pd.dismiss();
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

    public void sendRegisterRequestwithoutImage(String name, String email, String phone, String password, String city_id, String address) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(verificationCodeActivity);
            pd.setMessage("تحميل ...");
            pd.show();
            //Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
       /*firebaseAuth = FirebaseAuth.getInstance();
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+20 1065242773",
                60L,TimeUnit.SECONDS, (Activity) context,mCallbacks);*/
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = getDataService.signup_user(name,phone,email,city_id,password,address);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            pd.dismiss();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            //context.startActivity(new Intent(context, LoginActivity.class));
                            user = response.body();
                            userSharedPreference.Create_Update_UserData(context,user);
                            //Toast.makeText(context, "login successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("flag",1);
                            context.startActivity(intent);
                            //Animatoo.animateFade(context);
                            verificationCodeActivity.finish();
                        }else {
                            pd.dismiss();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
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
