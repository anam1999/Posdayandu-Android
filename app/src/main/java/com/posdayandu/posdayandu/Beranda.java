package com.posdayandu.posdayandu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Beranda extends AppCompatActivity {

    TextView saldo_user;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        saldo_user = findViewById(R.id.saldo_user);
        sharedpreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        saldo_user.setText(sharedpreferences.getString("saldo",null));
    }

    public void donasi(View view){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_donasi);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.findViewById(R.id.bt_potongsaldo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.findViewById(R.id.bt_metodelain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void tukarkupon(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_tukar_kupon);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.findViewById(R.id.bt_tukarkupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.findViewById(R.id.bt_batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void blog(View view){
        Bundle bundle = new Bundle();
        bundle.putString("link", "https://posdayandu.id/blog/");
        Intent intent = new Intent(Beranda.this, WebViewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void profil(View view) {
        Intent intent = new Intent(Beranda.this, Profil.class);
        startActivity(intent);
    }

    public void isisaldo(View view) {
        Intent intent = new Intent(Beranda.this, IsiSaldo.class);
        startActivity(intent);
    }

    public void kirimsaldo(View view) {
        Intent intent = new Intent(Beranda.this, KirimSaldo.class);
        startActivity(intent);
    }

    public void pesan(View view){
        Intent intent = new Intent(Beranda.this, Pesan_DaftarKader.class);
        startActivity(intent);
    }

    public void notifikasi(View view){
        Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
    }

    public void kms(View view){
        Intent intent = new Intent(Beranda.this, KMS.class);
        startActivity(intent);
    }

    public void imunisasi(View view){
        Intent intent = new Intent(Beranda.this, Imunisasi.class);
        startActivity(intent);
    }

    public void kontrolkehamilan(View view){
        Intent intent = new Intent(Beranda.this, KontrolKehamilan.class);
        startActivity(intent);
    }

    public void kb(View view){
        Toast.makeText(getApplicationContext(), "Fitur ini sedang dikembangkan, ditunggu pembaruan selanjutnya!", Toast.LENGTH_SHORT).show();
    }

    long lastPress;
    Toast backpressToast;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000){
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

    @Override
    protected void onResume() {
        super.onResume();
        sharedpreferences = getSharedPreferences("my_shared_preferences", Context.MODE_PRIVATE);
        saldo_user.setText(sharedpreferences.getString("saldo",null));
    }
}