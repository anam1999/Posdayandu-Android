package com.posdayandu.posdayandu;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;
import com.posdayandu.posdayandu.model.Video;

public class PutarVideo extends AppCompatActivity {

    public final static String EXTRA_VIDEO = "extra";
    EditText judul, deskripsi, kategori, pemeran;
    VideoView videos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        setContentView(R.layout.activity_putar_video);

        judul = findViewById(R.id.juduls);
        deskripsi = findViewById(R.id.deskripsis);
        kategori = findViewById(R.id.kategoris);
        pemeran = findViewById(R.id.pemerans);
        videos = findViewById(R.id.detailvideo);

        final Video video = getIntent().getParcelableExtra(EXTRA_VIDEO);
        if (video != null) {

            judul.setText(video.getJudul());
            deskripsi.setText(video.getDeskripsi());
            kategori.setText(video.getKategori());
            pemeran.setText(video.getPemeran());
            videos.setVideoURI(Uri.parse("http://portal.posdayandu.id/uploads/video/" + video.getNama_video()));
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videos);
            videos.setMediaController(mediaController);
            videos.start();

        }
    }
}
