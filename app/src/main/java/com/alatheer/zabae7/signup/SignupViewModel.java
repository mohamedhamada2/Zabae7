package com.alatheer.zabae7.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.login.LoginActivity;
import com.alatheer.zabae7.login.VerificationCodeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.firebase.auth.PhoneAuthOptions.newBuilder;

public class SignupViewModel {
    Context context;
    FirebaseAuth firebaseAuth;
    String code_sent;
    String firebase_token;
    SignUpActivity signupActivity;
    public SignupViewModel(Context context) {
        this.context = context;
        signupActivity = (SignUpActivity) context;
    }
    public void sendRegisterRequestwithImage(String user_name, String email, String phone, String password, String city_id,String address ,Uri filepath) {
        Intent intent = new Intent(signupActivity,VerificationCodeActivity.class);
        intent.putExtra("name",user_name);
        intent.putExtra("email",email);
        intent.putExtra("phone",phone);
        intent.putExtra("password",password);
        intent.putExtra("filepath",filepath.toString());
        intent.putExtra("city_id",city_id);
        intent.putExtra("address",address);
        intent.putExtra("flag",1);
        context.startActivity(intent);
    }

    public void sendRegisterRequestwithoutImage(String user_name, String email, String phone, String password, String city_id,String address) {
        Intent intent = new Intent(signupActivity, VerificationCodeActivity.class);
        intent.putExtra("name",user_name);
        intent.putExtra("email",email);
        intent.putExtra("phone",phone);
        intent.putExtra("password",password);
        intent.putExtra("city_id",city_id);
        intent.putExtra("address",address);
        intent.putExtra("flag",2);
        context.startActivity(intent);
    }
}
