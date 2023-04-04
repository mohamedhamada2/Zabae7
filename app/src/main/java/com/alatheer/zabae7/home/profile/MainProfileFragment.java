package com.alatheer.zabae7.home.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.Splash.SplashActivity;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentMainProfileBinding;
import com.alatheer.zabae7.editpassword.EditPasswordFragment;
import com.alatheer.zabae7.home.about.AboutFragment;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.home.product.ProductDetailsFragment;
import com.alatheer.zabae7.login.LoginActivity;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationModel;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.alatheer.zabae7.privacypolicy.PrivacyPolicyFragment;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MainProfileFragment extends Fragment {
    FragmentMainProfileBinding fragmentMainProfileBinding;
    UserSharedPreference userSharedPreference;
    User user;
    String user_id;
    DatabaseClass databaseClass;
    List<OrderItemList> orderItemListList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMainProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_main_profile, container, false);
        View view = fragmentMainProfileBinding.getRoot();
        getUserData();
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(), DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        fragmentMainProfileBinding.txtBasketNum.setText(orderItemListList.size()+"");
        fragmentMainProfileBinding.relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().addToBackStack("ProfileFragment").setCustomAnimations(R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out). //popExit)
                        replace(R.id.fragmentcontainer, fragment).commit();
            }
        });
        fragmentMainProfileBinding.relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EditPasswordFragment();
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().addToBackStack("ProfileFragment").setCustomAnimations(R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out). //popExit)
                        replace(R.id.fragmentcontainer, fragment).commit();
            }
        });
        fragmentMainProfileBinding.relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new PrivacyPolicyFragment();
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().addToBackStack("AboutFragment").setCustomAnimations(R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out). //popExit)
                        replace(R.id.fragmentcontainer, fragment).commit();
            }
        });
        fragmentMainProfileBinding.relative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AboutFragment();
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().addToBackStack("AboutFragment").setCustomAnimations(R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out). //popExit)
                        replace(R.id.fragmentcontainer, fragment).commit();
            }
        });
        fragmentMainProfileBinding.relative5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               userSharedPreference.ClearData(getActivity());
               startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });

        return view;
    }

    private void Login(String userPhone) {
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = getDataService.login_User(userPhone,userPhone);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()==1){
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), SplashActivity.class));
                            Animatoo.animateSlideLeft(getActivity());
                            userSharedPreference.Create_Update_UserData(getActivity(),response.body());
                            getActivity().finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    private void getUserData() {
        try {
            userSharedPreference = UserSharedPreference.getInstance();
            user = userSharedPreference.Get_UserData(getActivity());
            user_id = user.getMember().getUserId();
            getNotifications(user_id);
            getProfileData();
        }catch (Exception e){
            Toast.makeText(getActivity(), "برجاء تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),LoginActivity.class));
        }


    }

    private void getNotifications(String user_id) {
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<NotificationModel> call = getDataService.get_notifications(user_id);
            call.enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    if (response.isSuccessful()){
                        setNotificationsCount(response.body().getCount());
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {

                }
            });
        }
    }

    private void setNotificationsCount(int size) {
        if (size != 0){
            fragmentMainProfileBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentMainProfileBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentMainProfileBinding.txtNotificationNum.setVisibility(View.GONE);
        }
    }

    private void getProfileData() {
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<UserModel> call = getDataService.get_profile(user_id);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getMImage()!= null){
                            Picasso.get().load("https://thabaeh.com/uploads/user/"+response.body().getMImage()).into(fragmentMainProfileBinding.userImg);
                        }else {
                            fragmentMainProfileBinding.userImg.setImageResource(R.drawable.user_img9);
                        }

                        fragmentMainProfileBinding.txtUsername.setText(response.body().getUserName());
                        fragmentMainProfileBinding.txtPhone.setText(response.body().getUserPhone());
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                }
            });
            fragmentMainProfileBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GotoBasket();
                }
            });
            fragmentMainProfileBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GotoNotifications();
                }
            });
        }
    }
    private void GotoNotifications() {
        Fragment fragment = new NotificationsFragment();
        //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
        // fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("NotificationFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
        BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
        BottomNavigationView navBar2 = getActivity().findViewById(R.id.home_bottomnavigation2);
        FrameLayout fragment_container = getActivity().findViewById(R.id.fragmentcontainer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, 0);
        fragment_container.setLayoutParams(params);
        navBar.setVisibility(View.GONE);
        navBar2.setVisibility(View.GONE);
    }
    public void GotoBasket() {
        Fragment fragment = new BasketFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flag","2");
        fragment.setArguments(bundle);
        //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
        // fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("BasketFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
        BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
        BottomNavigationView navBar2 = getActivity().findViewById(R.id.home_bottomnavigation2);
        FrameLayout fragment_container = getActivity().findViewById(R.id.fragmentcontainer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, 0);
        fragment_container.setLayoutParams(params);
        navBar.setVisibility(View.GONE);
        navBar2.setVisibility(View.GONE);
    }
}
