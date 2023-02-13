package com.rosadi.haullur.Keluarga;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.List.Adapter.AlmarhumAdapter;
import com.rosadi.haullur.List.Model.Almarhum;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataKeluargaDetailActivity extends AppCompatActivity {

    TextView namaTV, jumlahAlmarhumTV;
    RecyclerView recyclerView;

    String idNya, namaNya, rtNya, teleponNya;
    List<Almarhum> almarhumList = new ArrayList<>();
    AlmarhumAdapter almarhumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_keluarga_detail);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogEditKeluarga();
            }
        });

        findViewById(R.id.hapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogHapusKeluarga();
            }
        });

        findViewById(R.id.tambah_almarhum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogTambahAlmarhum();
            }
        });

        Intent i = getIntent();
        idNya = i.getStringExtra(Konfigurasi.KEY_ID);
        namaNya = i.getStringExtra(Konfigurasi.KEY_NAMA);
        rtNya = i.getStringExtra(Konfigurasi.KEY_RT);
        teleponNya = i.getStringExtra(Konfigurasi.KEY_TELEPON);

        namaTV = findViewById(R.id.nama);
        namaTV.setText(namaNya);

        jumlahAlmarhumTV = findViewById(R.id.jumlah_almarhum);
        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DataKeluargaDetailActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        almarhumAdapter = new AlmarhumAdapter(DataKeluargaDetailActivity.this, almarhumList);
        recyclerView.setAdapter(almarhumAdapter);

        loadAlmarhums();
    }

    private void openDialogTambahAlmarhum() {
        Dialog dialog = new Dialog(DataKeluargaDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_almarhum);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        EditText namaEd = dialog.findViewById(R.id.nama);

        dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (namaEd.getText().toString().isEmpty()) {
                    Toast.makeText(DataKeluargaDetailActivity.this, "Masukkan nama!!", Toast.LENGTH_SHORT).show();
                } else if (namaEd.getText().toString().length() < 4) {
                    Toast.makeText(DataKeluargaDetailActivity.this, "Masukkan nama minimal 4 karakter!!", Toast.LENGTH_SHORT).show();
                } else {
                    tambahAlmarhum(namaEd.getText().toString());
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

    public void loadAlmarhums() {
        class LoadAlmarhums extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(DataKeluargaDetailActivity.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataKeluargaDetailActivity.this, "Informasi", "Memuat daftar almarhum / almarhumah...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                almarhumList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Almarhum almarhum = new Almarhum();
                        almarhum.setId(object.getString(Konfigurasi.KEY_ID));
                        almarhum.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        almarhum.setIdKeluarga(object.getString(Konfigurasi.KEY_ID_KELUARGA));

                        almarhum.setNomor(""+(i+1));
                        almarhumList.add(almarhum);
                    }

                    jumlahAlmarhumTV.setText(almarhumList.size() + " Almarhum / Almarhumah");

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DataKeluargaDetailActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    almarhumAdapter = new AlmarhumAdapter(DataKeluargaDetailActivity.this, almarhumList);
                    recyclerView.setAdapter(almarhumAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler handler = new RequestHandler();
                String s = handler.sendGetRequestParam(Konfigurasi.URL_LOAD_ALMARHUM, idNya);
                return s;
            }
        }

        LoadAlmarhums almarhums = new LoadAlmarhums();
        almarhums.execute();
    }

    private void openDialogEditKeluarga() {
        Dialog dialog = new Dialog(DataKeluargaDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ubah_keluarga);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        EditText namaEd = dialog.findViewById(R.id.nama);
        Spinner rt = dialog.findViewById(R.id.spinner);
        EditText teleponEd = dialog.findViewById(R.id.telepon);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_rt, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        rt.setAdapter(adapter);

        namaEd.setText(namaNya);
        int spinnerPosition = adapter.getPosition(rtNya);
        rt.setSelection(spinnerPosition);
        teleponEd.setText(teleponNya);

        dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (namaEd.getText().toString().isEmpty()) {
                    Toast.makeText(DataKeluargaDetailActivity.this, "Masukkan nama!!", Toast.LENGTH_SHORT).show();
                } else if (namaEd.getText().toString().length() < 4) {
                    Toast.makeText(DataKeluargaDetailActivity.this, "Masukkan nama minimal 4 karakter!!", Toast.LENGTH_SHORT).show();
                } else {
                    editKeluarga(namaEd.getText().toString(), rt.getSelectedItem().toString(), teleponEd.getText().toString());
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

    private void openDialogHapusKeluarga() {
        Dialog dialog = new Dialog(DataKeluargaDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("Hapus Data Keluarga");
        teks.setText("Menghapus data keluarga akan menghapus data Almarhum / Almarhumah juga, apakah Anda ingin menghapusnya ?");
        teksiya.setText("Hapus");

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusKeluarga();
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

    private void tambahAlmarhum(String nama) {
        class TambahProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataKeluargaDetailActivity.this, "Informasi", "Proses menambahkan...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                loadAlmarhums();

                Toast.makeText(DataKeluargaDetailActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, idNya);
                hashMap.put(Konfigurasi.KEY_NAMA, nama);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TAMBAH_ALMARHUM, hashMap);

                return s;
            }
        }

        TambahProses tambahProses = new TambahProses();
        tambahProses.execute();

    }

    private void editKeluarga(String nama, String rt, String telepon) {
        class EditProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataKeluargaDetailActivity.this, "Informasi", "Proses memperbarui...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah data keluarga berhasil diperbarui")) {
                    namaTV.setText(nama);
                    namaNya = nama;
                    rtNya = rt;
                    teleponNya = telepon;
                }

                Toast.makeText(DataKeluargaDetailActivity.this, s, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(DataKeluargaDetailActivity.this, DataKeluargaActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, idNya);
                hashMap.put(Konfigurasi.KEY_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_RT, rt);
                hashMap.put(Konfigurasi.KEY_TELEPON, telepon);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_EDIT_KELUARGA, hashMap);

                return s;
            }
        }

        EditProses tambahProses = new EditProses();
        tambahProses.execute();
    }

    private void hapusKeluarga() {
        class HapusProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataKeluargaDetailActivity.this, "Informasi", "Proses menghapus...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                Toast.makeText(DataKeluargaDetailActivity.this, s, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(DataKeluargaDetailActivity.this, DataKeluargaActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, idNya);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_KELUARGA, hashMap);

                return s;
            }
        }

        HapusProses hapusProses = new HapusProses();
        hapusProses.execute();
    }
}