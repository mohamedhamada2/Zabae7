package com.alatheer.zabae7.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Config;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.alatheer.zabae7.home.orders.VerificationCodeFragment;


public class OTP_Receiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    VerificationCodeFragment verificationCodeFragment;
    public static String msg;

    @Override
    public void onReceive(Context context, Intent intent) {
       if (intent.getAction()== SMS_RECEIVED){
           Bundle bundle = intent.getExtras();
           if (bundle != null){
               Object [] mypdu = (Object[]) bundle.get("pdus");
               final SmsMessage[] message= new SmsMessage[mypdu.length];
               for (int i = 0;i<mypdu.length;i++){
                   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                       String format = bundle.getString("format");
                       message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i],format);
                   }else{
                       message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                   }
                   msg = message[i].getMessageBody();
                   Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

               }
           }
       }
    }
}
