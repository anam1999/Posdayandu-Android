package com.posdayandu.posdayandu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.posdayandu.posdayandu.PutarVideo;
import com.posdayandu.posdayandu.R;
import com.posdayandu.posdayandu.model.Video;
import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapterVideo extends RecyclerView.Adapter<RecyclerViewAdapterVideo.ViewHolder> {

    private Context context;
    private ArrayList<Video> arrayVideo;

    // membuat kontruksi RecyclerViewAdapterVideo
    public RecyclerViewAdapterVideo(Context context, ArrayList<Video> arrayVideo) {
        this.context = context;
        this.arrayVideo = arrayVideo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.list_video, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // mendapatkan posisi item
        Video video = arrayVideo.get(position);

        Bitmap bitmap = null;
        try {
            bitmap = retrieveVideoFrameFromVideo("http://portal.posdayandu.id/uploads/video/" + video.getNama_video());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 175, 125, false);
                holder.videos.setImageBitmap(bitmap);
        }

        holder.judul.setText(video.getJudul());
        holder.deskripsi.setText(video.getDeskripsi());
        holder.kategori.setText("Akses " + video.getKategori());
        holder.pemeran.setText("Oleh " + video.getPemeran());

        holder.itemView.setOnClickListener(v -> {
            Video video1 = arrayVideo.get(holder.getAdapterPosition());
            Intent intent = new Intent(holder.itemView.getContext(), PutarVideo.class);
            intent.putExtra(PutarVideo.EXTRA_VIDEO, video1);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    public static Bitmap retrieveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    @Override
    public int getItemCount() {
        // mengembalikan data set
        return arrayVideo.size();
    }

    // membuat class viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // implementasi textview
        private TextView judul, deskripsi, kategori, pemeran;
        private ImageView videos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videos = itemView.findViewById(R.id.videos);
            judul = itemView.findViewById(R.id.judul);
            deskripsi = itemView.findViewById(R.id.deskripsi);
            kategori = itemView.findViewById(R.id.kategori);
            pemeran = itemView.findViewById(R.id.pemeran);

        }
    }
}