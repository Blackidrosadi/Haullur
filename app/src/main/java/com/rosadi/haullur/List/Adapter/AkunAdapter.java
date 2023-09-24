package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rosadi.haullur.Kelas.Akun.DetailAkunActivity;
import com.rosadi.haullur.List.Model.Akun;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AkunAdapter extends RecyclerView.Adapter<AkunAdapter.ViewHolder> {

    Context context;
    List<Akun> akunList;

    public AkunAdapter(Context context, List<Akun> akunList) {
        this.context = context;
        this.akunList = akunList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_akun, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Akun akun = akunList.get(position);

        holder.nama.setText(akun.getNama());
        if (akun.getEmail().equals("")) {
            holder.email.setText("Email belum ditambahkan");
            holder.email.setTextColor(context.getResources().getColor(R.color.yellow));
        } else {
            holder.email.setText(akun.getEmail());
        }

        if (akun.getLevel().equals("1")) {
            holder.level.setText("Admin");
        } else {
            holder.level.setText("Petugas");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailAkunActivity.class);
                i.putExtra(Konfigurasi.KEY_ID, akun.getId());
                i.putExtra(Konfigurasi.KEY_NAMA, akun.getNama());
                i.putExtra(Konfigurasi.KEY_EMAIL, akun.getEmail());
                i.putExtra(Konfigurasi.KEY_TELEPON, akun.getTelepon());
                i.putExtra(Konfigurasi.KEY_LEVEL, akun.getSandi());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return akunList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, email, level;
        ImageView telepon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            email = itemView.findViewById(R.id.email);
            telepon = itemView.findViewById(R.id.telepon);
            level = itemView.findViewById(R.id.level);
        }
    }
}
