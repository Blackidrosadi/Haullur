package com.rosadi.haullur.Kelas.Akun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rosadi.haullur.List.Adapter.AkunAdapter;
import com.rosadi.haullur.List.Model.Akun;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AkunActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<Akun> akunList = new ArrayList<>();
    AkunAdapter akunAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun);

        recyclerView = findViewById(R.id.recycler_view);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.tambah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AkunActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_tambah_akun);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                EditText namaET = dialog.findViewById(R.id.nama);
                EditText teleponET = dialog.findViewById(R.id.telepon);
                EditText emailET = dialog.findViewById(R.id.email);

                dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (namaET.getText().toString().equals("") || teleponET.getText().toString().equals("")) {
                            Toast.makeText(AkunActivity.this, "Lengkapi form terlebih dahulu!", Toast.LENGTH_SHORT).show();
                        } else if (namaET.getText().length() < 4) {
                            Toast.makeText(AkunActivity.this, "Masukkan nama minimal 4 karakter!", Toast.LENGTH_SHORT).show();
                        } else if (teleponET.getText().length() < 10) {
                            Toast.makeText(AkunActivity.this, "Nomor telepon tidak valid!", Toast.LENGTH_SHORT).show();
                        } else if (emailET.getText().length() > 0) {
                            if (!isValidEmail(emailET.getText().toString())) {
                                Toast.makeText(AkunActivity.this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
                            } else {
                                tambahAkun(namaET.getText().toString(), teleponET.getText().toString(), emailET.getText().toString());
                                dialog.dismiss();
                            }
                        } else {
                            tambahAkun(namaET.getText().toString(), teleponET.getText().toString(), emailET.getText().toString());
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
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AkunActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        akunAdapter = new AkunAdapter(AkunActivity.this, akunList);
        recyclerView.setAdapter(akunAdapter);

        loadAkun();
    }

    private void loadAkun() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(AkunActivity.this, "Informasi", "Memuat data akun...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                akunList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Akun akun = new Akun();
                        akun.setId(object.getString(Konfigurasi.KEY_ID));
                        akun.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        akun.setEmail(object.getString(Konfigurasi.KEY_EMAIL));
                        akun.setTelepon(object.getString(Konfigurasi.KEY_TELEPON));
                        akun.setSandi(object.getString(Konfigurasi.KEY_SANDI));
                        akun.setLevel(object.getString(Konfigurasi.KEY_LEVEL));
                        akunList.add(akun);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AkunActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    akunAdapter = new AkunAdapter(AkunActivity.this, akunList);
                    recyclerView.setAdapter(akunAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_AKUN);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void tambahAkun(String nama, String telepon, String email) {
        class TambahProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(AkunActivity.this, "Informasi", "Proses menambahkan...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                loadAkun();

                Toast.makeText(AkunActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_TELEPON, telepon);
                hashMap.put(Konfigurasi.KEY_EMAIL, email);
                hashMap.put(Konfigurasi.KEY_SANDI, "123");
                hashMap.put(Konfigurasi.KEY_LEVEL, "2");

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TAMBAH_AKUN, hashMap);

                return s;
            }
        }

        TambahProses tambahProses = new TambahProses();
        tambahProses.execute();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}