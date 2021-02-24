package com.posdayandu.posdayandu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.posdayandu.posdayandu.R;
import com.posdayandu.posdayandu.model.Imun;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterImunisasi extends RecyclerView.Adapter<RecyclerViewAdapterImunisasi.ViewHolder> {

    private Context context;
    private ArrayList<Imun> arrayImun;

    // membuat kontruksi RecyclerViewAdapterImunisasi
    public RecyclerViewAdapterImunisasi(Context context, ArrayList<Imun> arrayImun) {
        this.context = context;
        this.arrayImun = arrayImun;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.list_imunisasi, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // mendapatkan posisi item
        Imun Imun = arrayImun.get(position);

        // menset data
        holder.nama_anak.setText(Imun.getNama());
        holder.nama_vaksin.setText(Imun.getNama_vaksin());
        holder.jadwal.setText(Imun.getJadwal());
        holder.keterangan.setText(Imun.getKeterangan());
    }

    @Override
    public int getItemCount() {
        // mengembalikan data set
        return arrayImun.size();
    }

    // membuat class viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // implementasi textview
        private TextView nama_anak, nama_vaksin, jadwal, keterangan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_anak = itemView.findViewById(R.id.namaanak);
            nama_vaksin = itemView.findViewById(R.id.nama_vaksin);
            jadwal = itemView.findViewById(R.id.tanggal);
            keterangan = itemView.findViewById(R.id.keterangan);

        }
    }
}