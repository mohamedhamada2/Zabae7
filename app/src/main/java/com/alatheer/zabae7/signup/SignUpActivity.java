package com.alatheer.zabae7.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.api.GetDataService;
import com.alatheer.zabae7.api.RetrofitClientInstance;
import com.alatheer.zabae7.api.Utilities;
import com.alatheer.zabae7.databinding.ActivitySignUpBinding;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends FragmentActivity {
    ActivitySignUpBinding activitySignUpBinding;
    SignupViewModel signupViewModel;
    EditText et_name,et_phone,et_password,et_email,et_address;
    ImageView back_img;
    String user_name,phone,email,password,city_id,address;
    Spinner city_spinner;
    List<AllCity> allCities;
    List<String> allcitiestitle;
    ArrayAdapter<String> allcitiesadapter;
    Integer RESOLVE_HINT;
    GoogleApiClient apiClient;
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.ForceResendingToken forceResendingToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    Integer IMG = 5,REQUESTCAMERA =2;
    String phone_no;
    Uri filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initview();
        getCities();
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };

    }

    private void getCities() {
        allcitiestitle = new ArrayList<>();
        if (Utilities.isNetworkAvailable(SignUpActivity.this)) {
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
                        allcitiesadapter = new ArrayAdapter<String>(SignUpActivity.this, R.layout.spinner_item, allcitiestitle);
                        city_spinner.setAdapter(allcitiesadapter);
                    }
                }

                @Override
                public void onFailure(Call<CityModel> call, Throwable t) {

                }
            });
        }
    }
    private void initview() {
        activitySignUpBinding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        signupViewModel = new SignupViewModel(this);
        activitySignUpBinding.setSignupviewmodel(signupViewModel);
        et_name = activitySignUpBinding.etUserName;
        et_phone = activitySignUpBinding.etPhone;
        city_spinner = activitySignUpBinding.citySpinner;
        et_address = activitySignUpBinding.etAddress;
        et_password = activitySignUpBinding.etPassword;
        back_img = activitySignUpBinding.backimg;
        activitySignUpBinding.countryCodePicker.setCountryForPhoneCode(+966);
        activitySignUpBinding.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_ReadPermission(IMG);
            }
        });
    }

    private void Check_ReadPermission(Integer img) {
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Apply for multiple permissions together
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, img);
        } else {
            select_photo(img);
        }
    }

    private void select_photo(Integer img) {
        final CharSequence[] items = {"كاميرا", "ملفات الصور", "الغاء"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("اضافة صورة للملف الشخصي");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("كاميرا")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUESTCAMERA);
                } else if (items[which].equals("ملفات الصور")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent,"Select File"),img);
                    startActivityForResult(intent, img);

                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath = Uri.fromFile(finalfile);
                activitySignUpBinding.userImg.setImageURI(filepath);
                activitySignUpBinding.cameraImg.setVisibility(View.GONE);
            }catch (Exception e){
                filepath = data.getData();
                Picasso.get().load(filepath).into(activitySignUpBinding.userImg);
                activitySignUpBinding.cameraImg.setVisibility(View.GONE);
            }
            //filepath = data.getData();
        } else if (requestCode == REQUESTCAMERA && resultCode == Activity.RESULT_OK) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                String file_name= String.format("%d.jpg",System.currentTimeMillis());
                File finalfile = new File(path,file_name);
                FileOutputStream fileOutputStream = new FileOutputStream(finalfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                filepath = Uri.fromFile(finalfile);
                activitySignUpBinding.userImg.setImageURI(filepath);
                activitySignUpBinding.cameraImg.setVisibility(View.GONE);
            }catch (Exception e){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
                filepath = Utilities.compressImage(SignUpActivity.this, path);
                activitySignUpBinding.cameraImg.setVisibility(View.GONE);
                Picasso.get().load(filepath).into(activitySignUpBinding.userImg);
            }
        }
    }

    public void SignUp(View view) {
        Validation();
    }

    private void Validation() {
        user_name = et_name.getText().toString();
        email = "";
        phone= et_phone.getText().toString();
        password = et_password.getText().toString();
        address = et_address.getText().toString();
        //phone_no = activitySignUpBinding.countryCodePicker.getFullNumberWithPlus()+phone;
        if(!TextUtils.isEmpty(user_name)&& (phone.length()==9||phone.length()==10)&& password.length()>=6){
            if(filepath != null){
                signupViewModel.sendRegisterRequestwithImage(user_name,email,phone,password,city_id,address,filepath);
            }else {
                signupViewModel.sendRegisterRequestwithoutImage(user_name,email,phone,password,city_id,address);
            }
            //signupViewModel.signup_user(user_name,email,phone,password,city_id);
            //Log.e("error","123");
        }else {
            if (TextUtils.isEmpty(user_name)) {
                et_name.setError("ادخل اسم المستخدم");
            }else {
                et_name.setError(null);
            }
            if (phone.length() != 10) {
                et_phone.setError("رقم الهاتف غير صحيح");
            }else {
                et_phone.setError(null);
            }
            if(phone.length()!=9){
                et_phone.setError(getResources().getString(R.string.validate_email));
            }else {
                et_phone.setError(null);
            }
            if (password.length() <6) {
                et_password.setError("كلمة المرور ضعيفة");
            }else {
                et_password.setError(null);
            }
        }
    }
    // Construct a request for phone numbers and show the picker


    // Obtain the phone number from the result
}
