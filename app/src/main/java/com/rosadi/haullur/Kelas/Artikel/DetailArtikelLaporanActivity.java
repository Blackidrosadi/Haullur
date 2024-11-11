package com.rosadi.haullur.Kelas.Artikel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailArtikelLaporanActivity extends AppCompatActivity {

    String id, fotoTamnel, judul, tanggal, lokasi, deskripsi, dilihat, idHaul, foto, foto2;

    ImageViewZoom imageViewTamnel, imageView, imageView2;
    TextView textViewJudul, textViewTanggal, textViewLokasi, textViewDilihat, textViewDeskripsi;
    RecyclerView recyclerView;
    ProgressBar progressBar, progressBar1, progressBar2;
    RelativeLayout almarhums;
    CardView cardViewImage, cardViewImage2;

    List<HaulDetail> haulDetailList = new ArrayList<>();
    HaulDetailAdapter haulDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artikel);

        Intent intent = getIntent();
        id = intent.getStringExtra(Konfigurasi.KEY_ID);
        fotoTamnel = intent.getStringExtra(Konfigurasi.KEY_FOTO_TAMNEL);
        judul = intent.getStringExtra(Konfigurasi.KEY_JUDUL);
        tanggal = intent.getStringExtra(Konfigurasi.KEY_TANGGAL);
        lokasi = intent.getStringExtra(Konfigurasi.KEY_LOKASI);
        deskripsi = intent.getStringExtra(Konfigurasi.KEY_DESKRIPSI);
        dilihat = intent.getStringExtra(Konfigurasi.KEY_DILIHAT);
        idHaul = intent.getStringExtra(Konfigurasi.KEY_ID_HAUL);
        foto = intent.getStringExtra(Konfigurasi.KEY_FOTO);
        foto2 = intent.getStringExtra(Konfigurasi.KEY_FOTO_2);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageViewTamnel = findViewById(R.id.foto_tamnel);
        progressBar = findViewById(R.id.progress_bar);
        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);
        textViewJudul = findViewById(R.id.judul);
        textViewTanggal = findViewById(R.id.tanggal);
        textViewLokasi = findViewById(R.id.lokasi);
        textViewDilihat = findViewById(R.id.dilihat);
        textViewDeskripsi = findViewById(R.id.deskripsi);
        cardViewImage = findViewById(R.id.card_image);
        cardViewImage2 = findViewById(R.id.card_image2);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        recyclerView = findViewById(R.id.recycler_view);
        almarhums = findViewById(R.id.almarhums);

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

        if (!foto.equals("")) {
            cardViewImage.setVisibility(View.VISIBLE);
            Glide.with(DetailArtikelLaporanActivity.this)
                    .load(Konfigurasi.URL + "images/artikel/" + foto)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
            cardViewImage.setVisibility(View.GONE);
        }

        if (!foto2.equals("")) {
            cardViewImage2.setVisibility(View.VISIBLE);
            Glide.with(DetailArtikelLaporanActivity.this)
                    .load(Konfigurasi.URL + "images/artikel/" + foto2)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar2.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar2.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView2);
        } else {
            cardViewImage2.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
        }

        almarhums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailArtikelLaporanActivity.this, ListAlmarhumActivity.class);
                i.putExtra("id_haul", idHaul);
                startActivity(i);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        haulDetailAdapter = new HaulDetailAdapter(this, haulDetailList, idHaul);
        recyclerView.setAdapter(haulDetailAdapter);

        loadDataPenarikanPetugas();
    }

    private void loadDataPenarikanPetugas() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                haulDetailList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        HaulDetail haulDetail = new HaulDetail();
                        haulDetail.setIdAkun(object.getString(Konfigurasi.KEY_ID_AKUN));
                        haulDetail.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        haulDetail.setSubtotal(object.getString(Konfigurasi.KEY_SUBTOTAL));
                        haulDetailList.add(haulDetail);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailArtikelLaporanActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(DetailArtikelLaporanActivity.this, R.drawable.divider_primary));
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    haulDetailAdapter = new HaulDetailAdapter(DetailArtikelLaporanActivity.this, haulDetailList, idHaul);
                    recyclerView.setAdapter(haulDetailAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_LAPORAN_DETAIL, idHaul);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }
}