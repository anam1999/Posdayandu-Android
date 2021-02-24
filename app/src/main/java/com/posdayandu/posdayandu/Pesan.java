package com.posdayandu.posdayandu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.posdayandu.posdayandu.adapter.AdapterChatTelegram;
import com.posdayandu.posdayandu.model.MainViewModel;
import com.posdayandu.posdayandu.model.Message;
import com.posdayandu.posdayandu.model.MessageKeluhan;
import com.posdayandu.posdayandu.utils.Tools;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Pesan extends AppCompatActivity {

    String user, ed_id_kader, ed_nama_kader;
    SharedPreferences sharedpreferences;
    public static final String TAG_ID_USER = "id";
    public static String TAG_ID_KADER = "id_kader";
    RequestQueue queue;
    private ImageView btn_send;
    private EditText et_content;
    private AdapterChatTelegram adapter;
    private RecyclerView recycler_view;
    ProgressBar loading;
    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setImageResource(R.drawable.ic_send);
            } else {
                btn_send.setImageResource(R.drawable.ic_send);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        user = sharedpreferences.getString(TAG_ID_USER, "");

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            ed_id_kader = intent.getString("id_kader");
            TAG_ID_KADER = ed_id_kader;
            ed_nama_kader = intent.getString("nama_kader");
            getSupportActionBar().setTitle(ed_nama_kader);
        }

        loading = findViewById(R.id.loading_rekap);
        showLoading(true);
        queue = Volley.newRequestQueue(this);
        iniComponent();
        showDataAll();
    }

    public void iniComponent() {
        recycler_view = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);
        adapter = new AdapterChatTelegram(this);
        recycler_view.setAdapter(adapter);
        recycler_view.scrollToPosition(adapter.getItemCount() - 1);
        adapter.insertItem(new Message(adapter.getItemCount(), "Hai.. Selamat Datang di layanan Aduan Posdayandu", false, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));

        btn_send = findViewById(R.id.btn_send);
        et_content = findViewById(R.id.text_content);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat();
            }
        });
        et_content.addTextChangedListener(contentWatcher);
    }

    private void sendChat() {
        final String msg = et_content.getText().toString();
        if (msg.isEmpty()) return;
        final String idku = user;
        final int idadm = 2;

        String url = "http://portal.posdayandu.id/api/kirimpesan/" + user;
        StringRequest arrayRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String date = getTimenya(object.getString("created_at"));
                    Toast.makeText(getApplicationContext(), date, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id_pengirim", String.valueOf(idku));
                data.put("id_penerima", String.valueOf(idadm));
                data.put("pesan", msg);
                data.put("dibaca", String.valueOf(0));
                return data;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                return params;

            }
        };
        queue.getCache().clear();
        queue.add(arrayRequest);

        adapter.insertItem(new Message(adapter.getItemCount(), msg, true, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));
        et_content.setText("");
        recycler_view.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDataAll() {
        MainViewModel mainViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainViewModel.class);
        mainViewModel.setMessagekeluhanAll(queue, getApplicationContext());
        mainViewModel.getMessagekeluhanAll().observe(this, new Observer<ArrayList<MessageKeluhan>>() {
            @Override
            public void onChanged(ArrayList<MessageKeluhan> eventSessions) {
                if (eventSessions != null) {

                    for (int i = 0; i < eventSessions.size(); i++) {
                        MessageKeluhan messageKeluhan = eventSessions.get(i);
                        if (messageKeluhan.getDari().equals(user)) {
                            adapter.insertItem(new Message(adapter.getItemCount(), messageKeluhan.getPesan(), true, adapter.getItemCount() % 5 == 0,Tools.getFormattedTimeEvent(System.currentTimeMillis())));
                            recycler_view.scrollToPosition(adapter.getItemCount() - 1);
                        } else if (messageKeluhan.getDari().equals(ed_id_kader)) {
                            adapter.insertItem(new Message(adapter.getItemCount(), messageKeluhan.getPesan(), false, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));

                        }
                        Log.d("ALL", messageKeluhan.getTanggal());

                    }
                    showLoading(false);
                }
            }
        });
    }

    public String getTimenya(String DateString) {
        String dateStr = DateString;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC+7"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    private void showLoading(Boolean state) {
        if (state) {
            loading.setVisibility(View.VISIBLE);
        } else {
            loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showDataAll();

    }
}
