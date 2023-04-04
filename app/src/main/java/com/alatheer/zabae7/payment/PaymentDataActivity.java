package com.alatheer.zabae7.payment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.alatheer.zabae7.data.CitySharedPreferance;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.ActivityPaymentDataBinding;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.signup.CityModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class PaymentDataActivity extends AppCompatActivity {
    ActivityPaymentDataBinding activityPaymentDataBinding;
    PaymentViewModel paymentViewModel;
    PaymentAdapter paymentAdapter;
    RecyclerView.LayoutManager layoutManager;
    String user_name,user_phone,user_address,user_city,user_id,city_id,promo_code="0";
    String payment_id = "1";
    CitySharedPreferance citySharedPreferance;
    AllCity cityModel;
    Integer total_price;
    UserSharedPreference userSharedPreference;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_data);
        activityPaymentDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_payment_data);
        paymentViewModel = new PaymentViewModel(this,2);
        activityPaymentDataBinding.setPaymentviewmodel(paymentViewModel);
        paymentViewModel.get_payment_methods();
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(this);
        activityPaymentDataBinding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentViewModel.get_copon_discount(activityPaymentDataBinding.etCopon.getText().toString());
            }
        });
        activityPaymentDataBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getDataIntent();
    }

    private void validate_copon() {
    }

    private void getDataIntent() {
        citySharedPreferance = CitySharedPreferance.getInstance();
        cityModel = citySharedPreferance.Get_UserData(this);
        city_id = cityModel.getCityId();
        user_id =  getIntent().getStringExtra("id");
        user_name = getIntent().getStringExtra("name");
        user_phone = getIntent().getStringExtra("phone");
        user_city = getIntent().getStringExtra("city_id");
        user_address = getIntent().getStringExtra("address");
        total_price = getIntent().getIntExtra("totalprice",0);
        activityPaymentDataBinding.txtTotalPriceWithoutDiscount.setText(total_price+"");
        activityPaymentDataBinding.txtTotalPrice.setText(total_price+"");
        activityPaymentDataBinding.txtDiscountPrice.setText("0 ريال");

    }



    public void init_recycler(List<Payment> paymentList) {
        paymentAdapter = new PaymentAdapter(this,paymentList);
        layoutManager = new LinearLayoutManager(this);
        activityPaymentDataBinding.paymentRecycler.setHasFixedSize(true);
        activityPaymentDataBinding.paymentRecycler.setLayoutManager(layoutManager);
        activityPaymentDataBinding.paymentRecycler.setAdapter(paymentAdapter);

    }

    public void setData(Payment payment) {
        payment_id = payment.getId();
        if (payment_id.equals("1")){
            activityPaymentDataBinding.btnPay.setVisibility(View.VISIBLE);
        }else if (payment_id.equals("2")) {
            createBankTransferpopup();
            activityPaymentDataBinding.btnPay.setVisibility(View.GONE);
        }else if (payment_id.equals("3")) {
            createCreditCardpopup();
            activityPaymentDataBinding.btnPay.setVisibility(View.GONE);
        }
    }

    public void pay(View view) {
        if (user != null){
            //Toast.makeText(this, promo_code, Toast.LENGTH_SHORT).show();
            paymentViewModel.add_order(user_id,user_name,user_address,user_phone,user_city,payment_id,city_id,promo_code);
        }else {
            paymentViewModel.add_order_without_login("",user_name,user_address,user_phone,user_city,payment_id,city_id,promo_code);
        }
    }

    private void createBankTransferpopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.bank_account_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
        TextView txt_bank_name = view.findViewById(R.id.txt_bank_name);
        TextView txt_ipan = view.findViewById(R.id.txt_ipan);
        TextView txt_owner = view.findViewById(R.id.txt_owner_name);
        Button btn_pay = view.findViewById(R.id.btn_pay);
        ImageView copy_img = view.findViewById(R.id.copy_img);
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
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null){
                    paymentViewModel.add_order(user_id,user_name,user_address,user_phone,user_city,payment_id,city_id,promo_code);
                    dialog3.dismiss();
                }else {
                    paymentViewModel.add_order_without_login("",user_name,user_address,user_phone,user_city,payment_id,city_id,promo_code);
                    dialog3.dismiss();
                }
            }
        });

        if (Utilities.isNetworkAvailable(this)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<Bank> call = getDataService.get_bank_data();
            call.enqueue(new Callback<Bank>() {
                @Override
                public void onResponse(Call<Bank> call, Response<Bank> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess() ==1){
                            txt_bank_name.setText(response.body().getBankTransfer().getBankName());
                            txt_ipan.setText(response.body().getBankTransfer().getIpan());
                            txt_owner.setText(response.body().getBankTransfer().getOwner());
                            copy_img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ClipboardManager myClipboard;
                                    myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                                    ClipData myClip;
                                    String text = response.body().getBankTransfer().getIpan();
                                    myClip = ClipData.newPlainText("text", text);
                                    myClipboard.setPrimaryClip(myClip);
                                    Toast.makeText(PaymentDataActivity.this, "تم نسخ IPAN", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<Bank> call, Throwable t) {

                }
            });
        }
    }
    private void createCreditCardpopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.credit_card_dialog, null);
        ImageView cancel_img = view.findViewById(R.id.cancel_img);
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
    }

    public void setTotalPrice(String discount,String id) {
        promo_code = id;
        total_price = total_price - Integer.parseInt(discount);
        activityPaymentDataBinding.txtDiscountPrice.setText(discount+" "+"ريال");
        activityPaymentDataBinding.txtTotalPrice.setText(total_price+" "+"ريال");
        activityPaymentDataBinding.etCopon.setText("");
        activityPaymentDataBinding.etCopon.setFocusable(false);
        activityPaymentDataBinding.etCopon.setEnabled(false);
        activityPaymentDataBinding.etCopon.setCursorVisible(false);
        activityPaymentDataBinding.etCopon.setFocusableInTouchMode(false);
    }

    public void setTotalPrice2(String discount, String id) {
        promo_code = "0";
        total_price = total_price - Integer.parseInt(discount);
        activityPaymentDataBinding.txtDiscountPrice.setText(discount+" "+"ريال");
        activityPaymentDataBinding.txtTotalPrice.setText(total_price+" "+"ريال");
        activityPaymentDataBinding.etCopon.setText("");
    }
}