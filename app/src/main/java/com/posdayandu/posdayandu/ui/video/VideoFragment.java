package com.posdayandu.posdayandu.ui.video;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.posdayandu.posdayandu.Login;
import com.posdayandu.posdayandu.R;
import com.posdayandu.posdayandu.adapter.RecyclerViewAdapterVideo;
import com.posdayandu.posdayandu.helper.AppController;
import com.posdayandu.posdayandu.helper.Url;
import com.posdayandu.posdayandu.model.Video;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoFragment extends Fragment {

    String tag_json_obj = "json_obj_req";
    public String url = Url.URL + "video/";
    public String url1 = "http://portal.posdayandu.id/api/searchvideo";
    SweetAlertDialog pDialog;
    String user;
    SharedPreferences sharedpreferences;
    public static final String TAG_ID_USER = "id";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterVideo adapter;
    private ArrayList<Video> arrayVideo;
    EditText et_search;
    ImageButton cari;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_video, container, false);

        sharedpreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        user = sharedpreferences.getString(TAG_ID_USER, "");
        recyclerView = root.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        cari = root.findViewById(R.id.cari);
        et_search = root.findViewById(R.id.et_search);

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String judul = et_search.getText().toString();
                MuatData1(judul);
            }
        });

        MuatData();

        return root;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void MuatData1(String tanggal) {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Mohon Tunggu...");
        showDialog();
        StringRequest request = new StringRequest(Request.Method.POST, url1, response -> {
            try {
                Type typeModelVideo = new TypeToken<ArrayList<Video>>() {
                }.getType();
                arrayVideo = new Gson().fromJson(response, typeModelVideo);
                adapter = new RecyclerViewAdapterVideo(getActivity(), arrayVideo);
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Data Kosong!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            hideDialog();
        }, error -> {
            Toast.makeText(getActivity(), ""+ error, Toast.LENGTH_SHORT).show();
            hideDialog();
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("judul", tanggal);
                return MyData;
            }
        };
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }

    public void MuatData() {
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Mohon Tunggu...");
        showDialog();
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                Type typeModelVideo = new TypeToken<ArrayList<Video>>() {
                }.getType();
                arrayVideo = new Gson().fromJson(response, typeModelVideo);
                adapter = new RecyclerViewAdapterVideo(getActivity(), arrayVideo);
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Data Kosong!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            hideDialog();
        }, error -> {
            if (error instanceof TimeoutError) {
                Toast.makeText(getActivity(), "Network TimeoutError", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Toast.makeText(getActivity(), "Nerwork NoConnectionError", Toast.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {
                Toast.makeText(getActivity(), "Network AuthFailureError", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Toast.makeText(getActivity(), "Parse Error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Status Kesalahan Tidak Diketahui!", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
        });
        AppController.getInstance().addToQueue(request, "data_video");
    }
}