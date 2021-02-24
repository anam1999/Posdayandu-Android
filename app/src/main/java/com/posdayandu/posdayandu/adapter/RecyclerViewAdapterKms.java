package com.posdayandu.posdayandu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.posdayandu.posdayandu.R;
import com.posdayandu.posdayandu.model.Kms;
import java.util.ArrayList;

public class RecyclerViewAdapterKms extends RecyclerView.Adapter<RecyclerViewAdapterKms.ViewHolder> {

    private Context context;
    private ArrayList<Kms> arrayKms;

    // membuat kontruksi RecyclerViewAdapterKms
    public RecyclerViewAdapterKms(Context context, ArrayList<Kms> arrayKms) {
        this.context = context;
        this.arrayKms = arrayKms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.list_kms, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // mendapatkan posisi item
        Kms Kms = arrayKms.get(position);

        // menset data
        holder.nama_anak.setText(Kms.getNama() + " (" + Kms.getUsia()  + " bulan)");
        holder.tanggal.setText(Kms.getTanggal());
        holder.bb.setText(Kms.getBb() + "kg");
        holder.tinggi.setText(Kms.getTinggi() + " cm");
        holder.suhu.setText(Kms.getSuhu() + " derajat celsius");
        holder.lingkarkepala.setText(Kms.getLingkarkepala() + " cm");
        holder.jadwal.setText(Kms.getJadwal());
    }

    @Override
    public int getItemCount() {
        // mengembalikan data set
        return arrayKms.size();
    }

    // membuat class viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // implementasi textview
        private TextView nama_anak, usia, tanggal, bb, tinggi, suhu, lingkarkepala, jadwal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_anak = itemView.findViewById(R.id.namaanak);
            tanggal = itemView.findViewById(R.id.tanggal);
            bb = itemView.findViewById(R.id.bb);
            tinggi = itemView.findViewById(R.id.tinggi);
            suhu = itemView.findViewById(R.id.suhu);
            lingkarkepala = itemView.findViewById(R.id.lingkarkepala);
            jadwal = itemView.findViewById(R.id.tanggal);

        }
    }
}