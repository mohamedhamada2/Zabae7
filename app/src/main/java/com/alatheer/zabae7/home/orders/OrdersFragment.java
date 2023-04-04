package com.alatheer.zabae7.home.orders;

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

import android.text.Layout;
import android.util.Log;
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
import com.alatheer.zabae7.databinding.FragmentOrdersBinding;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.login.LoginActivity;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class OrdersFragment extends Fragment implements Backpressedlistener {
    FragmentOrdersBinding fragmentOrdersBinding;
    OrderViewModel orderViewModel;
    OrderAdapter orderAdapter;
    RecyclerView.LayoutManager layoutManager;
    UserSharedPreference userSharedPreference;
    LinearLayoutManager orders_status_manager;
    User user;
    OrderStatusAdapter orderStatusAdapter;
    DatabaseClass databaseClass;
    List<OrderItemList> orderItemListList;
    public static Backpressedlistener backpressedlistener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentOrdersBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_orders, container, false);
        View view = fragmentOrdersBinding.getRoot();
        orderViewModel = new OrderViewModel(getActivity(),this);
        fragmentOrdersBinding.setOrderviewmodel(orderViewModel);
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(), DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        fragmentOrdersBinding.txtBasketNum.setText(orderItemListList.size()+"");
        try {
            userSharedPreference = UserSharedPreference.getInstance();
            user = userSharedPreference.Get_UserData(getActivity());
            orderViewModel.getSharedpreferanceData();
            orderViewModel.get_orders_status();
            orderViewModel.getallorders("1");
        }catch (Exception e){
            Log.e("error4",e.getMessage());
            Toast.makeText(getActivity(), "برجاء تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        fragmentOrdersBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoBasket();
            }
        });
        fragmentOrdersBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoNotifications();
            }
        });
        fragmentOrdersBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return view;
    }

    public void init_recycler(List<Order> orderList) {
        orderAdapter = new OrderAdapter(orderList,getActivity(),this);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentOrdersBinding.ordersRecycler.setHasFixedSize(true);
        fragmentOrdersBinding.ordersRecycler.setAdapter(orderAdapter);
        fragmentOrdersBinding.ordersRecycler.setLayoutManager(layoutManager);
    }

    public void sendData(Order order) {
        Fragment fragment = new OrderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("model", order);
        //Toast.makeText(getActivity(),"hello"+ personalInformationModel.getHaveCar(), Toast.LENGTH_SHORT).show();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().addToBackStack("OrderDetailsFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
    }

    public void init_order_status(List<OrderStatus> orderStatusList) {
        orders_status_manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        orders_status_manager.setReverseLayout(true);
        orderStatusAdapter = new OrderStatusAdapter(orderStatusList,getActivity(),this);
        fragmentOrdersBinding.orderStatusRecycler.setAdapter(orderStatusAdapter);
        fragmentOrdersBinding.orderStatusRecycler.setLayoutManager(orders_status_manager);
        fragmentOrdersBinding.orderStatusRecycler.setHasFixedSize(true);
    }

    public void getallorders(String type) {
        orderViewModel.getallorders(type);
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
            fragmentOrdersBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentOrdersBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentOrdersBinding.txtNotificationNum.setVisibility(View.GONE);
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
