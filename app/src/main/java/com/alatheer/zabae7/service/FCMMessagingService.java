package com.alatheer.zabae7.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.notificationdata.NotificationsDataActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class FCMMessagingService extends FirebaseMessagingService {
    String type = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getData().get("moredata"));
        super.onMessageReceived(remoteMessage);
    }

    private void sendNotification(String title2, String messageBody, String data) {
        //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "heads up notification", importance);
            getSystemService(NotificationManager.class).createNotificationChannel(mChannel);
            Notification.Builder notification = new Notification.Builder(this,CHANNEL_ID);
            notification.setContentTitle(title2);
            notification.setContentText(messageBody);
            notification.setSmallIcon(R.drawable.logo);
            notification.setAutoCancel(true);
            try {
                JSONObject  json = new JSONObject(data);
                String type = (String) json.get("type");
                if (type.equals("order")){
                    Intent notifyIntent = new Intent(this, NotificationsDataActivity.class);
                    notifyIntent.putExtra("title",title2);
                    notifyIntent.putExtra("message",messageBody);
                    notifyIntent.putExtra("moredata",  data);
                    //notifyIntent.putExtra("flag",2);
// Set the Activity to start in a new, empty task
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
                    PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                            this, 0, notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );
                    notification.setContentIntent(notifyPendingIntent);
                }else if (type.equals("offer")){
                    Intent notifyIntent = new Intent(this, HomeActivity.class);
                    notifyIntent.putExtra("flag",2);
                    notifyIntent.putExtra("moredata",  data);
                    //notifyIntent.putExtra("flag",2);
// Set the Activity to start in a new, empty task
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
                    PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                            this, 0, notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );
                    notification.setContentIntent(notifyPendingIntent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NotificationManagerCompat.from(this).notify(1,notification.build());
        }
        /*Intent intent = new Intent("com.alatheer.noamany_FCM-MESSAGE");
        intent.putExtra("title",title2);
        intent.putExtra("message",messageBody);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);*/
    }
}
