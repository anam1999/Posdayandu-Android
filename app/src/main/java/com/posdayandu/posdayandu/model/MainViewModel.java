package com.posdayandu.posdayandu.model;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.posdayandu.posdayandu.Pesan;
import com.posdayandu.posdayandu.Pesan_DaftarKader;
import com.posdayandu.posdayandu.helper.SharedPrefManager;
import com.posdayandu.posdayandu.helper.Url;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MessageKeluhan>> listmessagekeluhan = new MutableLiveData<>();

    private MutableLiveData<ArrayList<MessageKeluhan>> listmessagekeluhanAll = new MutableLiveData<>();

    public MainViewModel() {
    }

    public void setMessagekeluhan(RequestQueue queue, final Context context) {
        final ArrayList<MessageKeluhan> listItemMessagekeluhan = new ArrayList<>();
        String url = Url.URL + "pesan/" + SharedPrefManager.getId(context);;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        MessageKeluhan messageKeluhan = new MessageKeluhan();
                        messageKeluhan.setId(data.getString("id"));
                        messageKeluhan.setDari(data.getString("id_pengirim"));
                        messageKeluhan.setKe(data.getString("id_penerima"));
                        messageKeluhan.setPesan(data.getString("pesan"));

                        listItemMessagekeluhan.add(messageKeluhan);
                    }
                    listmessagekeluhan.postValue(listItemMessagekeluhan);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(arrayRequest);
    }

    public void setMessagekeluhanAll(RequestQueue queue, final Context context) {
        final ArrayList<MessageKeluhan> listItemMessagekeluhanAll = new ArrayList<>();

        String url = Url.URL + "pesanallgetread/" + Pesan.TAG_ID_KADER + "/" + SharedPrefManager.getId(context);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        MessageKeluhan messageKeluhanAll = new MessageKeluhan();
                        messageKeluhanAll.setId(data.getString("id"));
                        messageKeluhanAll.setDari(data.getString("id_pengirim"));
                        messageKeluhanAll.setKe(data.getString("id_penerima"));
                        messageKeluhanAll.setPesan(data.getString("pesan"));
                        messageKeluhanAll.setTanggal((data.getString("created_at")));
                        listItemMessagekeluhanAll.add(messageKeluhanAll);
                    }
                    listmessagekeluhanAll.postValue(listItemMessagekeluhanAll);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                return data;
            }
        };
        queue.getCache().clear();
        queue.add(arrayRequest);
    }

    public LiveData<ArrayList<MessageKeluhan>> getMessagekeluhan() {
        return listmessagekeluhan;
    }

    public LiveData<ArrayList<MessageKeluhan>> getMessagekeluhanAll() {
        return listmessagekeluhanAll;
    }

}