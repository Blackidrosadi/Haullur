package com.rosadi.haullur.Kelas.Artikel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rosadi.haullur.List.Adapter.HaulDetailAdapter;
import com.rosadi.haullur.List.Model.HaulDetail;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;

import java.util.ArrayList;
import java.util.List;

public class DetailArtikelBiasaActivity extends AppCompatActivity {

    String id, fotoTamnel, judul, tanggal, lokasi, deskripsi, dilihat;

    ImageViewZoom imageViewTamnel;
    TextView textViewJudul, textViewTanggal, textViewLokasi, textViewDilihat, textViewDeskripsi;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artikel_biasa);

        Intent intent = getIntent();
        id = intent.getStringExtra(Konfigurasi.KEY_ID);
        fotoTamnel = intent.getStringExtra(Konfigurasi.KEY_FOTO_TAMNEL);
        judul = intent.getStringExtra(Konfigurasi.KEY_JUDUL);
        tanggal = intent.getStringExtra(Konfigurasi.KEY_TANGGAL);
        lokasi = intent.getStringExtra(Konfigurasi.KEY_LOKASI);
        deskripsi = intent.getStringExtra(Konfigurasi.KEY_DESKRIPSI);
        dilihat = intent.getStringExtra(Konfigurasi.KEY_DILIHAT);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageViewTamnel = findViewById(R.id.foto_tamnel);
        progressBar = findViewById(R.id.progress_bar);
        textViewJudul = findViewById(R.id.judul);
        textViewTanggal = findViewById(R.id.tanggal);
        textViewLokasi = findViewById(R.id.lokasi);
        textViewDilihat = findViewById(R.id.dilihat);
        textViewDeskripsi = findViewById(R.id.deskripsi);

        Glide.with(this)
                .load(Konfigurasi.URL + "images/thumbnail/" + fotoTamnel)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageViewTamnel);
        textViewJudul.setText(judul);
        textViewTanggal.setText(tanggal);
        textViewLokasi.setText(lokasi);
        textViewDilihat.setText(dilihat);
        textViewDeskripsi.setText(deskripsi);
    }
}