package com.alatheer.zabae7.payment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.editpassword.SuccessModel;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.basket.BasketModel;
import com.alatheer.zabae7.home.basket.OrderSuccess;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.signup.CityModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentViewModel {
    UserSharedPreference userSharedPreference;
    Context context;
    List<Payment> paymentList;
    PaymentDataActivity paymentDataActivity;
    PersonalDataActivity personalDataActivity;
    List<AllCity> allCities;
    List<String> allcitiestitles;
    List<OrderItemList> orderItemListList;
    String orderdate,order_time;
    DatabaseClass databaseClass;
    public PaymentViewModel(Context context,Integer flag) {
        this.context = context;
        if (flag == 1){
            personalDataActivity = (PersonalDataActivity) context;
        }else if (flag == 2){
            paymentDataActivity = (PaymentDataActivity) context;
        }
    }

    public void get_payment_methods() {
        paymentList = new ArrayList<>();
        Payment payment = new Payment();
        payment.setId("1");
        payment.setName("الدفع عند الإستلام");
        Payment payment2 = new Payment();
        payment2.setId("2");
        payment2.setName("الدفع بالتحويل البنكي");
        Payment payment3 = new Payment();
        payment3.setId("3");
        payment3.setName("الدفع ببطاقة الإئتمان");
        paymentList.add(payment);
        paymentList.add(payment2);
        paymentList.add(payment3);
        paymentDataActivity.init_recycler(paymentList);

    }

    public void getCities() {
        allcitiestitles = new ArrayList<>();
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CityModel> call = getDataService.getCities();
            call.enqueue(new Callback<CityModel>() {
                @Override
                public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                    if (response.isSuccessful()) {
                        allCities = response.body().getAllCities();
                        for (AllCity allCity : allCities) {
                            allcitiestitles.add(allCity.getCityName());
                        }
                        personalDataActivity.setSpinner(allcitiestitles,allCities);
                    }
                }

                @Override
                public void onFailure(Call<CityModel> call, Throwable t) {

                }
            });
        }
    }

    public void add_order(String user_id,String user_name, String user_address, String user_phone, String user_city, String payment_id,String city_id,String promo_code) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        orderdate = df.format(c);
        Log.e("date", orderdate);
        String delegate = "hh:mm aaa";
        order_time = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        databaseClass = Room.databaseBuilder(context.getApplicationContext(), DatabaseClass.class, "my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        BasketModel basketModel = new BasketModel();
        basketModel.setUserId(user_id);
        basketModel.setToken("");
        basketModel.setUserName(user_name);
        basketModel.setUserCity(user_city);
        basketModel.setUserPhone(user_phone);
        basketModel.setAddress(user_address);
        basketModel.setCity_id(city_id);
        basketModel.setPromo_code(promo_code);
        basketModel.setOrderItemList(orderItemListList);
        basketModel.setOrderDate(orderdate);
        basketModel.setOrderTime(order_time);
        basketModel.setPay_method(payment_id);
        if (!orderItemListList.isEmpty()){
            if (Utilities.isNetworkAvailable(context)){
                ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("تحميل ...");
                pd.show();
                GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<OrderSuccess> call = getDataService.send_order(basketModel);
                call.enqueue(new Callback<OrderSuccess>() {
                    @Override
                    public void onResponse(Call<OrderSuccess> call, Response<OrderSuccess> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess() == 1) {
                                pd.dismiss();
                                databaseClass.getDao().deleteAllproduct();
                                CreatePopUp2(response.body().getMessage());
                                //Login(phone,phone);
                            }else {
                                pd.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderSuccess> call, Throwable t) {

                    }
                });
            }
        }else {
            Toast.makeText(context, "عفوا السلة فارغة لقد قمت بالطلب هذا من قبل", Toast.LENGTH_LONG).show();
        }
    }

    public void add_order_without_login(String user_id, String user_name, String user_address, String user_phone, String user_city, String payment_id, String city_id, String promo_code) {
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
        basketModel.setUserName(user_name);
        basketModel.setUserCity(city_id);
        basketModel.setUserPhone(user_phone);
        basketModel.setAddress(user_address);
        basketModel.setOrderItemList(orderItemListList);
        basketModel.setOrderDate(orderdate);
        basketModel.setOrderTime(order_time);
        basketModel.setPay_method(payment_id);
        basketModel.setCity_id(city_id);
        basketModel.setPromo_code(promo_code);
        if (!orderItemListList.isEmpty()){
            if (Utilities.isNetworkAvailable(context)){
                ProgressDialog pd = new ProgressDialog(context);
                pd.setMessage("تحميل ...");
                pd.show();
                GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<OrderSuccess> call = getDataService.send_order(basketModel);
                call.enqueue(new Callback<OrderSuccess>() {
                    @Override
                    public void onResponse(Call<OrderSuccess> call, Response<OrderSuccess> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess() == 1) {
                                pd.dismiss();
                                databaseClass.getDao().deleteAllproduct();
                                CreatePopUpwithoutLogin(response.body().getMessage(),user_phone);

                            }else {
                                pd.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderSuccess> call, Throwable t) {

                    }
                });
            }
        }else {
            Toast.makeText(context, "عفوا السلة فارغة لقد قمت بالطلب هذا من قبل", Toast.LENGTH_LONG).show();
        }
    }

    private void CreatePopUpwithoutLogin(String message,String user_phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.complete_order_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        Button continue_btn = view.findViewById(R.id.btn_continue);
        Button order_btn = view.findViewById(R.id.btn_order);
        TextView txt = view.findViewById(R.id.txt_delete);
        txt.setText(message);
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
                Login(user_phone,user_phone);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                Login2(user_phone,user_phone);
            }
        });
    }
    private void CreatePopUp2(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.complete_order_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        Button continue_btn = view.findViewById(R.id.btn_continue);
        Button order_btn = view.findViewById(R.id.btn_order);
        TextView txt = view.findViewById(R.id.txt_delete);
        txt.setText(message);
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
                Intent intent = new Intent(paymentDataActivity, HomeActivity.class);
                intent.putExtra("flag",1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.dismiss();
                Intent intent = new Intent(paymentDataActivity, HomeActivity.class);
                intent.putExtra("flag",3);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void Login(String user_phone, String user_phone1) {
        userSharedPreference = UserSharedPreference.getInstance();
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = getDataService.login_User(user_phone, user_phone1);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            //Toast.makeText(context, "تم استلام الطلب وسيتم التواصل معك قريبا", Toast.LENGTH_LONG).show();
                            User user = response.body();
                            userSharedPreference.Create_Update_UserData(context, user);
                            databaseClass.getDao().deleteAllproduct();
                            Intent intent = new Intent(paymentDataActivity, HomeActivity.class);
                            intent.putExtra("flag",1);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
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
    private void Login2(String user_phone, String user_phone1) {
        userSharedPreference = UserSharedPreference.getInstance();
        if (Utilities.isNetworkAvailable(context)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = getDataService.login_User(user_phone, user_phone1);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {
                            //Toast.makeText(context, "تم استلام الطلب وسيتم التواصل معك قريبا", Toast.LENGTH_LONG).show();
                            User user = response.body();
                            userSharedPreference.Create_Update_UserData(context, user);
                            databaseClass.getDao().deleteAllproduct();
                            Intent intent = new Intent(paymentDataActivity, HomeActivity.class);
                            intent.putExtra("flag",3);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
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
    public void get_copon_discount(String copon) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<SuccessModel> call = getDataService.check_copon(copon);
            call.enqueue(new Callback<SuccessModel>() {
                @Override
                public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess() == 0){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            paymentDataActivity.setTotalPrice2(response.body().getDiscount(),response.body().getId());
                        }else if (response.body().getSuccess() == 1){
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            paymentDataActivity.setTotalPrice(response.body().getDiscount(),response.body().getId());
                        }
                    }
                }

                @Override
                public void onFailure(Call<SuccessModel> call, Throwable t) {

                }
            });
        }
    }

}
