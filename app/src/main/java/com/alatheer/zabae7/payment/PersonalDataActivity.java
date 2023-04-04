package com.alatheer.zabae7.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.CitySharedPreferance;
import com.alatheer.zabae7.data.UserSharedPreference;
import com.alatheer.zabae7.databinding.ActivityPersonalDataBinding;
import com.alatheer.zabae7.login.User;
import com.alatheer.zabae7.signup.AllCity;

import java.util.List;

public class PersonalDataActivity extends AppCompatActivity {
    ActivityPersonalDataBinding activityPersonalDataBinding;
    PaymentViewModel paymentViewModel;
    UserSharedPreference userSharedPreference;
    User user;
    String user_name,user_phone,user_address,user_city,user_id,city_id;
    List<AllCity> allCities;
    ArrayAdapter<String> allcitiesadapter;
    CitySharedPreferance citySharedPreferance;
    String promo_code = "";
    Integer totalprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        activityPersonalDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_personal_data);
        paymentViewModel = new PaymentViewModel(this,1);
        activityPersonalDataBinding.setPersonalviewmodel(paymentViewModel);
        get_user_data();
        getDataIntent();
        activityPersonalDataBinding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        activityPersonalDataBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getDataIntent() {
        totalprice = getIntent().getIntExtra("totalprice",0);
    }

    private void validation() {
        user_name = activityPersonalDataBinding.etUserName.getText().toString();
        user_phone = activityPersonalDataBinding.etPhone.getText().toString();
        user_address = activityPersonalDataBinding.etAddress.getText().toString();
        if (!TextUtils.isEmpty(user_name)&&!TextUtils.isEmpty(user_address)&&(user_phone.length()==9||user_phone.length()==10)){
            go_to_payment(user_id,user_name,user_phone,user_city,user_address);
        }else {
            if (TextUtils.isEmpty(user_name)){
                activityPersonalDataBinding.etUserName.setError("أدخل الإسم من فضلك");
            }else {
                activityPersonalDataBinding.etUserName.setError(null);
            }
            if (TextUtils.isEmpty(user_address)){
                activityPersonalDataBinding.etAddress.setError("أدخل العنوان من فضلك");
            }else {
                activityPersonalDataBinding.etAddress.setError(null);
            }
            if (user_phone.length() != 10) {
                activityPersonalDataBinding.etPhone.setError("رقم الهاتف غير صحيح");
            }else {
                activityPersonalDataBinding.etPhone.setError(null);
            }
            if(user_phone.length()!=9){
                activityPersonalDataBinding.etPhone.setError(getResources().getString(R.string.validate_email));
            }else {
                activityPersonalDataBinding.etPhone.setError(null);
            }
        }
    }

    private void go_to_payment(String user_id,String user_name, String user_phone,String user_city,String user_address) {
        Intent intent = new Intent(PersonalDataActivity.this,PaymentDataActivity.class);
        intent.putExtra("name",user_name);
        intent.putExtra("id",user_id);
        intent.putExtra("phone",user_phone);
        intent.putExtra("city_id",user_city);
        intent.putExtra("address",user_address);
        intent.putExtra("totalprice",totalprice);
        startActivity(intent);
    }

    private void get_user_data() {
        citySharedPreferance = CitySharedPreferance.getInstance();
        city_id = citySharedPreferance.Get_UserData(this).getCityId();
        userSharedPreference = UserSharedPreference.getInstance();
        user = userSharedPreference.Get_UserData(this);
        if (user != null){
            user_id = user.getMember().getUserId();
            user_name = user.getMember().getUserName();
            user_city = user.getMember().getUserCity();
            user_phone = user.getMember().getUserPhone();
            activityPersonalDataBinding.etUserName.setText(user_name);
            activityPersonalDataBinding.etPhone.setText(user_phone);
        }else {
            user_id ="";
            activityPersonalDataBinding.etPhone.setEnabled(true);
            activityPersonalDataBinding.etPhone.setFocusable(true);
            activityPersonalDataBinding.etPhone.setFocusableInTouchMode(true);
            activityPersonalDataBinding.etPhone.setCursorVisible(true);
            activityPersonalDataBinding.etPhone.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
        activityPersonalDataBinding.citySpinner.setEnabled(false);
        paymentViewModel.getCities();
    }


    public void setSpinner(List<String> allcitiestitles, List<AllCity> allCities) {
        this.allCities = allCities;
        String compareValue = city_id;
        allcitiesadapter = new ArrayAdapter<String>(this, R.layout.spinner_item, allcitiestitles);
        activityPersonalDataBinding.citySpinner.setAdapter(allcitiesadapter);
        if (compareValue != null) {
            for (AllCity allCity:allCities){
                if((allCity.getCityId()+"").equals(compareValue)){
                    int spinnerPosition = allcitiesadapter.getPosition(allCity.getCityName());
                    activityPersonalDataBinding.citySpinner.setSelection(spinnerPosition);
                    //profileViewModel.getadditions(datum.getSubCategoryId());
                }
            }
        }
    }
}