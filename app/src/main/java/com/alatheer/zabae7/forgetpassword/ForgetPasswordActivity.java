package com.alatheer.zabae7.forgetpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.databinding.ActivityForgetPasswordBinding;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding activityForgetPasswordBinding;
    ForgetPasswordViewModel forgetPasswordViewModel;
    EditText et_email;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        activityForgetPasswordBinding = DataBindingUtil.setContentView(this,R.layout.activity_forget_password);
        forgetPasswordViewModel = new ForgetPasswordViewModel(this);
        activityForgetPasswordBinding.setForgetpasswordviewmode(forgetPasswordViewModel);
        //et_email = activityForgetPasswordBinding.etEmail;
    }

    public void Back(View view) {
        onBackPressed();
    }

    public void forgetpassword(View view) {
        Validation();
    }

    private void Validation() {
        email = et_email.getText().toString();
        if(!TextUtils.isEmpty(email)){
            forgetPasswordViewModel.forgetpassword(email);
        }
    }
}
