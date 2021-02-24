package com.posdayandu.posdayandu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.posdayandu.posdayandu.helper.PrefManager;
import com.posdayandu.posdayandu.utils.Tools;

public class Intro extends AppCompatActivity {

    private static final int MAX_STEP = 3;
    private PrefManager prefManager;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    private String about_title_array[] = {
            "Halo, Bunda!",
            "Posyandu Gak Perlu Ribet!",
            "Ada Hadiah Dari Paijo, Nih!"
    };

    private String about_description_array[] = {
            "Saya Paijo, yang akan bimbing bunda, nih! Geser ke kiri, ya...",
            "Gak perlu bawa buku dan antri panjang, nih! Daftarnya juga tinggal scan Kode QR!",
            "Untuk Ibunda yang aktif posyandu pakai aplikasinya Paijo, ya. Yuk, klik mulai untuk buka aplikasinya Paijo..."
    };

    private int about_images_array[] = {
            R.drawable.paijo,
            R.drawable.paijojadwal,
            R.drawable.paijohadiah
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager = findViewById(R.id.view_pager);

        // adding bottom dots
        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Tools.setSystemBarColor(this, R.color.overlay_light_80);
        Tools.setSystemBarLight(this);
    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.bg_screen1), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Button btnNext;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_card_intro, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(about_title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(about_description_array[position]);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(about_images_array[position]);

            btnNext = view.findViewById(R.id.btn_next);

            if (position == about_title_array.length - 1) {
                btnNext.setText("Mulai!");
            } else {
                btnNext.setText("Selanjutnya");
            }

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current = viewPager.getCurrentItem() + 1;
                    if (current < MAX_STEP) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    } else {
                        launchHomeScreen();
                        finish();
                    }
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return about_title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(Intro.this, Login.class));
        finish();
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
}