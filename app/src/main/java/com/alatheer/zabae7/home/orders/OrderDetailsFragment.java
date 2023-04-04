package com.alatheer.zabae7.home.orders;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.Backpressedlistener;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.FragmentOrderDetailsBinding;
import com.alatheer.zabae7.databinding.FragmentProductDetailsBinding;
import com.alatheer.zabae7.editpassword.SuccessModel;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.home.profile.ProfileFragment;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.notifications.NotificationModel;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsFragment extends Fragment implements Backpressedlistener {
    Bundle bundle;
    Order order;
    List<OrderDetail> orderDetailList;
    OrderDetailAdapter orderDetailAdapter;
    RecyclerView.LayoutManager layoutManager;
    FragmentOrderDetailsBinding fragmentOrderDetailsBinding;
    DatabaseClass databaseClass;
    List<OrderItemList> orderItemListList;
    String user_id;
    UserSharedPreference userSharedPreference;
    User user;
    Dialog dialog3;
    Integer total_price;
    public static Backpressedlistener backpressedlistener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentOrderDetailsBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_order_details, container, false);
        View v = fragmentOrderDetailsBinding.getRoot();
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(getActivity());
        if (user != null){
            user_id = user.getMember().getUserId();
            getNotifications(user_id);
        }else {
            getNotifications("0");
        }
        bundle = this.getArguments();
        order = bundle.getParcelable("model");
        if (order.getHaletTalab().equals("neworder")){
            fragmentOrderDetailsBinding.btnCancelOrder.setVisibility(View.VISIBLE);
        }else{
            fragmentOrderDetailsBinding.btnCancelOrder.setVisibility(View.GONE);
        }
        orderDetailList = order.getOrderDetails();
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(), DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        fragmentOrderDetailsBinding.txtBasketNum.setText(orderItemListList.size()+"");
        fragmentOrderDetailsBinding.txtOrderNum.setText(order.getOrderId());
        fragmentOrderDetailsBinding.txtOrderDate.setText(order.getOrderDate());
        if (order.getHaletTalab().equals("neworder")){
            fragmentOrderDetailsBinding.txtOrderStatus.setText("طلب جديد");
        }else if (order.getHaletTalab().equals("inprogress")){
            fragmentOrderDetailsBinding.txtOrderStatus.setText("قيد التنفيذ");
        }else if (order.getHaletTalab().equals("tawsel")){
            fragmentOrderDetailsBinding.txtOrderStatus.setText("قيد التوصيل");
        }else if (order.getHaletTalab().equals("completed")){
            fragmentOrderDetailsBinding.txtOrderStatus.setText("تم التوصيل");
        }else if (order.getHaletTalab().equals("cancelled")){
            fragmentOrderDetailsBinding.txtOrderStatus.setText("ملغي");
        }
        if (order.getPromo_value() != null){
            if (order.getPromo_value().equals("0")){
                fragmentOrderDetailsBinding.relativeDiscount.setVisibility(View.GONE);
                fragmentOrderDetailsBinding.txtTotalPrice.setText(order.getAllSum()+"");
            }else {
                fragmentOrderDetailsBinding.relativeDiscount.setVisibility(View.VISIBLE);
                fragmentOrderDetailsBinding.txtDiscountPrice.setText(order.getPromo_value());
                fragmentOrderDetailsBinding.discountPrice.setText("ريال");
                total_price = order.getAllSum()-Integer.parseInt(order.getPromo_value());
                fragmentOrderDetailsBinding.txtTotalPrice.setText(total_price+"");
            }
        }else {
            fragmentOrderDetailsBinding.relativeDiscount.setVisibility(View.GONE);
            fragmentOrderDetailsBinding.txtTotalPrice.setText(order.getAllSum()+"");
        }
        fragmentOrderDetailsBinding.totalPrice.setText("ريال");
        fragmentOrderDetailsBinding.txtOrderAddress.setText(order.getAddress());
        if (order.getPayMethod().equals("1")){
            fragmentOrderDetailsBinding.txtPaymentMethods.setText("عند الإستلام");
        }else if (order.getPayMethod().equals("2")){
            fragmentOrderDetailsBinding.txtPaymentMethods.setText("تحويل بنكي");
        }else if (order.getPayMethod().equals("3")){
            fragmentOrderDetailsBinding.txtPaymentMethods.setText("بطاقة إئتمان");
        }
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
        //Toast.makeText(getActivity(), order.getHaletTalab(), Toast.LENGTH_SHORT).show();
        orderDetailAdapter = new OrderDetailAdapter(orderDetailList,getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentOrderDetailsBinding.orderDetailsRecycler.setHasFixedSize(true);
        fragmentOrderDetailsBinding.orderDetailsRecycler.setLayoutManager(layoutManager);
        fragmentOrderDetailsBinding.orderDetailsRecycler.setAdapter(orderDetailAdapter);
        fragmentOrderDetailsBinding.backimg.setOnClickListener(new View.OnClickListener() {
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
        fragmentOrderDetailsBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoBasket();
            }
        });
        fragmentOrderDetailsBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoNotifications();
            }
        });
        fragmentOrderDetailsBinding.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createpopup(order.getOrderId(),user_id);
            }
        });
        return v;
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
            fragmentOrderDetailsBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentOrderDetailsBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentOrderDetailsBinding.txtNotificationNum.setVisibility(View.GONE);
        }
    }

    private void createpopup(String order_id,String user_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.cancel_order_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_confirm = view.findViewById(R.id.btn_confirm);
        EditText et_reason = view.findViewById(R.id.et_reason);
        builder.setView(view);
        dialog3 = builder.create();
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
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String reason = et_reason.getText().toString();
               cancel_order(order_id,user_id,reason);
            }
        });
    }

    private void cancel_order(String order_id, String user_id, String reason) {
        if (Utilities.isNetworkAvailable(getActivity())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SuccessModel> call = getDataService.cancel_order(order_id,user_id,reason);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        dialog3.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {

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
