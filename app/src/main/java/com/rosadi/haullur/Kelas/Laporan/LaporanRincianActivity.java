package com.rosadi.haullur.Kelas.Laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rosadi.haullur.List.Adapter.PenarikanAdapter;
import com.rosadi.haullur.List.Model.Penarikan;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.Util;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class LaporanRincianActivity extends AppCompatActivity {

    String idHaul, idAkun, petugas, subtotal;
    TextView petugasTV, totalDanaTV;
    RecyclerView recyclerView;

    PenarikanAdapter penarikanAdapter;
    ArrayList<Penarikan> penarikanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_rincian);

        Intent i = getIntent();
        idHaul = i.getStringExtra("id_haul");
        idAkun = i.getStringExtra("id_akun");
        petugas = i.getStringExtra("nama");
        subtotal = i.getStringExtra("subtotal");

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        petugasTV = findViewById(R.id.petugas);
        totalDanaTV = findViewById(R.id.totaldana);
        recyclerView = findViewById(R.id.recycler_view);

        petugasTV.setText(petugas);
        totalDanaTV.setText(Util.rupiahFormat(subtotal) + ",-");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LaporanRincianActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        penarikanAdapter = new PenarikanAdapter(LaporanRincianActivity.this, penarikanList);
        recyclerView.setAdapter(penarikanAdapter);

        loadPenarikanByPetugas();
    }

    public void loadPenarikanByPetugas() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                penarikanList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Penarikan penarikan = new Penarikan();
                        penarikan.setId(object.getString(Konfigurasi.KEY_ID));
                        penarikan.setIdHaul(object.getString(Konfigurasi.KEY_ID_HAUL));
                        penarikan.setIdKeluarga(object.getString(Konfigurasi.KEY_ID_KELUARGA));
                        penarikan.setJumlahUang(object.getString(Konfigurasi.KEY_JUMLAH_UANG));
                        penarikan.setDeskripsi(object.getString(Konfigurasi.KEY_DESKRIPSI));
                        penarikan.setIdAkun(object.getString(Konfigurasi.KEY_ID_AKUN));
                        penarikan.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        penarikan.setRt(object.getString(Konfigurasi.KEY_RT));
                        penarikan.setJumlahAlmarhum(object.getString(Konfigurasi.KEY_JUMLAH_ALMARHUM));
                        penarikanList.add(penarikan);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LaporanRincianActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    penarikanAdapter = new PenarikanAdapter(LaporanRincianActivity.this, penarikanList);
                    recyclerView.setAdapter(penarikanAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, idAkun);
                hashMap.put(Konfigurasi.KEY_ID_HAUL, idHaul);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_LOAD_PENARIKAN, hashMap);
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
    }
}