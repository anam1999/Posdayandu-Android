package com.posdayandu.posdayandu.ui.beranda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.posdayandu.posdayandu.Login;
import com.posdayandu.posdayandu.qrcodescanner.QRCodeScannerJaripa;
import com.posdayandu.posdayandu.qrcodescanner.QRCodeScannerPosyandu;
import com.posdayandu.posdayandu.R;
import com.posdayandu.posdayandu.helper.AppController;
import com.posdayandu.posdayandu.helper.Url;
import com.posdayandu.posdayandu.utils.ViewAnimation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BerandaFragment extends Fragment {

    SharedPreferences sharedpreferences;
    String user;
    private String url = Url.URL + "kms_last/";
    private String url1 = Url.URL + "kontrolkehamilan_last/";
    public static final String TAG_ID_USER = "id";
    public static final String TAG_NAMA_ANAK = "nama";
    public static final String TAG_USIA = "usia";
    public static final String TAG_JADWAL = "jadwal";
    public static final String TAG_BB = "bb";
    public static final String TAG_NAMA_IBU = "nama";
    public static final String TAG_KONDISI_KEHAMILAN = "kondisi_kehamilan";
    public static final String TAG_JADWAL_KONTROL_KEHAMILAN = "jadwal";
    String tag_json_obj = "json_obj_req";
    private boolean rotate = false;
    private View lyt_mic;
    private View lyt_call;
    TextView id, namaibu, namaanak, usia, jadwal, bb, jadwal_kontrol_kehamilan, kondisi_kehamilan;
    private static final String TAG = BerandaFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        sharedpreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        user = sharedpreferences.getString(TAG_ID_USER, "");
        final FloatingActionButton fab_posyandu = view.findViewById(R.id.fab_mic);
        final FloatingActionButton fab_jaripa = view.findViewById(R.id.fab_call);
        final FloatingActionButton fab_add = view.findViewById(R.id.fab_add);
        lyt_mic = view.findViewById(R.id.lyt_mic);
        lyt_call = view.findViewById(R.id.lyt_call);
        ViewAnimation.initShowOut(lyt_mic);
        ViewAnimation.initShowOut(lyt_call);
        id = view.findViewById(R.id.id_ibu);
        namaanak = view.findViewById(R.id.namaanak);
        usia = view.findViewById(R.id.usia);
        jadwal = view.findViewById(R.id.jadwal);
        bb = view.findViewById(R.id.bb);
        namaibu = view.findViewById(R.id.namaibu);
        kondisi_kehamilan = view.findViewById(R.id.kondisi_kehamilan);
        jadwal_kontrol_kehamilan = view.findViewById(R.id.jadwal_kontrol_hamil);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
            }
        });

        fab_posyandu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QRCodeScannerPosyandu.class);
                startActivity(intent);
            }
        });

        fab_jaripa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QRCodeScannerJaripa.class);
                startActivity(intent);
            }
        });

        cekKMS();
        cekKontrolKehamilan();
        return view;
    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_mic);
            ViewAnimation.showIn(lyt_call);
        } else {
            ViewAnimation.showOut(lyt_mic);
            ViewAnimation.showOut(lyt_call);
        }
    }

    private void cekKMS() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0; i < arr.length(); i++){
                                JSONObject object = arr.getJSONObject(i);
                                id.setText(object.getString(TAG_ID_USER));
                                jadwal.setText(object.getString(TAG_JADWAL));
                                namaanak.setText(object.getString(TAG_NAMA_ANAK) + "\n" + object.getString(TAG_USIA) + " bln");
                                bb.setText("Berat badan " + object.getString(TAG_BB) + "kg");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "Waktu koneksi ke server habis", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(), "Network AuthFailureError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(), "Tidak diizinkan terhubung dengan server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), "Gangguan jaringan", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getActivity(), "Parse Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Status Error Tidak Diketahui!", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "Beranda Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void cekKontrolKehamilan() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1 + user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0; i < arr.length(); i++){
                                JSONObject object = arr.getJSONObject(i);
                                id.setText(object.getString(TAG_ID_USER));
                                jadwal_kontrol_kehamilan.setText(object.getString(TAG_JADWAL_KONTROL_KEHAMILAN));
                                namaibu.setText(object.getString(TAG_NAMA_IBU));
                                kondisi_kehamilan.setText("Usia Kehamilan\n" + object.getString(TAG_KONDISI_KEHAMILAN) + " bulan");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(getActivity(), "Waktu koneksi ke server habis", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(), "Network AuthFailureError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(), "Tidak diizinkan terhubung dengan server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(), "Gangguan jaringan", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getActivity(), "Parse Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Status Error Tidak Diketahui!", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "Beranda Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }
}