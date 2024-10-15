package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rosadi.haullur.Kelas.Artikel.DetailArtikelActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.Artikel.EditArtikelBiasaActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.Artikel.EditArtikelLaporanActivity;
import com.rosadi.haullur.List.Model.Artikel;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ViewHolder> {

    Context context;
    List<Artikel> artikelList;

    public ArtikelAdapter(Context context, List<Artikel> artikelList) {
        this.context = context;
        this.artikelList = artikelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_artikel_beranda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Artikel artikel = artikelList.get(position);

        Glide.with(context)
                .load(Konfigurasi.URL + "images/thumbnail/" + artikel.getFotoTamnel())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.tamnel);

        holder.judul.setText(artikel.getJudul());
        holder.tanggal.setText(artikel.getTanggal());
        holder.selengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context.getClass().getSimpleName().equals("MainActivity")) {
                    if (artikel.getIdHaul().equals("0")) {
//                        Intent i = new Intent(context, DetailArtikelActivity.class);
//                        i.putExtra(Konfigurasi.KEY_ID, artikel.getId());
//                        i.putExtra(Konfigurasi.KEY_FOTO_TAMNEL, artikel.getFotoTamnel());
//                        i.putExtra(Konfigurasi.KEY_JUDUL, artikel.getJudul());
//                        i.putExtra(Konfigurasi.KEY_TANGGAL, artikel.getTanggal());
//                        i.putExtra(Konfigurasi.KEY_LOKASI, artikel.getLokasi());
//                        i.putExtra(Konfigurasi.KEY_DESKRIPSI, artikel.getDeskripsi());
//                        i.putExtra(Konfigurasi.KEY_DILIHAT, artikel.getDilihat());
//                        context.startActivity(i);
                    } else {
                        Intent i = new Intent(context, DetailArtikelActivity.class);
                        i.putExtra(Konfigurasi.KEY_ID, artikel.getId());
                        i.putExtra(Konfigurasi.KEY_FOTO_TAMNEL, artikel.getFotoTamnel());
                        i.putExtra(Konfigurasi.KEY_JUDUL, artikel.getJudul());
                        i.putExtra(Konfigurasi.KEY_TANGGAL, artikel.getTanggal());
                        i.putExtra(Konfigurasi.KEY_LOKASI, artikel.getLokasi());
                        i.putExtra(Konfigurasi.KEY_DESKRIPSI, artikel.getDeskripsi());
                        i.putExtra(Konfigurasi.KEY_DILIHAT, artikel.getDilihat());
                        i.putExtra(Konfigurasi.KEY_ID_HAUL, artikel.getIdHaul());
                        i.putExtra(Konfigurasi.KEY_FOTO, artikel.getFoto());
                        i.putExtra(Konfigurasi.KEY_FOTO_2, artikel.getFoto2());
                        context.startActivity(i);
                    }
                } else if (context.getClass().getSimpleName().equals("ArtikelActivity")) {
                    if (artikel.getIdHaul().equals("0")) {
                        Intent i = new Intent(context, EditArtikelBiasaActivity.class);
                        i.putExtra(Konfigurasi.KEY_ID, artikel.getId());
                        i.putExtra(Konfigurasi.KEY_FOTO_TAMNEL, artikel.getFotoTamnel());
                        i.putExtra(Konfigurasi.KEY_JUDUL, artikel.getJudul());
                        i.putExtra(Konfigurasi.KEY_TANGGAL, artikel.getTanggal());
                        i.putExtra(Konfigurasi.KEY_LOKASI, artikel.getLokasi());
                        i.putExtra(Konfigurasi.KEY_DESKRIPSI, artikel.getDeskripsi());
                        i.putExtra(Konfigurasi.KEY_DILIHAT, artikel.getDilihat());
                        context.startActivity(i);
                    } else {
                        Intent i = new Intent(context, EditArtikelLaporanActivity.class);
                        i.putExtra(Konfigurasi.KEY_ID, artikel.getId());
                        i.putExtra(Konfigurasi.KEY_FOTO_TAMNEL, artikel.getFotoTamnel());
                        i.putExtra(Konfigurasi.KEY_JUDUL, artikel.getJudul());
                        i.putExtra(Konfigurasi.KEY_TANGGAL, artikel.getTanggal());
                        i.putExtra(Konfigurasi.KEY_LOKASI, artikel.getLokasi());
                        i.putExtra(Konfigurasi.KEY_DESKRIPSI, artikel.getDeskripsi());
                        i.putExtra(Konfigurasi.KEY_DILIHAT, artikel.getDilihat());
                        i.putExtra(Konfigurasi.KEY_ID_HAUL, artikel.getIdHaul());
                        i.putExtra(Konfigurasi.KEY_FOTO, artikel.getFoto());
                        i.putExtra(Konfigurasi.KEY_FOTO_2, artikel.getFoto2());
                        context.startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return artikelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView tamnel;
        TextView judul, tanggal;
        RelativeLayout selengkapnya;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tamnel = itemView.findViewById(R.id.foto_tamnel);
            judul = itemView.findViewById(R.id.judul);
            tanggal = itemView.findViewById(R.id.tanggal);
            selengkapnya = itemView.findViewById(R.id.selengkapnya);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
