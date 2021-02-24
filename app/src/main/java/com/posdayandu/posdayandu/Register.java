package com.posdayandu.posdayandu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.posdayandu.posdayandu.helper.AppController;
import com.posdayandu.posdayandu.helper.SharedPrefManager;
import com.posdayandu.posdayandu.helper.Url;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity {

    private static final String TAG = Register.class.getSimpleName();
    ConnectivityManager conMgr;
    SweetAlertDialog pDialog;
    Button button_register;
    EditText nomor_wa, nama_ibu;
    String success;
    String tag_json_obj = "json_obj_req";
    long lastPress;
    Toast backpressToast;

    private String url = Url.URL + "register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oh, tidak!")
                        .setContentText("Tidak ada koneksi internet!")
                        .show();
            }
        }

        button_register = findViewById(R.id.button_register);
        nama_ibu = findViewById(R.id.namaibu);
        nomor_wa = findViewById(R.id.nohp);

        button_register.setOnClickListener(v -> {
            String namaibu = nama_ibu.getText().toString();
            String nomorwa = nomor_wa.getText().toString();

            if (namaibu.trim().length() > 0 && nomorwa.trim().length() > 0) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    registrasi(namaibu, nomorwa);
                } else {
                    new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oh, tidak!")
                            .setContentText("Tidak ada koneksi internet!")
                            .show();
                }
            } else {
                new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oh, tidak!")
                        .setContentText("Kolom isiannya tidak boleh ada yang kosong, ya!")
                        .show();
            }
        });
    }

    private void registrasi(String namaibu, String nomorwa) {

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Mohon Tunggu...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, url, response -> {

            if (response.equals("Nomor WA Sudah Digunakan")) {
                new SweetAlertDialog(Register.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oh, tidak!")
                        .setContentText("Nomor WhatsApp ini sudah ada dalam sistem! Coba masukkan nomor lain, ya!")
                        .show();
            } else {
                new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Mantap!")
                        .setContentText("Anda berhasil melakukan registrasi! Yuk, masuk ke aplikasi sekarang!")
                        .setConfirmText("Masuk ke Aplikasi")
                        .setConfirmClickListener(sDialog -> {
                            Intent a = new Intent(Register.this, Login.class);
                            startActivity(a);
                        })
                        .show();
            }
            hideDialog();

        }, error -> {
            Toast.makeText(getBaseContext(), "" + error, Toast.LENGTH_SHORT).show();
            hideDialog();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("name", namaibu);
                MyData.put("nohp", nomorwa);
                MyData.put("fcm_token", SharedPrefManager.getFCM(getBaseContext()));
                Log.d("TAG", String.valueOf(MyData));
                return MyData;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPress > 5000) {
            backpressToast = Toast.makeText(getBaseContext(), "Tekan tombol kembali 2 kali untuk keluar", Toast.LENGTH_LONG);
            backpressToast.show();
            lastPress = currentTime;

        } else {
            if (backpressToast != null) backpressToast.cancel();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
            super.onBackPressed();
        }
    }

    public void kembalilogin(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}