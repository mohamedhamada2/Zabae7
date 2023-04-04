package com.alatheer.zabae7.home.orders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.Splash.SplashActivity;
import com.alatheer.zabae7.databinding.FragmentVerificationCodeBinding;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.home.home2.HomeFragment;
import com.alatheer.zabae7.sms.OTP_Receiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static android.service.controls.ControlsProviderService.TAG;

public class VerificationCodeFragment extends Fragment {
    FragmentVerificationCodeBinding fragmentVerificationCodeBinding;
    VerificationCodeViewModel verificationCodeViewModel;
    Bundle bundle;
    String phone,phone2;
    String mVerificationId,name,city_id,address,payment_method;
    FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    HomeActivity homeActivity;
    private  static final int receive_sms = 0;
    private  static final String Sms_RECEIVED ="android.provider.Telephony.SMS_RECEIVED";
    OTP_Receiver otp_receiver = new OTP_Receiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            String[] separated = msg.split(" ");
            String code = separated[6].trim();
            for(char c : code.toCharArray()) {
                if (c == '.') {
                    code = code.substring(0, code.length() - 1);
                }
            }
            fragmentVerificationCodeBinding.pinView.setText(code);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentVerificationCodeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_verification_code, container, false);
        View view = fragmentVerificationCodeBinding.getRoot();
        verificationCodeViewModel = new VerificationCodeViewModel(getActivity(),this);
        fragmentVerificationCodeBinding.setVerificationcodeviewmodel(verificationCodeViewModel);
        homeActivity = (HomeActivity) getActivity();
        //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
       /* if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.RECEIVE_SMS)){

            }else {
                ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.RECEIVE_SMS},PackageManager.PERMISSION_GRANTED);
            }
        }*/
        bundle = this.getArguments();
        phone = bundle.getString("phone");
        phone2 = bundle.getString("phone2");
        name = bundle.getString("name");
        city_id = bundle.getString("city_id");
        address = bundle.getString("address");
        payment_method = bundle.getString("payment_method");
        mAuth = FirebaseAuth.getInstance();
        //ReadSms();
        //fragmentVerificationCodeBinding.pinView.setText(code);
        sendVerificationCodeToUser(phone2);
        fragmentVerificationCodeBinding.txtPhone.setText(phone2);
       // fragmentVerificationCodeBinding.pinView.setText("159256");
        fragmentVerificationCodeBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = fragmentVerificationCodeBinding.pinView.getText().toString();
                if (!code.isEmpty()){
                    verifycode(code);
                }else{
                    fragmentVerificationCodeBinding.pinView.setError("ادخل الكود المرسل حتي يتم تفعيل حسابك");
                }
            }
        });
        fragmentVerificationCodeBinding.backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
// SMS message.
        return  view;
    }

    private void sendVerificationCodeToUser(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        //Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            String code = credential.getSmsCode();
            Toast.makeText(getActivity(), code, Toast.LENGTH_LONG).show();
            if (code != null){
                Log.e("code",code);
                fragmentVerificationCodeBinding.pinView.setText(code);
                verifycode(code);
            }
            //Toast.makeText(getActivity(), "completed", Toast.LENGTH_SHORT).show();
            //     user action.
            //Log.d(TAG, "onVerificationCompleted:" + credential);


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.e( "onVerificationFailed", e.getMessage());
            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            fragmentVerificationCodeBinding.txt.setText(e.getMessage());

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ReadSms();
                }
            }, 8000);*/
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            //mResendToken = token;
        }
    };

   /* public void ReadSms() {

        Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://sms"), null, null,null,null);
        cursor.moveToFirst();
        String sms = cursor.getString(12);
        String[] separated = sms.split(" ");
        String code = separated[6].trim();
        for(char c : code.toCharArray()) {
            if (c == '.') {
                code = code.substring(code.length() - 1);
            }
        }
        fragmentVerificationCodeBinding.pinView.setText(code);
        //fragmentVerificationCodeBinding.pinView.setText(code);
    }*/

    private void verifycode(String code) {
        fragmentVerificationCodeBinding.pinView.setText(code);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //ReadSms();
                            FirebaseUser user = task.getResult().getUser();
                            //Toast.makeText(getActivity(), "verification success", Toast.LENGTH_SHORT).show();
                            verificationCodeViewModel.addOrder(name,phone,city_id,address,payment_method);
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void go_to_home() {
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = homeActivity.getSupportFragmentManager();
        Bundle bundle = new Bundle();
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
        params.setMargins(0, 0, 0, 150);
        fragment_container.setLayoutParams(params);
        navBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(otp_receiver,new IntentFilter(Sms_RECEIVED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(otp_receiver);
    }
}
