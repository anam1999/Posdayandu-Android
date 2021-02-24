package com.posdayandu.posdayandu;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.text.Html;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
import com.posdayandu.posdayandu.adapter.RecyclerViewAdapterPesan_DaftarKader;
import com.posdayandu.posdayandu.helper.AppController;
import com.posdayandu.posdayandu.helper.Url;
import com.posdayandu.posdayandu.model.DaftarKader;

public class Pesan_DaftarKader extends AppCompatActivity {

    SweetAlertDialog pDialog;
    String tag_json_obj = "json_obj_req";
    private String URL = Url.URL + "pesanallkader/";
    String user;
    SharedPreferences sharedpreferences;
    public static final String TAG_ID_USER = "id";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterPesan_DaftarKader adapter;
    private ArrayList<DaftarKader> arrayPesan_DaftarKader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_list_kader);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        user = sharedpreferences.getString(TAG_ID_USER, "");
        recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        MuatData();

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void MuatData() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Mohon Tunggu...");
        showDialog();
        String url = URL + user;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Type typeModelPesan_DaftarKader = new TypeToken<ArrayList<DaftarKader>>() {
                    }.getType();
                    arrayPesan_DaftarKader = new Gson().fromJson(response, typeModelPesan_DaftarKader);
                    adapter = new RecyclerViewAdapterPesan_DaftarKader(Pesan_DaftarKader.this, arrayPesan_DaftarKader);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(Pesan_DaftarKader.this, "Data Kosong!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(Pesan_DaftarKader.this, "Network TimeoutError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Pesan_DaftarKader.this, "Nerwork NoConnectionError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Pesan_DaftarKader.this, "Network AuthFailureError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Pesan_DaftarKader.this, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Pesan_DaftarKader.this, "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Pesan_DaftarKader.this, "Parse Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Pesan_DaftarKader.this, "Status Kesalahan Tidak Diketahui!", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        });
        AppController.getInstance().addToQueue(request, "data_kms");
    }
}
