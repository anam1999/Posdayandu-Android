package com.posdayandu.posdayandu.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.posdayandu.posdayandu.Beranda;
import com.posdayandu.posdayandu.R;

import static android.media.RingtoneManager.getDefaultUri;

public class FirebaseCloudMessagingService extends FirebaseMessagingService {
    public String TAG = "FIREBASE MESSAGING";
SharedPreferences sharedPreferences;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sharedPreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String message = remoteMessage.getData().get("body");
            String title = remoteMessage.getData().get("title");
            String key = remoteMessage.getData().get("key_1");
            if (key.equals("sukses")) {
                String saldo = remoteMessage.getData().get("key_2");
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("saldo", saldo);
                editor.commit();
                issueNotification(message, title);
            } else {
                pendingNotification(message, title);
            }


//            startActivity(new Intent(this, Notifikasi.class));
//            showNotif(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"));

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "Token: " + s);
        super.onNewToken(s);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance) {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    void issueNotification(String message, String title) {
        int requestID = (int) System.currentTimeMillis();
        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Top Up Channel", NotificationManager.IMPORTANCE_HIGH);
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHANNEL_1");
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method
        Intent notificationIntent = new Intent(getApplicationContext(), Beranda.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Uri alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification
                .setSmallIcon(R.drawable.ic_status_success) // can use any other icon
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setSound(alarmSound)
                .setNumber(1); // this shows a number in the notification dots


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
        // it is better to not use 0 as notification id, so used 1.
    }

    void pendingNotification(String message, String title) {
        int requestID = (int) System.currentTimeMillis();
        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Top Up Channel", NotificationManager.IMPORTANCE_HIGH);
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHANNEL_1");
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method
        Intent notificationIntent = new Intent(getApplicationContext(), Beranda.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Uri alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification
                .setSmallIcon(R.drawable.ic_status_success) // can use any other icon
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setNumber(1); // this shows a number in the notification dots


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
        // it is better to not use 0 as notification id, so used 1.
    }

}