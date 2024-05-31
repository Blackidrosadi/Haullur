package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Kelas.DrawerMenu.Artikel.TambahArtikelLaporanActivity;
import com.rosadi.haullur.List.Model.Haul;
import com.rosadi.haullur.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PilihHaulAdapter extends RecyclerView.Adapter<PilihHaulAdapter.ViewHolder> {

    Context context;
    List<Haul> haulList;

    public PilihHaulAdapter(Context context, List<Haul> haulList) {
        this.context = context;
        this.haulList = haulList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_pilih_haul, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Haul haul = haulList.get(position);

        holder.deskripsi.setText(haul.getDeskripsi());
        holder.tanggal.setText(haul.getTanggal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TambahArtikelLaporanActivity) context).pilihHaul.setVisibility(View.GONE);
                ((TambahArtikelLaporanActivity) context).pilihanHaul.setVisibility(View.VISIBLE);

                ((TambahArtikelLaporanActivity) context).idHaul = haul.getId();
                ((TambahArtikelLaporanActivity) context).deskripsiHaul.setText(haul.getDeskripsi());
                ((TambahArtikelLaporanActivity) context).tanggalHaul.setText(haul.getTanggal());
                ((TambahArtikelLaporanActivity) context).totalDanaHaul = haul.getTotal();

                ((TambahArtikelLaporanActivity) context).templateLaporan.setVisibility(View.VISIBLE);
                ((TambahArtikelLaporanActivity) context).artikelForm.setVisibility(View.VISIBLE);

                ((TambahArtikelLaporanActivity) context).checkBoxTemplate.setChecked(false);
                ((TambahArtikelLaporanActivity) context).judul.setText("");
                ((TambahArtikelLaporanActivity) context).lokasi.setText("");
                ((TambahArtikelLaporanActivity) context).deskripsi.setText("");

                ((TambahArtikelLaporanActivity) context).dialogPilihanHaul.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return haulList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView deskripsi, tanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deskripsi = itemView.findViewById(R.id.deskripsi);
            tanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}
