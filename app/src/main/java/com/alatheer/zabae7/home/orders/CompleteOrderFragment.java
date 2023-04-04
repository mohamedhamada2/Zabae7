package com.alatheer.zabae7.home.orders;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.databinding.FragmentCompleteOrderBinding;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.home2.HomeFragment;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.signup.CityModel;
import com.alatheer.zabae7.signup.SignUpActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class CompleteOrderFragment extends Fragment {
    FragmentCompleteOrderBinding fragmentCompleteOrderBinding;
    CompleteOrderViewModel completeOrderViewModel;
    String name,address,phone,city_id,phone_no;
    List<AllCity> allCities;
    ArrayAdapter<String> allcitiesadapter;
    List<String> allcitiestitle;
    FirebaseAuth firebaseAuth;
    String code_sent;
    HomeActivity homeActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentCompleteOrderBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_complete_order, container, false);
        completeOrderViewModel = new CompleteOrderViewModel(getActivity(),this);
        fragmentCompleteOrderBinding.setCompleteorderviewmodel(completeOrderViewModel);
        View view = fragmentCompleteOrderBinding.getRoot();
        homeActivity=(HomeActivity) getActivity();
        fragmentCompleteOrderBinding.countryCodePicker.setCountryForPhoneCode(+966);
        getCities();
        fragmentCompleteOrderBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        fragmentCompleteOrderBinding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_id = allCities.get(position).getCityId();
                try {
                    TextView textView = (TextView) view;
                    textView.setTextColor(getResources().getColor(R.color.lightsilver));

                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fragmentCompleteOrderBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();
            }
        });
        return view;
    }

    private void getCities() {
        allcitiestitle = new ArrayList<>();
        if (Utilities.isNetworkAvailable(homeActivity)) {
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<CityModel> call = getDataService.getCities();
            call.enqueue(new Callback<CityModel>() {
                @Override
                public void onResponse(Call<CityModel> call, Response<CityModel> response) {
                    if (response.isSuccessful()) {
                        allCities = response.body().getAllCities();
                        for (AllCity allCity : allCities) {
                            allcitiestitle.add(allCity.getCityName());
                        }
                        allcitiesadapter = new ArrayAdapter<String>(homeActivity, R.layout.spinner_item, allcitiestitle);
                        fragmentCompleteOrderBinding.citySpinner.setAdapter(allcitiesadapter);
                    }
                }

                @Override
                public void onFailure(Call<CityModel> call, Throwable t) {

                }
            });
        }
    }
    private void Validation() {
        name = fragmentCompleteOrderBinding.etUserName.getText().toString();
        address = "";
        phone= fragmentCompleteOrderBinding.etPhone.getText().toString().trim();
        phone_no = "+"+fragmentCompleteOrderBinding.countryCodePicker.getFullNumber()+phone;

        //password = et_password.getText().toString();
        if(!TextUtils.isEmpty(name)&& phone.length() == 10){
            completeOrderViewModel.CreateDialog(name,address,phone,city_id,phone_no);
            //completeOrderViewModel.add_order(name,address,phone,city_id);
        }else {
            if (TextUtils.isEmpty(name)) {
                fragmentCompleteOrderBinding.etUserName.setError("ادخل اسم المستخدم");
            }else {
                fragmentCompleteOrderBinding.etUserName.setError(null);
            }
            if (phone.length() != 10) {
                fragmentCompleteOrderBinding.etPhone.setError("رقم الهاتف غير صحيح");
            }else {
                fragmentCompleteOrderBinding.etPhone.setError(null);
            }
        }
    }

    public void go_to_home() {
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("city_id",city_id);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().addToBackStack("HomeFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
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

    public void go_to_verification_code(String phone) {
        Fragment fragment = new VerificationCodeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("phone",phone_no);
        bundle.putString("phone2",phone);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().addToBackStack("VerificationCodeFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
    }
}
