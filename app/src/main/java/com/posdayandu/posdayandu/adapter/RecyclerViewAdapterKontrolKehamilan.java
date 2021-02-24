package com.posdayandu.posdayandu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.posdayandu.posdayandu.R;
import com.posdayandu.posdayandu.model.KontrolHamil;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterKontrolKehamilan extends RecyclerView.Adapter<RecyclerViewAdapterKontrolKehamilan.ViewHolder> {

    private Context context;
    private ArrayList<KontrolHamil> arrayKontrolHamil;

    // membuat kontruksi RecyclerViewAdapterKontrolKehamilan
    public RecyclerViewAdapterKontrolKehamilan(Context context, ArrayList<KontrolHamil> arrayKontrolHamil) {
        this.context = context;
        this.arrayKontrolHamil = arrayKontrolHamil;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.list_kontrol_kehamilan, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // mendapatkan posisi item
        KontrolHamil KontrolHamil = arrayKontrolHamil.get(position);

        // menset data
        holder.nama_ibu.setText(KontrolHamil.getNama());
        holder.kondisi_kehamilan.setText(KontrolHamil.getKontrol_kehamilan() + " bulan");
        holder.jadwal.setText(KontrolHamil.getJadwal());
    }

    @Override
    public int getItemCount() {
        // mengembalikan data set
        return arrayKontrolHamil.size();
    }

    // membuat class viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // implementasi textview
        private TextView nama_ibu, kondisi_kehamilan, jadwal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_ibu = itemView.findViewById(R.id.nama_ibu);
            kondisi_kehamilan = itemView.findViewById(R.id.kondisi_kehamilan);
            jadwal = itemView.findViewById(R.id.tanggal);

        }
    }
}