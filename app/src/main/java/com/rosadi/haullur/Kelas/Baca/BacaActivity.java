package com.rosadi.haullur.Kelas.Baca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rosadi.haullur.Kelas.Laporan.LaporanActivity;
import com.rosadi.haullur.List.Adapter.BacaAdapter;
import com.rosadi.haullur.List.Adapter.HaulAdapter;
import com.rosadi.haullur.List.Model.Almarhums;
import com.rosadi.haullur.List.Model.Baca;
import com.rosadi.haullur.List.Model.Haul;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BacaActivity extends AppCompatActivity {

    public String idHaulAktif;

    RecyclerView recyclerView;
    List<Baca> bacaList = new ArrayList<>();
    BacaAdapter bacaAdapter;

    TextView jumlah, jumlahBelumDibaca, jumlahSudahDibaca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baca);

        Intent i = getIntent();
        idHaulAktif = i.getStringExtra("id_haul");

        jumlah = findViewById(R.id.jumlah);
        jumlahBelumDibaca = findViewById(R.id.jumlah_belum_dibaca);
        jumlahSudahDibaca = findViewById(R.id.jumlah_sudah_dibaca);
        recyclerView = findViewById(R.id.recycler_view);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BacaActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        bacaAdapter = new BacaAdapter(BacaActivity.this, bacaList);
        recyclerView.setAdapter(bacaAdapter);

        loadRingkasan();
        loadDataHaul();
    }

    public void loadRingkasan() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    JSONObject data = result.getJSONObject(0);

                    jumlah.setText(data.getString(Konfigurasi.KEY_TOTAL) + " Keluarga");
                    jumlahBelumDibaca.setText(data.getString(Konfigurasi.KEY_JUMLAH_BELUM_DIBACA) + " Belum dibaca");
                    jumlahSudahDibaca.setText(data.getString(Konfigurasi.KEY_JUMLAH_SUDAH_DIBACA) + " Sudah dibaca");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_RINGKASAN_BACA, idHaulAktif);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void loadDataHaul() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(BacaActivity.this, "Informasi", "Memuat almarhum / almarhumah...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                bacaList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Baca baca = new Baca();
                        baca.setId(object.getString(Konfigurasi.KEY_ID));
                        baca.setIdKeluarga(object.getString(Konfigurasi.KEY_ID_KELUARGA));
                        baca.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        baca.setRt(object.getString(Konfigurasi.KEY_RT));
                        baca.setJumlahAlmarhum(object.getString(Konfigurasi.KEY_JUMLAH_ALMARHUM));
                        baca.setDibaca(object.getString(Konfigurasi.KEY_DIBACA));

                        JSONArray resultAlmarhum = object.getJSONArray(Konfigurasi.KEY_ALMARHUMS);

                        List<Almarhums> almarhumsList = new ArrayList<>();
                        for (int j = 0; j < resultAlmarhum.length(); j++) {
                            Almarhums almarhums = new Almarhums();
                            almarhums.setNomor("" + (j+1));
                            almarhums.setNama(resultAlmarhum.getString(j));
                            almarhumsList.add(almarhums);
                        }
                        baca.setAlmarhumsList(almarhumsList);

                        bacaList.add(baca);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BacaActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    bacaAdapter = new BacaAdapter(BacaActivity.this, bacaList);
                    recyclerView.setAdapter(bacaAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_BACA_LOAD_ALMARHUM, idHaulAktif);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }
}