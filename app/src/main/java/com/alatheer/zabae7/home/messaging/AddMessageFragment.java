package com.alatheer.zabae7.home.messaging;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentAddMessageBinding;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.home.profile.ProfileFragment;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class AddMessageFragment extends Fragment {
    FragmentAddMessageBinding fragmentAddMessageBinding;
    AddMessageViewModel addMessageViewModel;
    String phone,title,content;
    UserSharedPreference userSharedPreference;
    User user;
    String user_id,name;
    DatabaseClass databaseClass;
    List<OrderItemList> orderItemListList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentAddMessageBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_message, container, false);
        View view = fragmentAddMessageBinding.getRoot();
        addMessageViewModel = new AddMessageViewModel(getActivity(),this);
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(), DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        fragmentAddMessageBinding.txtBasketNum.setText(orderItemListList.size()+"");
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
        fragmentAddMessageBinding.setAddmessageviewmodel(addMessageViewModel);
        try {
            addMessageViewModel.getSharedpreferancedata();
        }catch (Exception e){
        }
        fragmentAddMessageBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();
            }
        });
        fragmentAddMessageBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
                BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
                BottomNavigationView navBar2 = getActivity().findViewById(R.id.home_bottomnavigation2);
                FrameLayout fragment_container = getActivity().findViewById(R.id.fragmentcontainer);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                params.setMargins(0, 0, 0, 160);
                fragment_container.setLayoutParams(params);
                navBar.setVisibility(View.VISIBLE);
            }
        });
        fragmentAddMessageBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoBasket();
            }
        });
        fragmentAddMessageBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoNotifications();
            }
        });
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(getActivity());
        if (user != null){
            user_id = user.getMember().getUserId();
            addMessageViewModel.getNotifications(user_id);
        }else {
            addMessageViewModel.getNotifications("0");
        }
        return view;
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

    private void Validation() {
        title = fragmentAddMessageBinding.etMessageTitle.getText().toString();
        content = fragmentAddMessageBinding.etMessageContent.getText().toString();
        phone = fragmentAddMessageBinding.etPhone.getText().toString();
        name = fragmentAddMessageBinding.etUserName.getText().toString();
        if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(name)){
            addMessageViewModel.send_message(phone,title,content,name);
        }else {
            if (TextUtils.isEmpty(name)){
                fragmentAddMessageBinding.etUserName.setError("برجاء اضافة الإسم");
            }else {
                fragmentAddMessageBinding.etUserName.setError(null);
            }
            if(TextUtils.isEmpty(title)){
                fragmentAddMessageBinding.etMessageTitle.setError("برجاء اضافة عنوان الشكوي");
            }else {
                fragmentAddMessageBinding.etMessageTitle.setError(null);
            }
            if (TextUtils.isEmpty(content)){
                fragmentAddMessageBinding.etMessageContent.setError("برجاء اضافة محتوي الشكوي");
            }else {
                fragmentAddMessageBinding.etMessageContent.setError(null);
            }
            if(TextUtils.isEmpty(phone)){
                fragmentAddMessageBinding.etMessageTitle.setError("برجاء اضافة رقم الجوال");
            }else {
                fragmentAddMessageBinding.etMessageTitle.setError(null);
            }
        }
    }

    public void sendUser(User user) {
        if (user != null){
            fragmentAddMessageBinding.etPhone.setText(user.getMember().getUserPhone());
            fragmentAddMessageBinding.etPhone.setFocusable(false);
            fragmentAddMessageBinding.etPhone.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            fragmentAddMessageBinding.etPhone.setClickable(false);
        }
    }

    public void setNotificationsCount(int size) {
        if (size != 0){
            fragmentAddMessageBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentAddMessageBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentAddMessageBinding.txtNotificationNum.setVisibility(View.GONE);
        }
    }
}
