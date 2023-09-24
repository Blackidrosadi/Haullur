package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rosadi.haullur.List.Model.Almarhums;
import com.rosadi.haullur.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BacaAlmarhumAdapter extends RecyclerView.Adapter<BacaAlmarhumAdapter.ViewHolder> {

    Context context;
    List<Almarhums> almarhumsList;

    public BacaAlmarhumAdapter(Context context, List<Almarhums> almarhumsList) {
        this.context = context;
        this.almarhumsList = almarhumsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_almarhum_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Almarhums almarhums = almarhumsList.get(position);
        holder.nomor.setText(almarhums.getNomor());
        holder.nama.setText(almarhums.getNama());
    }

    @Override
    public int getItemCount() {
        return almarhumsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nomor, nama;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nomor = itemView.findViewById(R.id.nomor);
            nama = itemView.findViewById(R.id.nama);
        }
    }
}
