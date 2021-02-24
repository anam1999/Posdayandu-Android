package com.posdayandu.posdayandu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.posdayandu.posdayandu.helper.AppController;
import com.posdayandu.posdayandu.helper.SharedPrefManager;
import com.posdayandu.posdayandu.helper.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    public final static String TAG_ID = "id";
    public final static String TAG_NAMA_IBU = "name";
    public final static String TAG_NOHP = "nohp";
    public final static String TAG_ROLE = "role";
    public final static String TAG_SALDO = "saldo";
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    public static final String otp_shared_preferences = VerifikasiOTP.my_shared_preferences;
    public static final String session_otp_status = VerifikasiOTP.session_status;
    private static final String TAG = Login.class.getSimpleName();
    SweetAlertDialog pDialog;
    Button btn_login;
    EditText nomor_wa;
    String success;
    ConnectivityManager conMgr;
    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences, otp_preferences;
    Boolean session = false;
    String id, name, nohp, role, saldo;
    Boolean session_otp = VerifikasiOTP.session;
    long lastPress;
    Toast backpressToast;
    private String url = Url.URL + "ceknomor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        btn_login = findViewById(R.id.button_login);
        nomor_wa = findViewById(R.id.nohp);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        otp_preferences = getSharedPreferences(otp_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        session_otp = otp_preferences.getBoolean(session_otp_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        name = sharedpreferences.getString(TAG_NAMA_IBU, null);
        nohp = sharedpreferences.getString(TAG_NOHP, null);
        role = sharedpreferences.getString(TAG_ROLE, null);
        saldo = sharedpreferences.getString(TAG_SALDO, null);

        if (session_otp) {
            Intent intent = new Intent(Login.this, Beranda.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_NAMA_IBU, name);
            intent.putExtra(TAG_NOHP, nohp);
            intent.putExtra(TAG_ROLE, role);
            intent.putExtra(TAG_SALDO, saldo);
            finish();
            startActivity(intent);
        }

        btn_login.setOnClickListener(v -> {

            String no_hp = nomor_wa.getText().toString();

            if (no_hp.trim().length() > 0) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    checkLogin(no_hp);
                } else {
                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oh, tidak!")
                            .setContentText("Tidak ada koneksi internet!")
                            .show();
                }
            } else {
                new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oh, tidak!")
                        .setContentText("No. HP WhatsApp tidak boleh kosong, ya!")
                        .show();
            }
        });
    }

    private void checkLogin(final String no_wa) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Mohon Tunggu...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, url, response -> {
            Log.e(TAG, "Login Respon: " + response.toString());
            hideDialog();
            try {
                JSONArray jarray = new JSONArray(response);
                JSONObject mJsonObject = jarray.getJSONObject(1);
                success = mJsonObject.getString("nohp");

                if (success.equals(nomor_wa.getText().toString())) {
                    String id = mJsonObject.getString("id");
                    String name = mJsonObject.getString("name");
                    String nohp = mJsonObject.getString("nohp");
                    String role = mJsonObject.getString("role");
                    String saldo = mJsonObject.getString("saldo");
                    Log.e("Login Berhasil", mJsonObject.toString());

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(session_status, true);
                    editor.putString(TAG_ID, id);
                    editor.putString(TAG_NAMA_IBU, name);
                    editor.putString(TAG_NOHP, nohp);
                    editor.putString(TAG_ROLE, role);
                    editor.putString(TAG_SALDO, saldo);
                    editor.commit();

                    if (nomor_wa.getText().toString().equals(nohp)) {
                        Intent intent = new Intent(Login.this, VerifikasiOTP.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_NAMA_IBU, name);
                        intent.putExtra(TAG_NOHP, nohp);
                        intent.putExtra(TAG_ROLE, role);
                        intent.putExtra(TAG_SALDO, saldo);
                        finish();
                        startActivity(intent);
                    }
                } else {
                    new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oh, tidak!")
                            .setContentText("No. HP WhatsApp tidak terdaftar pada sistem! Yuk registrasi dulu.")
                            .show();
                }
            } catch (JSONException e) {
                new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oh, tidak!")
                        .setContentText("No. HP WhatsApp tidak terdaftar pada sistem! Yuk registrasi dulu.")
                        .show();
            }
        }, error -> {
            if (error instanceof TimeoutError) {
                Toast.makeText(Login.this, "Waktu koneksi ke server habis", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Toast.makeText(Login.this, "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {
                Toast.makeText(Login.this, "Network AuthFailureError", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(Login.this, "Tidak dapat terhubung dengan server", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError) {
                Toast.makeText(Login.this, "Gangguan jaringan", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Toast.makeText(Login.this, "Parse Error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Login.this, "Status Error Tidak Diketahui!", Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nohp", no_wa);
//                params.put("fcm_token", SharedPrefManager.getFCM(getBaseContext()));
                Log.d("params", String.valueOf(params));

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

    public void register(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    public void sk(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_syarat_ketentuan);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Terima kasih telah menyetujui Syarat dan Ketentuan, silahkan melanjutkan menggunakan aplikasi.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oh, tidak!")
                        .setContentText("Anda menolak menyetujui Syarat dan Ketentuan, sehingga Anda tidak diperkenankan menggunakan aplikasi ini!\nHentikan penggunaan aplikasi jika diperlukan.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finish();
                            }
                        })
                        .show();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}