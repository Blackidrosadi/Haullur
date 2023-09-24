package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rosadi.haullur.Kelas.Laporan.LaporanRincianActivity;
import com.rosadi.haullur.List.Model.HaulDetail;
import com.rosadi.haullur.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HaulDetailAdapter extends RecyclerView.Adapter<HaulDetailAdapter.ViewHolder> {

    Context context;
    List<HaulDetail> haulDetailList;
    String idHaul;

    public HaulDetailAdapter(Context context, List<HaulDetail> haulDetailList, String idHaul) {
        this.context = context;
        this.haulDetailList = haulDetailList;
        this.idHaul = idHaul;
    }

    @NonNull
    @Override
    public HaulDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_haul_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HaulDetailAdapter.ViewHolder holder, int position) {
        HaulDetail haulDetail = haulDetailList.get(position);

        holder.nama.setText(haulDetail.getNama());
        holder.subtotal.setText("Rp" + rupiahFormat(haulDetail.getSubtotal()) + ",-");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LaporanRincianActivity.class);
                i.putExtra("id_haul", idHaul);
                i.putExtra("id_akun", haulDetail.getIdAkun());
                i.putExtra("nama", haulDetail.getNama());
                i.putExtra("subtotal", haulDetail.getSubtotal());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return haulDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, subtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            subtotal = itemView.findViewById(R.id.subtotal);
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
