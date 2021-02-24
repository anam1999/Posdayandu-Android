package com.posdayandu.posdayandu;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.posdayandu.posdayandu.helper.AppController;
import com.posdayandu.posdayandu.helper.Url;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profil extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    String user;
    public static final String session_status = "session_status";
    public static final String TAG_ID_USER = "id";
    public static final String TAG_USER = "name";
    public static final String TAG_STATUS = "status";
    public static final String TAG_JUMLAH_ANAK = "jumlah_anak";
    public static final String TAG_NIK = "nik";
    TextView nama_ibu, jumlah_anak, status_berlangganan;
    private String url = Url.URL + "show_ibu/";
    String tag_json_obj = "json_obj_req";
    private static final String TAG = Profil.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        user = sharedpreferences.getString(TAG_ID_USER, "");
        nama_ibu = findViewById(R.id.nama_pengguna);
        jumlah_anak = findViewById(R.id.jumlah_anak);
        status_berlangganan = findViewById(R.id.status_berlangganan);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        cekProfil();

    }

    public void ubahprofil(View view) {
        Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
    }

    public void dataanak(View view) {
        Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
    }

    public void transaksi(View view) {
        Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
    }

    public void alamatpengiriman(View view) {
        Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
    }

    public void bantuan(View view) {
        Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
    }

    public void masukan(View view) {
        Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
    }

    public void tentang(View view) {
        Intent intent = new Intent(Profil.this, Tentang.class);
        startActivity(intent);
    }

    private void cekProfil() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0; i < arr.length(); i++){
                                JSONObject object = arr.getJSONObject(i);
                                nama_ibu.setText(object.getString(TAG_USER));
                                jumlah_anak.setText("Jumlah Anak : " + object.getString(TAG_JUMLAH_ANAK));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(Profil.this, "Waktu koneksi ke server habis", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Profil.this, "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Profil.this, "Network AuthFailureError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Profil.this, "Tidak diizinkan terhubung dengan server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Profil.this, "Gangguan jaringan", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Profil.this, "Parse Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Profil.this, "Status Error Tidak Diketahui!", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "MenuSiswa Error: " + error.getMessage());
                Toast.makeText(Profil.this.getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    public void logout(View view) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Logout dari aplikasi")
                .setContentText("Yakin ingin keluar dari aplikasi?")
                .setCancelText("Tidak")
                .setConfirmText("Ya")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        logout();
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Profil.session_status, false);
        editor.putString(TAG_USER, null);
        editor.putString(TAG_ID_USER, null);
        editor.commit();
        Intent ua = new Intent(Profil.this, Login.class);
        finish();
        startActivity(ua);
    }

    public void berlangganan(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_berlangganan);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.findViewById(R.id.bt_potongpulsa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("link", "http://pay.fortumo.com/mobile_payments/f48de69579c4666d32f6d410ca072c2e?key=value");
                Intent intent = new Intent(Profil.this, WebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        dialog.findViewById(R.id.bt_potongsaldo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profil.this, WebViewActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }
}