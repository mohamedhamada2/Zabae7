package com.alatheer.zabae7.home.messaging;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.Backpressedlistener;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentMessagingBinding;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.home2.HomeFragment;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.login.LoginActivity;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MessagingFragment extends Fragment implements Backpressedlistener {
    FragmentMessagingBinding fragmentMessagingBinding;
    MessagingViewModel messagingViewModel;
    RecyclerView.LayoutManager layoutManager;
    MessageAdapter messageAdapter;
    UserSharedPreference userSharedPreference;
    User user;
    DatabaseClass databaseClass;
    List<OrderItemList>orderItemListList;
    String user_id;
    public static Backpressedlistener backpressedlistener;
    public void setData(MessageModel messageModel) {
        Fragment fragment = new MessageDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("message",messageModel);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("MessageDetailsFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMessagingBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_messaging, container, false);
        View view = fragmentMessagingBinding.getRoot();
        messagingViewModel = new MessagingViewModel(getActivity(),this);
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(), DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        fragmentMessagingBinding.txtBasketNum.setText(orderItemListList.size()+"");
        try {
            userSharedPreference = UserSharedPreference.getInstance();
            user = userSharedPreference.Get_UserData(getActivity());
            if (user != null){
                user_id = user.getMember().getUserId();
                messagingViewModel.getNotifications(user_id);
            }else {
                messagingViewModel.getNotifications("0");
            }
            messagingViewModel.getSharedPreferanceData();
            messagingViewModel.get_messaging();
            fragmentMessagingBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new AddMessageFragment();
                    //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
                    // fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();

                    fragmentManager.beginTransaction().addToBackStack("AddMessageFragment").setCustomAnimations(R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out). //popExit)
                            replace(R.id.fragmentcontainer, fragment).commit();
                }
            });
        }catch (Exception e){
            startActivity( new Intent(getActivity(), LoginActivity.class));
            Toast.makeText(getActivity(), "برجاء تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
        }
        fragmentMessagingBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoBasket();
            }
        });
        fragmentMessagingBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoNotifications();
            }
        });
        fragmentMessagingBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }

    public void init_recycler(List<MessageModel> body) {
        layoutManager = new LinearLayoutManager(getActivity());
        messageAdapter = new MessageAdapter(body,getActivity(),this);
        fragmentMessagingBinding.messageRecycler.setHasFixedSize(true);
        fragmentMessagingBinding.messageRecycler.setLayoutManager(layoutManager);
        fragmentMessagingBinding.messageRecycler.setAdapter(messageAdapter);
    }

    public void show_msg() {
        fragmentMessagingBinding.noMessageLayout.setVisibility(View.VISIBLE);
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

    public void setNotificationsCount(int size) {
        if (size != 0){
            fragmentMessagingBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentMessagingBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentMessagingBinding.txtNotificationNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

    }
    @Override
    public void onPause() {
        // passing null value
        // to backpressedlistener
        backpressedlistener = null;
        super.onPause();
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


    // Overriding onResume() method
    @Override
    public void onResume() {
        super.onResume();
        // passing context of fragment
        //  to backpressedlistener
        backpressedlistener = this;

    }
}
