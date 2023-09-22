package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rosadi.haullur.List.Model.Pengeluaran;
import com.rosadi.haullur.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PengeluaranAdapter extends RecyclerView.Adapter<PengeluaranAdapter.ViewHolder> {

    Context context;
    List<Pengeluaran> pengeluaranList;

    public PengeluaranAdapter(Context context, List<Pengeluaran> pengeluaranList) {
        this.context = context;
        this.pengeluaranList = pengeluaranList;
    }

    @NonNull
    @Override
    public PengeluaranAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_pengeluaran_dana, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengeluaranAdapter.ViewHolder holder, int position) {
        Pengeluaran pengeluaran = pengeluaranList.get(position);

        holder.deskripsi.setText(pengeluaran.getDeskripsi());
        holder.jumlah.setText("Rp" + rupiahFormat(pengeluaran.getJumlahUang()) + ",-");
    }

    @Override
    public int getItemCount() {
        return pengeluaranList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView deskripsi, jumlah;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deskripsi = itemView.findViewById(R.id.deskripsi);
            jumlah = itemView.findViewById(R.id.jumlah);
        }
    }

    public String rupiahFormat(String jumlah) {
        Locale local = new Locale("id", "id");
        String replaceable = String.format("[Rp,.\\s]",
                NumberFormat.getCurrencyInstance()
                        .getCurrency()
                        .getSymbol(local));
        String cleanString = jumlah.replaceAll(replaceable, "");

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
