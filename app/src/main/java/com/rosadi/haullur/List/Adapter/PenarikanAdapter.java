package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rosadi.haullur.List.Model.Penarikan;
import com.rosadi.haullur.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PenarikanAdapter extends RecyclerView.Adapter<PenarikanAdapter.ViewHolder> {

    Context context;
    List<Penarikan> penarikanList;

    public PenarikanAdapter(Context context, List<Penarikan> penarikanList) {
        this.context = context;
        this.penarikanList = penarikanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_keluarga_penarikan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Penarikan penarikan = penarikanList.get(position);

        holder.nama.setText(penarikan.getNama());
        holder.total_almarhums.setText(penarikan.getJumlahAlmarhum());

        Locale local = new Locale("id", "id");
        String replaceable = String.format("[Rp,.\\s]",
                NumberFormat.getCurrencyInstance()
                        .getCurrency()
                        .getSymbol(local));
        String cleanString = penarikan.getJumlahUang().replaceAll(replaceable, "");

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

        holder.jumlah.setText("Rp" + clean + ",-");
    }

    @Override
    public int getItemCount() {
        return penarikanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, jumlah, total_almarhums;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            jumlah = itemView.findViewById(R.id.jumlah);
            total_almarhums = itemView.findViewById(R.id.total_almarhums);
        }
    }
}
