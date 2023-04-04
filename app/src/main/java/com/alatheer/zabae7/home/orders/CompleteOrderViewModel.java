package com.alatheer.zabae7.home.orders;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.home.basket.BasketModel;
import com.alatheer.zabae7.home.basket.OrderSuccess;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.login.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteOrderViewModel {
    Context context;
    CompleteOrderFragment completeOrderFragment;
    List<OrderItemList> orderItemListList;
    String orderdate,order_time;
    DatabaseClass databaseClass;
    UserSharedPreference userSharedPreference;
    String payment_method;
    public CompleteOrderViewModel(Context context, CompleteOrderFragment completeOrderFragment) {
        this.context = context;
        this.completeOrderFragment = completeOrderFragment;
    }

    public void add_order(String name, String address, String phone, String city_id,String payment_method,String phone2) {

        /*Fragment fragment = new VerificationCodeFragment();
        FragmentManager fragmentManager = completeOrderFragment.getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("address",address);
        bundle.putString("phone",phone);
        bundle.putString("phone2",phone2);
        bundle.putString("city_id",city_id);
        bundle.putString("payment_method",payment_method);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().addToBackStack("VerificationCodeFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();*/
        //
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        orderdate = df.format(c);
        Log.e("date", orderdate);
        String delegate = "hh:mm aaa";
        order_time = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        databaseClass = Room.databaseBuilder(context.getApplicationContext(), DatabaseClass.class, "my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        BasketModel basketModel = new BasketModel();
        basketModel.setUserId("");
        basketModel.setToken("");
        basketModel.setUserName(name);
        basketModel.setUserCity(city_id);
        basketModel.setUserPhone(phone);
        basketModel.setAddress(address);
        basketModel.setOrderItemList(orderItemListList);
        basketModel.setOrderDate(orderdate);
        basketModel.setOrderTime(order_time);
        basketModel.setPay_method(payment_method);
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<OrderSuccess> call = getDataService.send_order(basketModel);
        call.enqueue(new Callback<OrderSuccess>() {
            @Override
            public void onResponse(Call<OrderSuccess> call, Response<OrderSuccess> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {
                        //Toast.makeText(context, "تم استلام الطلب برجاء تفعيل الحساب وسيتم التواصل معك لاستكمال البيانات ...", Toast.LENGTH_LONG).show();
                        databaseClass.getDao().deleteAllproduct();
                        Login(phone,phone);


                    }
                }
            }

            @Override
            public void onFailure(Call<OrderSuccess> call, Throwable t) {

            }
        });

    }

    private void Login(String phone, String phone1) {
        userSharedPreference = UserSharedPreference.getInstance();
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = getDataService.login_User(phone, phone1);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(context, "تم استلام الطلب وسيتم التواصل معك قريبا", Toast.LENGTH_LONG).show();
                            User user = response.body();
                            userSharedPreference.Create_Update_UserData(context, user);
                            completeOrderFragment.go_to_home();
                        } else {
                            //Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    public void CreateDialog(String name, String address, String phone, String city_id,String phone2) {
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(context).inflate(R.layout.payment_dialog, null);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        RadioGroup radioGroup = view.findViewById(R.id.radio);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Checked_id = radioGroup.getCheckedRadioButtonId();
                if (Checked_id == -1){
                    Toast.makeText(context, "اختر طرية الدفع", Toast.LENGTH_SHORT).show();
                }else {
                    findRadioButton(Checked_id,name,address,phone,city_id,phone2);
                    dialog.dismiss();

                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //dialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        try {
            dialog.show();
        }catch (WindowManager.BadTokenException e){
            dialog.dismiss();
        }

    }

    private void findRadioButton(int checked_id, String name, String address, String phone, String city_id,String phone2) {
        switch (checked_id){
            case R.id.radio_btn1:
                payment_method ="1";
                Toast.makeText(context, payment_method, Toast.LENGTH_SHORT).show();
                add_order(name,address,phone,city_id,payment_method,phone2);
                break;
            case R.id.radio_btn2:
                payment_method="2";
                Toast.makeText(context, payment_method, Toast.LENGTH_SHORT).show();
                add_order(name,address,phone,city_id,payment_method,phone2);
                break;
        }
    }
}
