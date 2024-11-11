package com.rosadi.haullur.Kelas.Pendaftaran;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.List.Adapter.AlmarhumPrefAdapter;
import com.rosadi.haullur.List.Model.Almarhum;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FormAlmarhumActivity extends AppCompatActivity {

    TextView namaTV, jumlahAlmarhumTV;
    RecyclerView recyclerView;

    String nama, telepon, rt;
    AlmarhumPrefAdapter almarhumPrefAdapter;
    List<Almarhum> almarhumList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_almarhum);

        findViewById(R.id.kembali).setOnClickListener(view -> onBackPressed());

        Intent i = getIntent();
        nama = i.getStringExtra("nama");
        telepon = i.getStringExtra("telepon");
        rt = i.getStringExtra("rt");

        namaTV = findViewById(R.id.nama);
        jumlahAlmarhumTV = findViewById(R.id.jumlah_almarhum);
        recyclerView = findViewById(R.id.recycler_view);

        namaTV.setText(nama);
        jumlahAlmarhumTV.setText("0 Almarhum / Almarhumah");

        findViewById(R.id.tambah_almarhum).setOnClickListener(view -> {
            openDialogTambahAlmarhum();
        });

        findViewById(R.id.simpan).setOnClickListener(view -> {
            Dialog dialog = new Dialog(FormAlmarhumActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_iya_tidak);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            TextView judul = dialog.findViewById(R.id.judul);
            TextView teks = dialog.findViewById(R.id.teks);
            TextView teksiya = dialog.findViewById(R.id.teksiya);

            judul.setText("PENDAFTARAN KELUARGA");
            teks.setText("Pastikan data keluarga dan data almarhum/almarhumah yang Anda masukkan sudah benar. Setelah Anda melakukan pendaftaran, data hanya bisa diubah oleh petugas. Apakah Anda yakin untuk melakukan pendaftaran ?");
            teksiya.setText("Daftar");

            dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    daftarKeluarga();
                }
            });

            dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FormAlmarhumActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        almarhumPrefAdapter = new AlmarhumPrefAdapter(FormAlmarhumActivity.this, almarhumList);
        recyclerView.setAdapter(almarhumPrefAdapter);
    }

    private void daftarKeluarga() {
        class TambahProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(FormAlmarhumActivity.this, "Informasi", "Proses mendaftarkan keluarga...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                Intent i = new Intent(FormAlmarhumActivity.this, PendaftaranBerhasilActivity.class);
                i.putExtra("nama", nama);
                i.putExtra("rt", rt);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_TELEPON, telepon);
                hashMap.put(Konfigurasi.KEY_RT, rt);
                hashMap.put("jumlah", "" + almarhumList.size());
                for (Almarhum almarhum : almarhumList) {
                    hashMap.put("almarhum" + (almarhumList.indexOf(almarhum) + 1), almarhum.getNama());
                }

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_DAFTAR_KELUARGA, hashMap);

                return s;
            }
        }

        TambahProses tambahProses = new TambahProses();
        tambahProses.execute();
    }

    private void openDialogTambahAlmarhum() {
        Dialog dialog = new Dialog(FormAlmarhumActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_almarhum);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        EditText namaEd = dialog.findViewById(R.id.nama);

        dialog.findViewById(R.id.simpan).setOnClickListener(view -> {
            if (namaEd.getText().toString().isEmpty()) {
                Toast.makeText(FormAlmarhumActivity.this, "Masukkan nama!!", Toast.LENGTH_SHORT).show();
            } else if (namaEd.getText().toString().length() < 4) {
                Toast.makeText(FormAlmarhumActivity.this, "Masukkan nama minimal 4 karakter!!", Toast.LENGTH_SHORT).show();
            } else {
                tambahAlmarhum(namaEd.getText().toString());
                dialog.dismiss();
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
        Almarhum almarhum = new Almarhum();
        almarhum.setNama(nama);

        almarhumList.add(almarhum);
        jumlahAlmarhumTV.setText(almarhumList.size() + " Almarhum / Almarhumah");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FormAlmarhumActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        almarhumPrefAdapter = new AlmarhumPrefAdapter(FormAlmarhumActivity.this, almarhumList);
        recyclerView.setAdapter(almarhumPrefAdapter);
    }

    public void hapusAlmarhum(Almarhum almarhum) {
        almarhumList.remove(almarhum);
        jumlahAlmarhumTV.setText(almarhumList.size() + " Almarhum / Almarhumah");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FormAlmarhumActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        almarhumPrefAdapter = new AlmarhumPrefAdapter(FormAlmarhumActivity.this, almarhumList);
        recyclerView.setAdapter(almarhumPrefAdapter);
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(FormAlmarhumActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("PERINGATAN!!");
        teks.setText("Kembali ke halaman sebelumnya akan menghapus semua data yang telah Anda masukkan. Apakah anda yakin ingin kembali ?");
        teksiya.setText("Kembali");

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finishAndRemoveTask();
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
}