package com.alatheer.zabae7.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.databinding.ActivityVerificationCodeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.service.controls.ControlsProviderService.TAG;

public class VerificationCodeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ActivityVerificationCodeBinding activityVerificationCodeBinding;
    VerificationCodeViewModel verificationCodeViewModel;
    String name,email,phone,password,city_id,address,mVerificationId;
    Uri filepath;
    Integer flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        activityVerificationCodeBinding = DataBindingUtil.setContentView(this,R.layout.activity_verification_code);
        verificationCodeViewModel = new VerificationCodeViewModel(this);
        mAuth = FirebaseAuth.getInstance();
        activityVerificationCodeBinding.setVerificationcodeviewmodel(verificationCodeViewModel);
        getDataIntent();
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Toast.makeText(VerificationCodeActivity.this, "vertify", Toast.LENGTH_SHORT).show();
            Log.d(ContentValues.TAG, "onVerificationCompleted:" + credential);

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            //Toast.makeText(VerificationCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.w(ContentValues.TAG, "onVerificationFailed", e);

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
            Toast.makeText(VerificationCodeActivity.this, "send", Toast.LENGTH_SHORT).show();
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(ContentValues.TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
        }
    };

    public void sendVerificationCodeToUser(String phone_no) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone_no)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

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
        activityVerificationCodeBinding.etCode.setText(code);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerificationCodeActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithCredential:success");
                            //ReadSms();
                            FirebaseUser user = task.getResult().getUser();
                            if (flag == 1){
                                verificationCodeViewModel.sendRegisterRequestwithImage(name,email,phone, password, filepath,city_id,address);
                            }else if (flag == 2){
                                verificationCodeViewModel.sendRegisterRequestwithoutImage(name,email,phone, password,city_id,address);
                            }else if (flag == 3){
                                /*Intent intent = new Intent(VerificationCodeActivity.this, NewPasswordActivity.class);
                                intent.putExtra("phone",phone);
                                startActivity(intent);*/
                            }
                            //Toast.makeText(getActivity(), "verification success", Toast.LENGTH_SHORT).show();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(ContentValues.TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerificationCodeActivity.this, "الكود الذي قمت بإدخاله غير صالح", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void getDataIntent() {
        flag = getIntent().getIntExtra("flag", 0);
        if (flag == 1) {
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
            password = getIntent().getStringExtra("password");
            city_id = getIntent().getStringExtra("city_id");
            address = getIntent().getStringExtra("address");
            filepath = Uri.parse(getIntent().getStringExtra("filepath"));
            sendVerificationCodeToUser("+2" + phone);
            //verificationCodeViewModel.check_phone(phone);
        } else if (flag == 2) {
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
            password = getIntent().getStringExtra("password");
            city_id = getIntent().getStringExtra("city_id");
            address = getIntent().getStringExtra("address");
            sendVerificationCodeToUser("+2" + phone);
        } else if (flag == 3) {
            phone = getIntent().getStringExtra("phone");
            //activityVerificationCodeBinding.txtPhone.setText("+2" + phone);
            //verificationCodeViewModel.check_phone(phone);
            sendVerificationCodeToUser("+2" + phone);

        }
        activityVerificationCodeBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = activityVerificationCodeBinding.etCode.getText().toString();
                if (!code.isEmpty()) {
                    verifycode(code);
                } else {
                    if (flag == 1 || flag == 2) {
                        activityVerificationCodeBinding.etCode.setError("ادخل الكود المرسل حتي يتم تفعيل حسابك");
                    } else {
                        activityVerificationCodeBinding.etCode.setError("ادخل الكود المرسل حتي يتم تعديل كلمة المرور");
                    }
                }
            }
        });
    }
}
