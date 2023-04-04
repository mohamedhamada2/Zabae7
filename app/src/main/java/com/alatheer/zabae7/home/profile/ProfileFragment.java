package com.alatheer.zabae7.home.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.data.Backpressedlistener;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.databinding.FragmentProfileBinding;
import com.alatheer.zabae7.home.basket.BasketFragment;
import com.alatheer.zabae7.home.messaging.MessagingFragment;
import com.alatheer.zabae7.home.product.OrderItemList;
import com.alatheer.zabae7.notifications.NotificationsFragment;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.signup.CityModel;
import com.alatheer.zabae7.signup.SignUpActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements Backpressedlistener{
    FragmentProfileBinding fragmentProfileBinding;
    ProfileViewModel profileViewModel;
    ArrayAdapter<String> allcitiesadapter;
    String city_id,city_id2;
    List<AllCity> allCities;
    int Img =1;
    Uri user_image;
    String user_name,email,phone,password;
    DatabaseClass databaseClass;
    List<OrderItemList> orderItemListList;
    public static Backpressedlistener backpressedlistener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false);
        View view = fragmentProfileBinding.getRoot();
        databaseClass =  Room.databaseBuilder(getActivity().getApplicationContext(), DatabaseClass.class,"my_orders").allowMainThreadQueries().build();
        orderItemListList = databaseClass.getDao().getallproducts();
        fragmentProfileBinding.txtBasketNum.setText(orderItemListList.size()+"");
        profileViewModel = new ProfileViewModel(getActivity(),this);
        fragmentProfileBinding.countryCodePicker.setCountryForPhoneCode(+966);
        fragmentProfileBinding.setProfileviewmodel(profileViewModel);
        profileViewModel.getSharedPreferanceData();
        profileViewModel.getProfileData();
        profileViewModel.getCities();
        fragmentProfileBinding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_id = allCities.get(position).getCityId();
                try {
                    TextView textView = (TextView) view;
                    textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fragmentProfileBinding.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_ReadPermission(Img);
            }
        });
        fragmentProfileBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
        fragmentProfileBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        fragmentProfileBinding.basketFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoBasket();
            }
        });
        fragmentProfileBinding.notificationFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoNotifications();
            }
        });
        return view;
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
    private void validation() {
        user_name = fragmentProfileBinding.etUserName.getText().toString();
        email = "";
        phone= fragmentProfileBinding.etPhone.getText().toString();
        password = fragmentProfileBinding.etPhone.getText().toString();
        if(!TextUtils.isEmpty(user_name)&& phone.length() == 10&& password.length()>=6){
            profileViewModel.update_user_data(user_name,email,phone,password,city_id,user_image);
        }else {
            if (TextUtils.isEmpty(user_name)) {
                fragmentProfileBinding.etUserName.setError("ادخل اسم المستخدم");
            }else {
                fragmentProfileBinding.etUserName.setError(null);
            }
            if (phone.length() != 10) {
                fragmentProfileBinding.etPhone.setError("ادخل رقم الهاتف");
            }else {
                fragmentProfileBinding.etPhone.setError(null);
            }
        }
    }

    private void Check_ReadPermission(int img) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, img);
        }else {
            select_photo(img);
        }
    }

    private void select_photo(int img) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);

        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, img);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Img && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            //militery_img2 = data.getData();
            //user_image = Utilities.compressImage(getActivity(),data.getDataString());
            //Picasso.get().load(user_image).into(fragmentProfileBinding.profileImg);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                user_image = Uri.fromFile(finalfile);
                fragmentProfileBinding.profileImg.setImageURI(user_image);
            }catch (Exception e){
                user_image = data.getData();
                Picasso.get().load(user_image).into(fragmentProfileBinding.profileImg);
            }
        }
    }

    public void setData(UserModel userModel) {
        fragmentProfileBinding.etUserName.setText(userModel.getUserName());
        fragmentProfileBinding.etPhone.setText(userModel.getUserPhone());
        Picasso.get().load("https://demo.thabaeh.com/uploads/user/"+userModel.getMImage()).into(fragmentProfileBinding.profileImg);
        city_id = userModel.getUserCity();
    }

    public void setSpinner(List<String> allcitiestitles, List<AllCity> allCities) {
        this.allCities = allCities;
        String compareValue = city_id;
        allcitiesadapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, allcitiestitles);
        fragmentProfileBinding.citySpinner.setAdapter(allcitiesadapter);
        if (compareValue != null) {
            for (AllCity allCity:allCities){
                if((allCity.getCityId()+"").equals(compareValue)){
                    int spinnerPosition = allcitiesadapter.getPosition(allCity.getCityName());
                    fragmentProfileBinding.citySpinner.setSelection(spinnerPosition);
                    //profileViewModel.getadditions(datum.getSubCategoryId());
                }
            }
        }
    }

    public void go_to_main() {
        Fragment fragment = new MainProfileFragment();
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().addToBackStack("MainProfileFragment").setCustomAnimations(R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out). //popExit)
                replace(R.id.fragmentcontainer, fragment).commit();
    }

    public void setNotificationsCount(int size) {
        if (size != 0){
            fragmentProfileBinding.txtNotificationNum.setVisibility(View.VISIBLE);
            fragmentProfileBinding.txtNotificationNum.setText(size+"");
        }else {
            fragmentProfileBinding.txtNotificationNum.setVisibility(View.GONE);
        }
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

    @Override
    public void onBackPressed() {

    }
}
