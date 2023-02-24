package com.rosadi.haullur.Penarikan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Akun.DetailAkunActivity;
import com.rosadi.haullur.Keluarga.DataKeluargaActivity;
import com.rosadi.haullur.List.Adapter.KeluargaAdapter;
import com.rosadi.haullur.List.Adapter.KeluargaByAkunAdapter;
import com.rosadi.haullur.List.Adapter.PenarikanAdapter;
import com.rosadi.haullur.List.Model.Keluarga;
import com.rosadi.haullur.List.Model.Penarikan;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PenarikanActivity extends AppCompatActivity {

    SharedPreferences preferences;
    public String idHaul, idAkun;

    KeluargaByAkunAdapter keluargaByAkunAdapter;
    ArrayList<Keluarga> keluargaList = new ArrayList<>();
    PenarikanAdapter penarikanAdapter;
    ArrayList<Penarikan> penarikanList = new ArrayList<>();

    TextView deskripsiTV, tanggalTV;
    RecyclerView recyclerView;

    public Dialog dialogTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penarikan);

        preferences = getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, 0);
        idAkun = preferences.getString(Konfigurasi.KEY_USER_ID_PREFERENCE, null);

        TextView nama = findViewById(R.id.petugas);
        nama.setText(preferences.getString(Konfigurasi.KEY_USER_NAMA_PREFERENCE, null));
        deskripsiTV = findViewById(R.id.deskripsi);
        tanggalTV = findViewById(R.id.tanggal);
        recyclerView = findViewById(R.id.recycler_view);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.totaldana).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.tambah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogTransaksi();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PenarikanActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        penarikanAdapter = new PenarikanAdapter(PenarikanActivity.this, penarikanList);
        recyclerView.setAdapter(penarikanAdapter);

        loadData();
    }

    private void openDialogTransaksi() {
        dialogTambah = new Dialog(PenarikanActivity.this);
        dialogTambah.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTambah.setContentView(R.layout.dialog_tambah_penarikan);
        dialogTambah.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogTambah.setCancelable(false);

        RecyclerView recyclerView = dialogTambah.findViewById(R.id.recycler_view);
        EditText cari = dialogTambah.findViewById(R.id.cari);

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                cariKeluargaFree(recyclerView, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PenarikanActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        keluargaByAkunAdapter = new KeluargaByAkunAdapter(PenarikanActivity.this, keluargaList);
        recyclerView.setAdapter(keluargaByAkunAdapter);

        loadKeluargaPenarikan(recyclerView);

        dialogTambah.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTambah.dismiss();
            }
        });

        dialogTambah.show();
    }

    private void loadKeluargaPenarikan(RecyclerView recyclerView) {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(PenarikanActivity.this, "Informasi", "Memuat data keluarga...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
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
                        keluargaList.add(keluarga);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PenarikanActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaByAkunAdapter = new KeluargaByAkunAdapter(PenarikanActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaByAkunAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_KELUARGA_BY_PETUGAS, preferences.getString(Konfigurasi.KEY_USER_ID_PREFERENCE, null));
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
    }

    private void loadData() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(PenarikanActivity.this, "Informasi", "Memuat data haul...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    JSONObject data = result.getJSONObject(0);

                    idHaul = data.getString(Konfigurasi.KEY_ID);
                    String deskripsi = data.getString(Konfigurasi.KEY_DESKRIPSI);
                    String tanggal = data.getString(Konfigurasi.KEY_TANGGAL);

                    deskripsiTV.setText(deskripsi);
                    tanggalTV.setText(tanggal);

                    loadPenarikanByPetugas();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_PROGRAM_AKTIF_HAUL);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
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
                        penarikan.setJumlah(object.getString(Konfigurasi.KEY_JUMLAH));
                        penarikan.setDeskripsi(object.getString(Konfigurasi.KEY_DESKRIPSI));
                        penarikan.setIdAkun(object.getString(Konfigurasi.KEY_ID_AKUN));
                        penarikan.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        penarikan.setRt(object.getString(Konfigurasi.KEY_RT));
                        penarikanList.add(penarikan);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PenarikanActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    penarikanAdapter = new PenarikanAdapter(PenarikanActivity.this, penarikanList);
                    recyclerView.setAdapter(penarikanAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_PENARIKAN, preferences.getString(Konfigurasi.KEY_USER_ID_PREFERENCE, null));
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
    }
}