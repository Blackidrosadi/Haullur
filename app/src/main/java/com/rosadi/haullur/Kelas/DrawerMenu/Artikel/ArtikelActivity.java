package com.rosadi.haullur.Kelas.DrawerMenu.Artikel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rosadi.haullur.List.Adapter.ArtikelAdapter;
import com.rosadi.haullur.List.Model.Artikel;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtikelActivity extends AppCompatActivity {

    RelativeLayout tambahArtikel;
    RecyclerView recyclerView;

    ArtikelAdapter artikelAdapter;
    List<Artikel> artikelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);

        tambahArtikel = findViewById(R.id.tambah);
        recyclerView = findViewById(R.id.recycler_view);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tambahArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogPilihJenis();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        artikelAdapter = new ArtikelAdapter(this, artikelList);
        recyclerView.setAdapter(artikelAdapter);

        loadArtikel();
    }

    private void openDialogPilihJenis() {
        Dialog dialog = new Dialog(ArtikelActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pilih_jenis_artikel);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.laporan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ArtikelActivity.this, TambahArtikelLaporanActivity.class));
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.artikel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ArtikelActivity.this, TambahArtikelBiasaActivity.class));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void loadArtikel() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ArtikelActivity.this, "Informasi", "Memuat data artikel", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                artikelList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Artikel artikel = new Artikel();
                        artikel.setId(object.getString(Konfigurasi.KEY_ID));
                        artikel.setFotoTamnel(object.getString(Konfigurasi.KEY_FOTO_TAMNEL));
                        artikel.setJudul(object.getString(Konfigurasi.KEY_JUDUL));
                        artikel.setTanggal(object.getString(Konfigurasi.KEY_TANGGAL));
                        artikel.setLokasi(object.getString(Konfigurasi.KEY_LOKASI));
                        artikel.setDeskripsi(object.getString(Konfigurasi.KEY_DESKRIPSI));
                        artikel.setDilihat(object.getString(Konfigurasi.KEY_DILIHAT));
                        artikel.setIdHaul(object.getString(Konfigurasi.KEY_ID_HAUL));
                        artikelList.add(artikel);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ArtikelActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    artikelAdapter = new ArtikelAdapter(ArtikelActivity.this, artikelList);
                    recyclerView.setAdapter(artikelAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_ARTIKEL);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }
}