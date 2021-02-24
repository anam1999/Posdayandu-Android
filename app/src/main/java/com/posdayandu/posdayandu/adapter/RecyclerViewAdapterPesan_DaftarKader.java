package com.posdayandu.posdayandu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.posdayandu.posdayandu.Pesan;
import com.posdayandu.posdayandu.R;
import com.posdayandu.posdayandu.model.DaftarKader;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterPesan_DaftarKader extends RecyclerView.Adapter<RecyclerViewAdapterPesan_DaftarKader.ViewHolder> {

    private Context context;
    private ArrayList<DaftarKader> arrayDaftarKader;

    // membuat kontruksi RecyclerViewAdapterPesan_DaftarKader
    public RecyclerViewAdapterPesan_DaftarKader(Context context, ArrayList<DaftarKader> arrayDaftarKader) {
        this.context = context;
        this.arrayDaftarKader = arrayDaftarKader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat layout inflater
        View view = LayoutInflater.from(context).inflate(R.layout.list_kader, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // mendapatkan posisi item
        DaftarKader DaftarKader = arrayDaftarKader.get(position);

        // menset data
        if(DaftarKader.getUnread().equals("0")){
            holder.unread.setVisibility(View.GONE);
        } else {
            holder.unread.setVisibility(View.VISIBLE);
            holder.unread.setText(DaftarKader.getUnread());
        }

        holder.nama_kader.setText(DaftarKader.getName());
    }

    @Override
    public int getItemCount() {
        // mengembalikan data set
        return arrayDaftarKader.size();
    }

    // membuat class viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // implementasi textview
        private TextView id_kader, nama_kader, unread;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id_kader = itemView.findViewById(R.id.id_kader);
            nama_kader = itemView.findViewById(R.id.nama_kader);
            unread = itemView.findViewById(R.id.mark);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    final DaftarKader Daftarkader = arrayDaftarKader.get(position);

                    Intent intent = new Intent(context, Pesan.class);
                    intent.putExtra("id_kader", Daftarkader.getId());
                    intent.putExtra("nama_kader", Daftarkader.getName());
                    context.startActivity(intent);
                }
            });

        }
    }
}