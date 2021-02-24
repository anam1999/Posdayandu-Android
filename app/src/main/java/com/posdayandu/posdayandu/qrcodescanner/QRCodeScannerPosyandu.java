package com.posdayandu.posdayandu.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.posdayandu.posdayandu.Beranda;
import com.posdayandu.posdayandu.Login;
import com.posdayandu.posdayandu.R;
import com.posdayandu.posdayandu.helper.AppController;
import com.posdayandu.posdayandu.helper.Url;
import java.util.HashMap;
import java.util.Map;

public class QRCodeScannerPosyandu extends AppCompatActivity {

    String user;
    SharedPreferences sharedpreferences;
    public static final String TAG_ID_USER = "id";
    private static final String TAG = QRCodeScannerPosyandu.class.getSimpleName();
    private ImageView ivBgContent;
    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    private SweetAlertDialog pDialog;
    private String url = Url.URL + "scan-qr/";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code_scanner_posyandu);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ivBgContent = findViewById(R.id.ivBgContent);
        scannerView = findViewById(R.id.scannerView);
        ivBgContent.bringToFront();
        sharedpreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        user = sharedpreferences.getString(TAG_ID_USER, "");

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = result.getText();
                        showAlertDialog(message);
                    }
                });
            }
        });

        checkCameraPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCameraPermission();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void checkCameraPermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mCodeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void showAlertDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda yakin ingin mendaftar di Posyandu tersebut?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        simpanData(message);
                    }
                });

        builder.setNegativeButton(
                "Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        mCodeScanner.startPreview();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void simpanData(final String QRCode) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#DA1F3E"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Mohon Tunggu...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, url + user, response -> {
            Log.e(TAG, "QRCode Scanner Respon: " + response.toString());
            hideDialog();
            Toast.makeText(QRCodeScannerPosyandu.this, "Selamat, pendaftaran ke posyandu berhasil!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QRCodeScannerPosyandu.this, Beranda.class);
            startActivity(intent);
            finish();
        }, error -> {
            if (error instanceof TimeoutError) {
                Toast.makeText(QRCodeScannerPosyandu.this, "Waktu koneksi ke server habis", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NoConnectionError) {
                Toast.makeText(QRCodeScannerPosyandu.this, "Tidak ada jaringan", Toast.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {
                Toast.makeText(QRCodeScannerPosyandu.this, "Network AuthFailureError", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(QRCodeScannerPosyandu.this, "Tidak dapat terhubung dengan server", Toast.LENGTH_SHORT).show();
            } else if (error instanceof NetworkError) {
                Toast.makeText(QRCodeScannerPosyandu.this, "Gangguan jaringan", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                Toast.makeText(QRCodeScannerPosyandu.this, "Parse Error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(QRCodeScannerPosyandu.this, "Status Error Tidak Diketahui!", Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "QRCodeScannerPosyandu Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_posyandu", QRCode);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
}