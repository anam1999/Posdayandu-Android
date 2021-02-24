package com.posdayandu.posdayandu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.posdayandu.posdayandu.helper.SharedPrefManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashScreen extends AppCompatActivity {

    private int timelapse = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getFCMToken();
        if (!isConnected(SplashScreen.this)) buildDialog(SplashScreen.this);
        else {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            thread.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent home = new Intent(SplashScreen.this, Intro.class);
                    startActivity(home);
                    finish();
                }
            }, timelapse);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_splash_screen);

    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    public void buildDialog(SplashScreen v) {
        new SweetAlertDialog(SplashScreen.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Tidak ada koneksi internet!")
                .setContentText("Anda membutuhkan koneksi internet untuk dapat mengakses aplikasi ini. \n Klik OK untuk keluar.")
                .setConfirmText("OK")
                .setConfirmClickListener(sDialog -> finish())
                .show();
    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("Token", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String TOKEN = task.getResult();
                    SharedPrefManager.setFCM(getBaseContext(), TOKEN);
                    // Log and toast

                    Log.d("Token", SharedPrefManager.getFCM(getBaseContext()));

                });

    }
}