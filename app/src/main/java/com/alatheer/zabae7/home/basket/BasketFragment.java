package com.alatheer.zabae7.home.basket;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.Backpressedlistener;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentBasketBinding;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.home2.HomeFragment;
import com.alatheer.zabae7.home.orders.CompleteOrderFragment;
import com.alatheer.zabae7.home.orders.OrdersFragment;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.home.profile.ProfileFragment;
import com.alatheer.zabae7.home.profile.UserModel;
import com.alatheer.zabae7.login.LoginActivity;
import com.alatheer.zabae7.login.LoginActivity2;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.payment.PersonalDataActivity;
import com.alatheer.zabae7.singleton.OrderItemsSingleTone;
import com.alatheer.zabae7.updateproduct.UpdateProductFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class BasketFragment extends Fragment implements Backpressedlistener {
    FragmentBasketBinding fragmentBasketBinding;
    BasketViewModel basketViewModel ;
    OrderItemsSingleTone orderItemsSingleTone;
    List<OrderItemList> orderItemListList;
    BasketAdapter basketAdapter;
    RecyclerView.LayoutManager layoutManager;
    BasketModel basketModel;
    UserSharedPreference userSharedPreference;
    User userModel;
    String user_id = null;
    String user_name,user_phone,user_city,token,city_id,flag;
    String currentTime,orderdate,order_time;
    Integer totalPrice;
    DatabaseClass databaseClass;
    Bundle bundle;
    public static Backpressedlistener backpressedlistener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentBasketBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_basket, container, false);
        View view = fragmentBasketBinding.getRoot();
        databaseClass = Room.databaseBuilder(getActivity().getApplicationContext(),DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        basketViewModel = new BasketViewModel(getActivity(),this);
        fragmentBasketBinding.setBasketviewmodel(basketViewModel);
        bundle = this.getArguments();
        flag = bundle.getString("flag");
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
        //Log.e("user_id",user_id);
        initBasket();
        grandTotal();
        fragmentBasketBinding.txtTotalPrice.setText(totalPrice+"");
        fragmentBasketBinding.totalPrice.setText("ريال");
        fragmentBasketBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
                BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
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
        try {
            userSharedPreference = UserSharedPreference.getInstance();
            userModel = userSharedPreference.Get_UserData(getActivity());
            user_id = userModel.getMember().getUserId();
            if (userModel != null){
                basketViewModel.getNotifications(user_id);
            }else {
                basketViewModel.getNotifications("0");
            }
            token = userModel.getToken();
            basketViewModel.getProfileData(user_id);
            fragmentBasketBinding.sendOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!orderItemListList.isEmpty()){
                        if (userModel != null){
                            //basketViewModel.CreateDialog(basketModel);
                            Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                            intent.putExtra("totalprice",totalPrice);
                            //Toast.makeText(getContext(), totalPrice+"", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            createpopup();
                        }
                        //basketViewModel.sendOrder(basketModel);
                    }else {
                        Toast.makeText(getActivity(), "السلة فارغة لا يمكن ارسال الطلب", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }catch (Exception e){
            fragmentBasketBinding.sendOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!orderItemListList.isEmpty()){
                        Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                        intent.putExtra("totalprice",totalPrice);
                        //Toast.makeText(getContext(), totalPrice+"", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        getActivity().finish();
                        //basketViewModel.sendOrder(basketModel);
                    }else {
                        Toast.makeText(getActivity(), "السلة فارغة لا يمكن ارسال الطلب", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            fragmentBasketBinding.backimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag.equals("2")){
                        getFragmentManager().popBackStack();
                    }else {
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
                        if (userModel != null){
                            navBar.setVisibility(View.VISIBLE);
                        }else {
                            navBar.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            /*Toast.makeText(getActivity(), "برجاء تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));*/
        }
        return view;
    }

    private void createpopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                dialog3.dismiss();
                Intent intent = new Intent(getActivity(), PersonalDataActivity.class);
                intent.putExtra("totalprice",totalPrice);
                //Toast.makeText(getContext(), totalPrice+"", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                getActivity().finish();
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    private void initBasket() {
        orderItemsSingleTone = OrderItemsSingleTone.newInstance();
        orderItemListList = databaseClass.getDao().getallproducts();
        basketAdapter = new BasketAdapter(getActivity(),orderItemListList,this);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentBasketBinding.basketRecycler.setLayoutManager(layoutManager);
        fragmentBasketBinding.basketRecycler.setAdapter(basketAdapter);
        fragmentBasketBinding.basketRecycler.setHasFixedSize(true);
    }

    public void setData(UserModel userModel) {
        user_name = userModel.getUserName();
        user_phone = userModel.getUserPhone();
        user_city = userModel.getUserCity();
        save_to_basket();
    }

    private void save_to_basket() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        orderdate = df.format(c);
        Log.e("date",orderdate);
        String delegate = "hh:mm aaa";
        order_time =(String) DateFormat.format(delegate,Calendar.getInstance().getTime());
        basketModel = new BasketModel();
        if (user_id != null){
            basketModel.setUserId(user_id);
            basketModel.setUserName(user_name);
            basketModel.setUserCity(user_city);
            basketModel.setUserPhone(user_phone);
            basketModel.setAddress("");
            basketModel.setToken(token);
            basketModel.setOrderItemList(orderItemListList);
            basketModel.setOrderDate(orderdate);
            basketModel.setOrderTime(order_time);
        }

    }
    private Integer grandTotal() {
        totalPrice = 0;
        //Log.e("totalprice",orderItemListList.get(0).getTotal_price());
        for(int i = 0 ; i < orderItemListList.size(); i++) {
            totalPrice += Integer.parseInt(orderItemListList.get(i).getTotal_price());
            }
        return totalPrice;
    }
    public void UpdateList() {
        totalPrice = 0;
        Log.e("totalprice",orderItemListList.get(0).getTotal_price());
        for (int i = 0; i < orderItemListList.size(); i++) {
            totalPrice += Integer.parseInt(orderItemListList.get(i).getTotal_price());
        }
        fragmentBasketBinding.txtTotalPrice.setText(totalPrice+"");
    }

    public void restart() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        //grandTotal();
        ft.detach(this).attach(BasketFragment.this).commit();
    }

    public void go_to_home() {
        Fragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("city_id",city_id);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        BottomNavigationView navBar = getActivity().findViewById(R.id.home_bottomnavigation);
        FrameLayout fragment_container = getActivity().findViewById(R.id.fragmentcontainer);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 160);
        fragment_container.setLayoutParams(params);
        navBar.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
    }

    public void update_order(OrderItemList orderItemList) {
        Fragment fragment = new UpdateProductFragment();
        FragmentManager fragmentManager =getFragmentManager();
        Bundle b = new Bundle();
        b.putSerializable("order",orderItemList);
        fragment.setArguments(b);
        fragmentManager.beginTransaction().addToBackStack("updatefragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
    }

    public void setNotificationsCount(int size) {
        if (size != 0){
            fragmentBasketBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentBasketBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentBasketBinding.txtNotificationNum.setVisibility(View.GONE);
        }
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
