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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import com.posdayandu.posdayandu.adapter.RecyclerViewAdapterImunisasi;
import com.posdayandu.posdayandu.helper.AppController;
import com.posdayandu.posdayandu.helper.Url;
import com.posdayandu.posdayandu.model.Imun;

public class Imunisasi extends AppCompatActivity {

    SweetAlertDialog pDialog;
    String tag_json_obj = "json_obj_req";
    private String URL1 = Url.URL + "searchimunisasi/";
    private String URL = Url.URL + "imunisasi/";
    String user;
    SharedPreferences sharedpreferences;
    public static final String TAG_ID_USER = "id";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterImunisasi adapter;
    private ArrayList<Imun> arrayImunisasi;
    EditText et_search;
    ImageButton cari;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imunisasi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        user = sharedpreferences.getString(TAG_ID_USER, "");
        recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        et_search = findViewById(R.id.et_search);
        cari = findViewById(R.id.cari);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TanggalMasuk();
            }
        });

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tanggal_masuk = et_search.getText().toString();
                MuatData1(tanggal_masuk);
            }
        });

        MuatData();

    }

    private void MuatData1(String tanggal) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Mohon Tunggu...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, URL1 + user, response -> {
            try {
                Type typeModelImunisasi = new TypeToken<ArrayList<Imun>>() {
                }.getType();
                arrayImunisasi = new Gson().fromJson(response, typeModelImunisasi);
                adapter = new RecyclerViewAdapterImunisasi(Imunisasi.this, arrayImunisasi);
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(Imunisasi.this, "Data Kosong!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            hideDialog();
        }, error -> {
            Toast.makeText(getBaseContext(), ""+ error, Toast.LENGTH_SHORT).show();
            hideDialog();
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("tanggal", tanggal);
                return MyData;
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

    private void TanggalMasuk() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_search.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
                    Type typeModelImunisasi = new TypeToken<ArrayList<Imun>>() {
                    }.getType();
                    arrayImunisasi = new Gson().fromJson(response, typeModelImunisasi);
                    adapter = new RecyclerViewAdapterImunisasi(Imunisasi.this, arrayImunisasi);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(Imunisasi.this, "Data Kosong!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(Imunisasi.this, "Network TimeoutError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Imunisasi.this, "Nerwork NoConnectionError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Imunisasi.this, "Network AuthFailureError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Imunisasi.this, "Server Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(Imunisasi.this, "Network Error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Imunisasi.this, "Parse Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Imunisasi.this, "Status Kesalahan Tidak Diketahui!", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        });
        AppController.getInstance().addToQueue(request, "data_kms");
    }
}
