package com.posdayandu.posdayandu;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.dhairytripathi.library.EditTextPin;
import com.posdayandu.posdayandu.helper.AppController;
import com.posdayandu.posdayandu.helper.SharedPrefManager;
import com.posdayandu.posdayandu.helper.Url;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class VerifikasiOTP extends AppCompatActivity {

    public static String my_shared_preferences = "my_shared_preferences";
    SweetAlertDialog pDialog;
    public static Boolean session = false;
    public final static String TAG_ID = "id";
    public final static String TAG_NAMA_IBU = "name";
    public final static String TAG_NOHP = "nohp";
    public final static String session_status = "session_status";
    private static final String TAG = VerifikasiOTP.class.getSimpleName();
    private static String url = Url.URL + "login";
    SharedPreferences sharedpreferences;
    String no_wa;
    EditText nohp;
    EditTextPin otp;
    Button btn_VerifikasiOTP;
    ConnectivityManager conMgr;
    String tag_json_obj = "json_obj_req";
    String success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_o_t_p);

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

        nohp = findViewById(R.id.nohp);
        otp = findViewById(R.id.inputotp);
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        no_wa = getIntent().getStringExtra(TAG_NOHP);
        btn_VerifikasiOTP = findViewById(R.id.button_verify);

        nohp.setText(no_wa);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(session_status, false);
        editor.commit();

        btn_VerifikasiOTP.setOnClickListener(v -> {

            String no_hp = nohp.getText().toString();
            String kodeotp = otp.getPin();
            if (kodeotp.trim().length() > 0) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    checkOTP(no_hp, kodeotp);
                } else {
                    new SweetAlertDialog(VerifikasiOTP.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oh, tidak!")
                            .setContentText("Tidak ada koneksi internet!")
                            .show();
                }
            } else {
                new SweetAlertDialog(VerifikasiOTP.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oh, tidak!")
                        .setContentText("Kode verifikasi tidak boleh kosong, ya!")
                        .show();
            }
        });
    }

    private void checkOTP(String no_hp, String kodeotp) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Mohon Tunggu...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, url, response -> {
            Log.e(TAG, "Verifikasi OTP Respon: " + response.toString());
            hideDialog();
            try {
                JSONObject jObj = new JSONObject(response);
                success = jObj.getString("success");

                if (success.equals("1")) {
                    String id = jObj.getString("id");
                    String name = jObj.getString("name");
                    String nomorhp = jObj.getString("nohp");
                    Log.e("Verifikasi OTP Berhasil", jObj.toString());
                    Toast.makeText(getApplicationContext(), "Login Berhasil! Selamat datang, " + name, Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(session_status, true);
                    editor.putString(TAG_ID, id);
                    editor.putString(TAG_NAMA_IBU, name);
                    editor.putString(TAG_NOHP, nomorhp);
                    editor.commit();
                    SharedPrefManager.setid(getBaseContext(),getIntent().getStringExtra(TAG_ID));
                    Intent intent = new Intent(VerifikasiOTP.this, Beranda.class);
                    intent.putExtra(TAG_ID, id);
                    intent.putExtra(TAG_NAMA_IBU, name);
                    intent.putExtra(TAG_NOHP, nomorhp);
                    finish();
                    startActivity(intent);

                } else {
                    new SweetAlertDialog(VerifikasiOTP.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oh, tidak!")
                            .setContentText("Kode OTP Salah! Masukkan kode OTP dengan benar, ya.")
                            .show();
                }
            } catch (JSONException e) {
                new SweetAlertDialog(VerifikasiOTP.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oh, tidak!")
                        .setContentText("Kode OTP Salah! Masukkan kode OTP dengan benar, ya.")
                        .show();
            }
        }, error -> {
            if (error instanceof TimeoutError) {
                Toast.makeText(VerifikasiOTP.this, "Waktu koneksi ke server habis", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Toast.makeText(VerifikasiOTP.this, "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {
                Toast.makeText(VerifikasiOTP.this, "Network AuthFailureError", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(VerifikasiOTP.this, "Tidak dapat terhubung dengan server", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError) {
                Toast.makeText(VerifikasiOTP.this, "Gangguan jaringan", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Toast.makeText(VerifikasiOTP.this, "Parse Error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(VerifikasiOTP.this, "Status Error Tidak Diketahui!", Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "Verifikasi OTP Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nohp", no_hp);
                params.put("otp", kodeotp);
                params.put("fcm_token", SharedPrefManager.getFCM(getBaseContext()));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    long lastPress;
    Toast backpressToast;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000){
            backpressToast = Toast.makeText(getBaseContext(), "Tekan tombol kembali 2 kali untuk keluar", Toast.LENGTH_LONG);
            backpressToast.show();
            lastPress = currentTime;

        } else {
            if (backpressToast != null) backpressToast.cancel();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(session_status, false);
            editor.commit();
            finish();
            startActivity(intent);
            super.onBackPressed();
        }
    }
}