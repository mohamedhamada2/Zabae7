package com.alatheer.zabae7.notifications;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.Backpressedlistener;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentNotificationsBinding;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.home.profile.UserModel;
import com.alatheer.zabae7.login.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class NotificationsFragment extends Fragment implements Backpressedlistener {
    FragmentNotificationsBinding fragmentNotificationsBinding;
    NotificationViewModel notificationViewModel;
    UserSharedPreference userSharedPreference;
    User userModel;
    String user_id;
    NotificationAdapter notificationAdapter;
    LinearLayoutManager linearLayoutManager;
    DatabaseClass databaseClass;
    List<OrderItemList> orderItemListList;
    public static Backpressedlistener backpressedlistener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentNotificationsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_notifications, container, false);
        notificationViewModel = new NotificationViewModel(getContext(),this);
        fragmentNotificationsBinding.setNotificationviewmodel(notificationViewModel);
        View view = fragmentNotificationsBinding.getRoot();
        userSharedPreference = UserSharedPreference.getInstance();
        userModel = userSharedPreference.Get_UserData(getContext());
        user_id = userModel.getMember().getUserId();
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(), DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        fragmentNotificationsBinding.txtBasketNum.setText(orderItemListList.size()+"");
        if (userModel != null){
            notificationViewModel.getNotifications(user_id);
        }else {
            notificationViewModel.getNotifications("0");
        }
        fragmentNotificationsBinding.backimg.setOnClickListener(new View.OnClickListener() {
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
        fragmentNotificationsBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoBasket();
            }
        });
        return view;
    }

    private void GotoBasket() {
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

    public void init_recycler(List<Record> records) {
        notificationAdapter = new NotificationAdapter(records,getActivity(),this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        fragmentNotificationsBinding.notificationsRecycler.setHasFixedSize(true);
        fragmentNotificationsBinding.notificationsRecycler.setLayoutManager(linearLayoutManager);
        fragmentNotificationsBinding.notificationsRecycler.setAdapter(notificationAdapter);
    }

    @Override
    public void onPause() {
        // passing null value
        // to backpressedlistener
        backpressedlistener=null;
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
        backpressedlistener=this;
    }

    @Override
    public void onBackPressed() {

    }

}