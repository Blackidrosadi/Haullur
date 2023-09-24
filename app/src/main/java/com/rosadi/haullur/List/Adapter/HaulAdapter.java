package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rosadi.haullur.Kelas.Laporan.LaporanDetailActivity;
import com.rosadi.haullur.List.Model.Haul;
import com.rosadi.haullur.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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
        View view = null;
        if (context.getClass().getSimpleName().equals("ProgramHaulActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_haul, parent, false);
        } else if (context.getClass().getSimpleName().equals("LaporanActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_haul_simple, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Haul haul = haulList.get(position);

        if (context.getClass().getSimpleName().equals("ProgramHaulActivity")) {
            if (haul.getStatus().equals("1")) {
                holder.status.setText("Sedang berjalan");
                holder.status.setTextColor(context.getResources().getColor(R.color.yellow));

                holder.program.setText(haul.getDeskripsi());
                holder.tanggal.setText(haul.getTanggal());
            } else {
                holder.status.setText("Selesai");
                holder.status.setTextColor(context.getResources().getColor(R.color.accent));

                holder.program.setText(haul.getDeskripsi());
                holder.tanggal.setText(haul.getTanggal());
            }
        } else if (context.getClass().getSimpleName().equals("LaporanActivity")) {
            if (haul.getStatus().equals("1")) {
                holder.status.setVisibility(View.VISIBLE);
            } else {
                holder.status.setVisibility(View.GONE);
            }

            holder.tanggal.setText(haul.getTanggal());
            holder.total.setText("Rp" + rupiahFormat(haul.getTotal()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, LaporanDetailActivity.class);
                    i.putExtra("id", haul.getId());
                    i.putExtra("tanggal", haul.getTanggal());
                    i.putExtra("total", haul.getTotal());
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return haulList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView program, tanggal, status, total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            program = itemView.findViewById(R.id.haul);
            tanggal = itemView.findViewById(R.id.tanggal);
            status = itemView.findViewById(R.id.status);
            total = itemView.findViewById(R.id.total);
        }
    }

    private String rupiahFormat(String jumlahUang) {
        Locale local = new Locale("id", "id");
        String replaceable = String.format("[Rp,.\\s]",
                NumberFormat.getCurrencyInstance()
                        .getCurrency()
                        .getSymbol(local));
        String cleanString = jumlahUang.replaceAll(replaceable,
                "");

        double parsed;
        try {
            parsed = Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            parsed = 0.00;
        }

        NumberFormat formatter = NumberFormat
                .getCurrencyInstance(local);
        formatter.setMaximumFractionDigits(0);
        formatter.setParseIntegerOnly(true);
        String formatted = formatter.format((parsed));

        String replace = String.format("[Rp\\s]",
                NumberFormat.getCurrencyInstance().getCurrency()
                        .getSymbol(local));
        String clean = formatted.replaceAll(replace, "");

        return clean;
    }
}
