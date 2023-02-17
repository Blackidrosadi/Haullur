package com.rosadi.haullur.Keluarga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.rosadi.haullur.List.Adapter.AlmarhumAdapter;
import com.rosadi.haullur.List.Adapter.KeluargaAdapter;
import com.rosadi.haullur.List.Model.Almarhum;
import com.rosadi.haullur.List.Model.Keluarga;
import com.rosadi.haullur.LoginActivity;
import com.rosadi.haullur.Penarikan.PenarikanActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataKeluargaActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<Keluarga> keluargaList = new ArrayList<>();
    KeluargaAdapter keluargaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_keluarga);

        recyclerView = findViewById(R.id.recycler_view);
        EditText cari = findViewById(R.id.cari);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.tambahkeluarga).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogTambahKeluarga();
            }
        });

        findViewById(R.id.filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DataKeluargaActivity.this, "Sepurane, Fitur e durung tersedia.", Toast.LENGTH_LONG).show();
            }
        });

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cariKeluarga(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DataKeluargaActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        keluargaAdapter = new KeluargaAdapter(DataKeluargaActivity.this, keluargaList);
        recyclerView.setAdapter(keluargaAdapter);

        loadKeluarga();

        SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadKeluarga();
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void loadKeluarga() {
        class LoadKeluarga extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataKeluargaActivity.this, "Informasi", "Memuat data keluarga...", false, false);
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

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DataKeluargaActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaAdapter = new KeluargaAdapter(DataKeluargaActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_KELUARGA);
                return s;
            }
        }

        LoadKeluarga loadKeluarga = new LoadKeluarga();
        loadKeluarga.execute();
    }

    private void cariKeluarga(String cari) {
        class CariKeluarga extends AsyncTask<Void, Void, String> {

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
                        keluargaList.add(keluarga);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DataKeluargaActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaAdapter = new KeluargaAdapter(DataKeluargaActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler handler = new RequestHandler();
                String s = handler.sendGetRequestParam(Konfigurasi.URL_CARI_KELUARGA, cari);
                return s;
            }
        }

        CariKeluarga cariKeluarga = new CariKeluarga();
        cariKeluarga.execute();
    }

    private void loadKeluargaTerbaru() {
        class LoadKeluarga extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataKeluargaActivity.this, "Informasi", "Memuat data keluarga...", false, false);
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

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DataKeluargaActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaAdapter = new KeluargaAdapter(DataKeluargaActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_KELUARGA_TERBARU);
                return s;
            }
        }

        LoadKeluarga loadKeluarga = new LoadKeluarga();
        loadKeluarga.execute();
    }

    private void openDialogTambahKeluarga() {
        Dialog dialog = new Dialog(DataKeluargaActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_keluarga);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        EditText namaEd = dialog.findViewById(R.id.nama);
        Spinner rt = dialog.findViewById(R.id.spinner);
        EditText teleponEd = dialog.findViewById(R.id.telepon);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_rt, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        rt.setAdapter(adapter);

        dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (namaEd.getText().toString().isEmpty()) {
                    Toast.makeText(DataKeluargaActivity.this, "Masukkan nama!!", Toast.LENGTH_SHORT).show();
                } else if (namaEd.getText().toString().length() < 4) {
                    Toast.makeText(DataKeluargaActivity.this, "Masukkan nama minimal 4 karakter!!", Toast.LENGTH_SHORT).show();
                } else {
                    tambahKeluarga(namaEd.getText().toString(), rt.getSelectedItem().toString(), teleponEd.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void tambahKeluarga(String nama, String rt, String telepon) {
        class TambahProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataKeluargaActivity.this, "Informasi", "Proses menambahkan...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                loadKeluargaTerbaru();

                Toast.makeText(DataKeluargaActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_RT, rt);
                hashMap.put(Konfigurasi.KEY_TELEPON, telepon);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TAMBAH_KELUARGA, hashMap);

                return s;
            }
        }

        TambahProses tambahProses = new TambahProses();
        tambahProses.execute();
    }
}