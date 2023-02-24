package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rosadi.haullur.List.Model.Haul;
import com.rosadi.haullur.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HaulAdapter extends RecyclerView.Adapter<HaulAdapter.ViewHolder> {

    Context context;
    List<Haul> haulList;

    public HaulAdapter(Context context, List<Haul> haulList) {
        this.context = context;
        this.haulList = haulList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_haul, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Haul haul = haulList.get(position);

        if (haul.getStatus().equals("1")) {
            holder.status.setText("Sedang berjalan");
            holder.status.setTextColor(context.getResources().getColor(R.color.yellow));

            if (haul.getDeskripsi().equals("Haul Jemuah Legi")) {
                holder.program.setText(haul.getDeskripsi() + ", " + haul.getTanggal());
                holder.tanggal.setVisibility(View.GONE);
            } else {
                holder.program.setText(haul.getDeskripsi());
                holder.tanggal.setText(haul.getTanggal());
            }
        } else {
            holder.status.setText("Selesai");
            holder.status.setTextColor(context.getResources().getColor(R.color.accent));
            if (haul.getDeskripsi().equals("Haul Jemuah Legi")) {
                holder.program.setText(haul.getDeskripsi() + "; " + haul.getTanggal());
                holder.tanggal.setVisibility(View.GONE);
            } else {
                holder.program.setText(haul.getDeskripsi());
                holder.tanggal.setText(haul.getTanggal());
            }
        }
    }

    @Override
    public int getItemCount() {
        return haulList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView program, tanggal, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            program = itemView.findViewById(R.id.haul);
            tanggal = itemView.findViewById(R.id.tanggal);
            status = itemView.findViewById(R.id.status);
        }
    }
}
