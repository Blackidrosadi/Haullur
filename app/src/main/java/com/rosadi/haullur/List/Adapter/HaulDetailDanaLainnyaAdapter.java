package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rosadi.haullur.List.Model.DanaLainnya;
import com.rosadi.haullur.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HaulDetailDanaLainnyaAdapter extends RecyclerView.Adapter {

    Context context;
    List<DanaLainnya> danaLainnyaList;

    public HaulDetailDanaLainnyaAdapter(Context context, List<DanaLainnya> danaLainnyaList) {
        this.context = context;
        this.danaLainnyaList = danaLainnyaList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.list_haul_dana_lainnya_keluarga, parent, false);
            return new KeluargaViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.list_haul_dana_lainnya_sumbangan, parent, false);
            return new SumbanganViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DanaLainnya danaLainnya = danaLainnyaList.get(position);

        if (getItemViewType(position) == 1) {
            ((KeluargaViewHolder) holder).totalAlmarhum.setText(danaLainnya.getJumlahAlmarhum());
            ((KeluargaViewHolder) holder).nama.setText(danaLainnya.getNama());
            ((KeluargaViewHolder) holder).jumlah.setText("Rp" + rupiahFormat(danaLainnya.getJumlahUang()) + ",-");
        } else {
            ((SumbanganViewHolder) holder).nama.setText(danaLainnya.getDeskripsi());
            ((SumbanganViewHolder) holder).jumlah.setText("Rp" + rupiahFormat(danaLainnya.getJumlahUang()) + ",-");
        }
    }

    @Override
    public int getItemViewType(int position) {
        DanaLainnya danaLainnya = danaLainnyaList.get(position);
        if (danaLainnya.getIdKeluarga().equals("0")) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return danaLainnyaList.size();
    }

    public class KeluargaViewHolder extends RecyclerView.ViewHolder {

        TextView nama, jumlah, totalAlmarhum;

        public KeluargaViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            jumlah = itemView.findViewById(R.id.subtotal);
            totalAlmarhum = itemView.findViewById(R.id.total_almarhums);
        }
    }

    public class SumbanganViewHolder extends RecyclerView.ViewHolder {

        TextView nama, jumlah;

        public SumbanganViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            jumlah = itemView.findViewById(R.id.subtotal);
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
