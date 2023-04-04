package com.alatheer.zabae7.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alatheer.zabae7.MainActivity;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.countries.CountriesActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.alatheer.zabae7.R;
import com.alatheer.zabae7.databinding.ActivityLoginBinding;
import com.alatheer.zabae7.forgetpassword.ForgetPasswordActivity;
import com.alatheer.zabae7.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    LoginViewModel loginViewModel;
    EditText et_phone;
    Button btn_login;
    String phone,password,phone_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
    }

    private void initview() {
        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        loginViewModel = new LoginViewModel(this);
        activityLoginBinding.setLoginViewModel(loginViewModel);
        activityLoginBinding.countryCodePicker.setCountryForPhoneCode(+966);
        et_phone = activityLoginBinding.etPhone;

    }

    public void Back(View view) {

    }

    public void Login(View view) {
        Validation();
    }

    private void Validation() {
        phone = et_phone.getText().toString();
        phone_no = "+"+activityLoginBinding.countryCodePicker.getFullNumber()+phone;
        password = et_phone.getText().toString();
        if ((phone.length()==9||phone.length()==10) &&  password.length()>= 6){
            loginViewModel.SignInData(phone,password,phone_no);
        }else {
            if(phone.length()!=10){
                et_phone.setError(getResources().getString(R.string.validate_email));
            }else {
                et_phone.setError(null);
            }
            if(phone.length()!=9){
                et_phone.setError(getResources().getString(R.string.validate_email));
            }else {
                et_phone.setError(null);
            }
        }
    }


    public void skip(View view) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("flag",1);
        startActivity(intent);
        Animatoo.animateFade(this);
    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        Animatoo.animateFade(this);
    }

    public void remember_password(View view) {
        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
        Animatoo.animateFade(this);
    }

    public void signup(View view) {
    }
}
