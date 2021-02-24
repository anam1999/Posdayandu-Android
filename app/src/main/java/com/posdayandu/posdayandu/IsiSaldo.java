package com.posdayandu.posdayandu;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.balysv.materialripple.MaterialRippleLayout;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.posdayandu.posdayandu.helper.Url;
import com.posdayandu.posdayandu.model.MerchantModel;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class IsiSaldo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static String METODE_BAYAR = "";
    String[] pembayaran = {"Gopay", "Indomaret", "Transfer Bank"};
    MerchantModel merchant = new MerchantModel();
    RequestQueue queue;
    SharedPreferences sharedpreferences;
    private Spinner mPembayaran;
    private EditText mSaldo;
    private TextView mBayar;
    private MaterialRippleLayout btn_bayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_saldo);
        sharedpreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        queue = Volley.newRequestQueue(this);
        mBayar = findViewById(R.id.total_bayar);
        mPembayaran = findViewById(R.id.spinner_pembayaran);
        mSaldo = findViewById(R.id.jumlah_saldo);
        btn_bayar = findViewById(R.id.bt_bayar);

        btn_bayar.setOnClickListener(view -> {
            GetToken();

        });
        mSaldo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mBayar.setText("Rp. " + charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayAdapter spin = new ArrayAdapter(this, android.R.layout.simple_spinner_item, pembayaran);
        spin.setDropDownViewResource(android.R.layout.simple_list_item_1);

        mPembayaran.setAdapter(spin);

        mPembayaran.setOnItemSelectedListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        METODE_BAYAR = item;
        Toast.makeText(IsiSaldo.this, item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void GetToken() {
        String url = Url.URL + "charge";
        StringRequest arrayRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                if (!response.isEmpty()) {
                    JSONObject object = new JSONObject(response);

                    merchant.setToken(object.getString("token"));
                    merchant.setUrl(object.getString("redirect_url"));
                    Log.d("MERCHANT", merchant.toString());
                    initSDK(merchant.getToken(), METODE_BAYAR);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(IsiSaldo.this.getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("fname", "Sastra");
                data.put("lname", "Jasain");
                data.put("email", "dhimaspanji66@gmail.com");
                data.put("phone", "081111111111");
                data.put("user_id", sharedpreferences.getString("id", null));
                data.put("saldo", mSaldo.getText().toString());
                Log.d("isi saldo", String.valueOf(data));
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
    }

    void initSDK(String token, String metode) {

//        getSupportActionBar().hide();
        UIKitCustomSetting uisetting = new UIKitCustomSetting();
        uisetting.setShowPaymentStatus(true);
        uisetting.setSkipCustomerDetailsPages(true);

        SdkUIFlowBuilder.init()
                .setContext(IsiSaldo.this)
                .setUIkitCustomSetting(uisetting)
                .enableLog(true)
                .setClientKey(BuildConfig.CLIENT_KEY)
                .buildSDK();
        if (metode.equals("Gopay")) {
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.GO_PAY, token);
        } else if (metode.equals("Indomaret")) {
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.INDOMARET, token);
        } else if (metode.equals("Transfer Bank")) {
            MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.BANK_TRANSFER, token);
        }


    }
}