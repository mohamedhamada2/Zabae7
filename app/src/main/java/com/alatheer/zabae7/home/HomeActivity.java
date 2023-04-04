package com.alatheer.zabae7.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.CitySharedPreferance;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.ActivityHomeBinding;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.home2.HomeFragment;
import com.alatheer.zabae7.home.messaging.AddMessageFragment;
import com.alatheer.zabae7.home.messaging.MessagingFragment;
import com.alatheer.zabae7.home.orders.OrderDetailsFragment;
import com.alatheer.zabae7.home.orders.OrdersFragment;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;
import com.alatheer.zabae7.home.profile.MainProfileFragment;
import com.alatheer.zabae7.home.profile.ProfileFragment;
import com.alatheer.zabae7.login.LoginActivity;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.alatheer.zabae7.offers.OffersFragment;
import com.alatheer.zabae7.payment.PersonalDataActivity;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.sms.OTP_Receiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding activityHomeBinding;
    HomeActivityViewModel homeActivityViewModel;
    Fragment selectedfragment;
    UserSharedPreference userSharedPreference;
    User user;
    String firebase_token;
    CitySharedPreferance citySharedPreferance;
    AllCity allCity;
    String city_id;
    Integer flag;
    String more_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activityHomeBinding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        homeActivityViewModel = new HomeActivityViewModel(this);
        activityHomeBinding.setHomeviewmodel(homeActivityViewModel);
        //getDataIntent();
        activityHomeBinding.homeBottomnavigation.setOnNavigationItemSelectedListener(nav_listner);
        activityHomeBinding.homeBottomnavigation2.setOnNavigationItemSelectedListener(nav_listner2);
        //activityHomeBinding.homeBottomnavigation.setSelectedItemId(R.id.home);
        userSharedPreference = UserSharedPreference.getInstance();
        citySharedPreferance = CitySharedPreferance.getInstance();
        allCity = citySharedPreferance.Get_UserData(this);
        city_id = allCity.getCityId();
        user = userSharedPreference.Get_UserData(this);
        activityHomeBinding.homeBottomnavigation.setSelectedItemId(R.id.home);
        getfirebasetoken();
        getDataIntent();
        /*if(user != null){
            //Toast.makeText(this, user.getMember().getUserId(), Toast.LENGTH_SHORT).show();
            activityHomeBinding.homeBottomnavigation.setVisibility(View.VISIBLE);
            activityHomeBinding.homeBottomnavigation2.setVisibility(View.GONE);


        }else {
            //Toast.makeText(this, "heelo", Toast.LENGTH_SHORT).show();
            activityHomeBinding.homeBottomnavigation2.setVisibility(View.VISIBLE);
            activityHomeBinding.homeBottomnavigation.setVisibility(View.GONE);
            activityHomeBinding.homeBottomnavigation2.setSelectedItemId(R.id.home);
        }*/
// set Fragmentclass Arguments

    }

    private void getDataIntent() {
        flag = getIntent().getIntExtra("flag",0);
        if (flag == 2){
            more_data = getIntent().getStringExtra("moredata");
            selectedfragment = new ProductDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("flag",flag);
            //Toast.makeText(this, more_data, Toast.LENGTH_SHORT).show();
            bundle.putString("moredata",more_data);
            selectedfragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, selectedfragment).commit();
            activityHomeBinding.homeBottomnavigation.setVisibility(View.GONE);
        }else if (flag == 1) {
            selectedfragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, selectedfragment).commit();
            activityHomeBinding.homeBottomnavigation.setVisibility(View.VISIBLE);
        }else if (flag == 3){
            selectedfragment = new OrdersFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, selectedfragment).commit();
            activityHomeBinding.homeBottomnavigation.setVisibility(View.VISIBLE);
            activityHomeBinding.homeBottomnavigation.setSelectedItemId(R.id.order);
        }else {
            more_data = getIntent().getStringExtra("moredata");
            selectedfragment = new ProductDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("flag",2);
            //Toast.makeText(this, more_data, Toast.LENGTH_SHORT).show();
            bundle.putString("moredata",more_data);
            selectedfragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer, selectedfragment).commit();
            activityHomeBinding.homeBottomnavigation.setVisibility(View.GONE);
        }
    }

    /*private void getDataIntent() {
        city_id = getIntent().getStringExtra("city_id");
        Log.e("city_id",city_id);

    }*/

    private BottomNavigationView.OnNavigationItemSelectedListener nav_listner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            selectedfragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    Bundle bundle = new Bundle();
                    //bundle.putString("city_id", city_id);
// set Fragmentclass Arguments
                    selectedfragment = new HomeFragment();
                    selectedfragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out //popExit
                    ).replace(R.id.fragmentcontainer, selectedfragment).commit();
                    //item.setTitle(resources.getString(R.string.myaccount));
                    break;
                case R.id.profile:
                    if (user != null){
                        selectedfragment = new MainProfileFragment();
                        getSupportFragmentManager().beginTransaction().addToBackStack("MainProfileFragment").setCustomAnimations(R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out //popExit//
                        ).replace(R.id.fragmentcontainer, selectedfragment).commit();
                    }else {
                        createpopup();
                    }
                    //item.setTitle(resources.getString(R.string.home));
                    //Toast.makeText(HomeActivity.this, "profile Activity", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.offer:
                    selectedfragment = new OffersFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack("offersfragment").setCustomAnimations(R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out //popExit
                    ).replace(R.id.fragmentcontainer, selectedfragment).commit();
                    //item.setTitle(resources.getString(R.string.myaccount));
                    break;
                case R.id.order:
                    if (user != null){
                        selectedfragment = new OrdersFragment();
                        getSupportFragmentManager().beginTransaction().addToBackStack("OrdersFragment").setCustomAnimations(R.anim.slide_in,  // enter
                                R.anim.fade_out,  // exit
                                R.anim.fade_in,   // popEnter
                                R.anim.slide_out //popExit//
                        ).replace(R.id.fragmentcontainer, selectedfragment).commit();
                    }else {
                        createpopup();
                    }
                    //item.setTitle(resources.getString(R.string.home));
                    break;
                case R.id.basket:
                    selectedfragment = new MessagingFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack("MessagingFragment").setCustomAnimations(R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out //popExit//
                    ).replace(R.id.fragmentcontainer, selectedfragment).commit();
                    //item.setTitle(resources.getString(R.string.home));
                    break;
            }
            return true;
        }
    };

    private void createpopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.login_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        Button continue_btn = view.findViewById(R.id.btn_continue);
        Button order_btn = view.findViewById(R.id.btn_order);
        builder.setView(view);
        Dialog dialog3 = builder.create();
        dialog3.show();
        Window window = dialog3.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityHomeBinding.homeBottomnavigation.setSelectedItemId(R.id.home);
                dialog3.dismiss();
                selectedfragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out //popExit
                ).replace(R.id.fragmentcontainer, selectedfragment).commit();
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener nav_listner2 = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            selectedfragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    selectedfragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out //popExit
                    ).replace(R.id.fragmentcontainer, selectedfragment).commit();
                    //item.setTitle(resources.getString(R.string.myaccount));
                    break;
                case R.id.add_message:
                    selectedfragment = new AddMessageFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack("AddMessageFragment").setCustomAnimations(R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out //popExit//
                    ).replace(R.id.fragmentcontainer, selectedfragment).commit();
                    //item.setTitle(resources.getString(R.string.home));
                    //Toast.makeText(HomeActivity.this, "profile Activity", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.order:
                    selectedfragment = new OrdersFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack("OrdersFragment").setCustomAnimations(R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out //popExit//
                    ).replace(R.id.fragmentcontainer, selectedfragment).commit();
                    //item.setTitle(resources.getString(R.string.home));
                    break;
            }
            return true;
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(ProductDetailsFragment.backpressedlistener!=null) {
            // accessing the backpressedlistener of fragment
            BottomNavigationView navBar = findViewById(R.id.home_bottomnavigation);
            BottomNavigationView navBar2 = findViewById(R.id.home_bottomnavigation2);
            FrameLayout fragment_container = findViewById(R.id.fragmentcontainer);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(0, 0, 0, 160);
            fragment_container.setLayoutParams(params);
            navBar.setVisibility(View.VISIBLE);
            ProductDetailsFragment.backpressedlistener.onBackPressed();
        }else if(BasketFragment.backpressedlistener!=null) {
            // accessing the backpressedlistener of fragment
            BasketFragment.backpressedlistener.onBackPressed();
        }else if(NotificationsFragment.backpressedlistener!=null) {
            // accessing the backpressedlistener of fragment
            NotificationsFragment.backpressedlistener.onBackPressed();
        }else if(OrdersFragment.backpressedlistener!=null) {
            // accessing the backpressedlistener of fragment
            OrdersFragment.backpressedlistener.onBackPressed();
        }else if(OffersFragment.backpressedlistener!=null) {
            // accessing the backpressedlistener of fragment
            OffersFragment.backpressedlistener.onBackPressed();
        }else if(MessagingFragment.backpressedlistener!=null) {
            // accessing the backpressedlistener of fragment
            MessagingFragment.backpressedlistener.onBackPressed();
        }else if(ProfileFragment.backpressedlistener!=null) {
            // accessing the backpressedlistener of fragment
            ProfileFragment.backpressedlistener.onBackPressed();
        }else if(HomeFragment.backpressedlistener!=null) {
            // accessing the backpressedlistener of fragment
            HomeFragment.backpressedlistener.onBackPressed();
        }else if(OrderDetailsFragment.backpressedlistener!=null) {
            // accessing the backpressedlistener of fragment
            OrderDetailsFragment.backpressedlistener.onBackPressed();
        }
    }
    private void getfirebasetoken() {
        FirebaseMessaging.getInstance().subscribeToTopic(city_id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("TAG", msg);
                        //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        FirebaseMessaging.getInstance().subscribeToTopic("offer123")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d("TAG1", msg);
                        //Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        try {
            if (user != null){
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            firebase_token = task.getResult().getToken();
                            homeActivityViewModel.update_token(user.getMember().getUserId(),firebase_token);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e("exception_e",e.toString());
            e.printStackTrace();
        }
    }
}
