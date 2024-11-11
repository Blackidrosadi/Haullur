package com.rosadi.haullur.Akun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.rosadi.haullur.Kelas.DrawerMenu.Akun.DetailAkunActivity;
import com.rosadi.haullur.List.Adapter.KeluargaByAkunAdapter;
import com.rosadi.haullur.List.Model.Keluarga;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rosadi.haullur._util.Util.rupiahFormat;

public class ProfilActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String idAkun, nama, email, telepon, sandi, level;
    ArrayList<Keluarga> keluargaList = new ArrayList<>();
    KeluargaByAkunAdapter keluargaByAkunAdapter;

    TextView namaTV, emailTV, teleponTV, jumlahPenugasan, jumlahPenarikan, totalPenarikan, tidakAda;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        preferences = getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, 0);
        idAkun = preferences.getString(Konfigurasi.KEY_USER_ID_PREFERENCE, null);
        nama = preferences.getString(Konfigurasi.KEY_USER_NAMA_PREFERENCE, null);
        email = preferences.getString(Konfigurasi.KEY_USER_EMAIL_PREFERENCE, null);
        telepon = preferences.getString(Konfigurasi.KEY_USER_TELEPON_PREFERENCE, null);
        sandi = preferences.getString(Konfigurasi.KEY_USER_SANDI_PREFERENCE, null);
        level = preferences.getString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, null);

        findViewById(R.id.kembali).setOnClickListener(view -> onBackPressed());

        namaTV = findViewById(R.id.nama);
        emailTV = findViewById(R.id.email);
        teleponTV = findViewById(R.id.telepon);
        jumlahPenugasan = findViewById(R.id.jumlah_penugasan);
        jumlahPenarikan = findViewById(R.id.jumlah_penarikan);
        totalPenarikan = findViewById(R.id.total);
        tidakAda = findViewById(R.id.tidak_ada);
        recyclerView = findViewById(R.id.recycler_view);

        namaTV.setText(nama);
        if (email.equals("")) {
            emailTV.setText("Email belum ditambahkan");
        } else {
            emailTV.setText(email);
        }
        teleponTV.setText(telepon);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProfilActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        keluargaByAkunAdapter = new KeluargaByAkunAdapter(ProfilActivity.this, keluargaList);
        recyclerView.setAdapter(keluargaByAkunAdapter);

        loadPenarikanByUser();
        loadKeluargaByPetugas();
    }

    private void loadKeluargaByPetugas() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                keluargaList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Keluarga keluarga = new Keluarga();
                        keluarga.setId(object.getString(Konfigurasi.KEY_ID));
                        keluarga.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        keluarga.setRt(object.getString(Konfigurasi.KEY_RT));
                        keluarga.setTelepon(object.getString(Konfigurasi.KEY_TELEPON));
                        keluarga.setjumlahAlmarhum(object.getString(Konfigurasi.KEY_JUMLAH_ALMARHUM));
                        keluargaList.add(keluarga);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProfilActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaByAkunAdapter = new KeluargaByAkunAdapter(ProfilActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaByAkunAdapter);

                    jumlahPenugasan.setText(keluargaList.size() + " Keluarga");

                    if (keluargaList.size() == 0) {
                        tidakAda.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tidakAda.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_KELUARGA_BY_PETUGAS, idAkun);
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
    }

    private void loadPenarikanByUser() {
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

                    String jumlah = data.getString(Konfigurasi.KEY_JUMLAH);
                    String total = data.getString(Konfigurasi.KEY_TOTAL);

                    jumlahPenarikan.setText(jumlah + " Penarikan");
                    totalPenarikan.setText(rupiahFormat(total));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_PENARIKAN_AKUN, idAkun);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }
}