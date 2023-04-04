package com.alatheer.zabae7.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.countries.CountriesActivity;
import com.alatheer.zabae7.home.home2.HomeFragment;
import com.alatheer.zabae7.home.profile.UserModel;
import com.alatheer.zabae7.login.LoginActivity;
import com.alatheer.zabae7.R;
import com.alatheer.zabae7.login.User;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    int SPLASH_DISPLAY_LENGTH = 3000;
    ImageView logo,logo2;
    UserSharedPreference userSharedPreference;
    User user;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo);
        logo2 = findViewById(R.id.logo2);
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(SplashActivity.this);
        RotateAnimation rotateAnimation=new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF,
                .5f, RotateAnimation.RELATIVE_TO_SELF
                ,.5f);


        rotateAnimation.setDuration(3000);
        logo2.startAnimation(rotateAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null){
                    startActivity(new Intent(SplashActivity.this, CountriesActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this, CountriesActivity.class));
                    finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
