package com.rosadi.haullur.Kelas.Penarikan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.Locale;

public class PenarikanActivity extends AppCompatActivity {

    SharedPreferences preferences;
    public String idHaul, idAkun;

    KeluargaByAkunAdapter keluargaByAkunAdapter;
    ArrayList<Keluarga> keluargaList = new ArrayList<>();
    PenarikanAdapter penarikanAdapter;
    ArrayList<Penarikan> penarikanList = new ArrayList<>();

    TextView deskripsiTV, tanggalTV, totaldanaTV;
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
        totaldanaTV = findViewById(R.id.totaldana);
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
                        keluarga.setjumlahAlmarhum(object.getString(Konfigurasi.KEY_JUMLAH_ALMARHUM));
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
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID_AKUN, preferences.getString(Konfigurasi.KEY_USER_ID_PREFERENCE, null));
                hashMap.put(Konfigurasi.KEY_ID_HAUL, idHaul);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_KELUARGA_PENARIKAN, hashMap);
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
                    loadTotalPenarikanByPetugas();
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

    public void loadTotalPenarikanByPetugas() {
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
                    String totaldana = jsonObject.getString(Konfigurasi.KEY_JSON_ARRAY_RESULT);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance()
                                    .getCurrency()
                                    .getSymbol(local));
                    String cleanString = totaldana.replaceAll(replaceable, "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    NumberFormat formatter = NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    String replace = String.format("[Rp\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String clean = formatted.replaceAll(replace, "");

                    if (clean.equals("0")) {
                        totaldanaTV.setText("0,-");
                    } else {
                        totaldanaTV.setText(clean + ",-");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, preferences.getString(Konfigurasi.KEY_USER_ID_PREFERENCE, null));
                hashMap.put(Konfigurasi.KEY_ID_HAUL, idHaul);

                System.out.println("iduser = " + preferences.getString(Konfigurasi.KEY_USER_ID_PREFERENCE, null));
                System.out.println("idhaul = " + idHaul);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TOTAL_DANA_PENARIKAN, hashMap);
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
                        penarikan.setJumlahUang(object.getString(Konfigurasi.KEY_JUMLAH_UANG));
                        penarikan.setDeskripsi(object.getString(Konfigurasi.KEY_DESKRIPSI));
                        penarikan.setIdAkun(object.getString(Konfigurasi.KEY_ID_AKUN));
                        penarikan.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        penarikan.setRt(object.getString(Konfigurasi.KEY_RT));
                        penarikan.setJumlahAlmarhum(object.getString(Konfigurasi.KEY_JUMLAH_ALMARHUM));
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
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, preferences.getString(Konfigurasi.KEY_USER_ID_PREFERENCE, null));
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