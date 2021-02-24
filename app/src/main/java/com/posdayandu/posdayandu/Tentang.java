package com.posdayandu.posdayandu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Tentang extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }

    public void syaratdanketentuan(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_syarat_ketentuan);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Terima kasih telah menyetujui Syarat dan Ketentuan, silahkan melanjutkan menggunakan aplikasi.", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Anda menolak menyetujui Syarat dan Ketentuan, hentikan penggunaan aplikasi jika diperlukan.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}