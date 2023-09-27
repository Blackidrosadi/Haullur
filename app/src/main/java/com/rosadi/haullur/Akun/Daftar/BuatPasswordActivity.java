package com.rosadi.haullur.Akun.Daftar;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.auth.FirebaseAuth;
import com.rosadi.haullur.Akun.LoginActivity;
import com.rosadi.haullur.Kelas.Almarhum.DataKeluargaActivity;
import com.rosadi.haullur.MainActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.util.HashMap;

public class BuatPasswordActivity extends AppCompatActivity {

    FirebaseAuth auth;

    EditText sandiET, konfirmasiSandiET;
    String nama, email, telepon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_password);

        auth = FirebaseAuth.getInstance();
        auth.signOut();

        Intent i = getIntent();
        nama = i.getStringExtra("nama");
        email = i.getStringExtra("email");
        telepon = i.getStringExtra("telepon");

        sandiET = findViewById(R.id.sandi);
        konfirmasiSandiET = findViewById(R.id.konfirmasi_sandi);

        findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sandiET.getText().toString().isEmpty() || sandiET.getText().toString().length() == 0) {
                    Toast.makeText(BuatPasswordActivity.this, "Masukkan kata sandi!", Toast.LENGTH_SHORT).show();
                } else if (sandiET.getText().toString().length() < 6) {
                    Toast.makeText(BuatPasswordActivity.this, "Masukkan kata sandi minimal 6 karakter!", Toast.LENGTH_SHORT).show();
                } else if (konfirmasiSandiET.getText().toString().isEmpty() || konfirmasiSandiET.getText().toString().length() == 0) {
                    Toast.makeText(BuatPasswordActivity.this, "Masukkan konfirmasi kata sandi!", Toast.LENGTH_SHORT).show();
                } else if (!konfirmasiSandiET.getText().toString().equals(sandiET.getText().toString())) {
                    Toast.makeText(BuatPasswordActivity.this, "Konfirmasi kata sandi tidak sama!", Toast.LENGTH_SHORT).show();
                } else {
                    daftarAkunBaru();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(BuatPasswordActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("Kembali");
        teks.setText("Apakah anda yakin ingin kembali ? Semua data yang Anda inputkan akan hilang.");
        teksiya.setText("Kembali");

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onBackPressed();
                finish();
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

    private void daftarAkunBaru() {
        class TambahProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(BuatPasswordActivity.this, "Informasi", "Proses mendaftar...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                Intent i = new Intent(BuatPasswordActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

                Toast.makeText(BuatPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_EMAIL, email);
                hashMap.put(Konfigurasi.KEY_TELEPON, telepon);
                hashMap.put(Konfigurasi.KEY_SANDI, konfirmasiSandiET.getText().toString());
                hashMap.put(Konfigurasi.KEY_LEVEL, "0");

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TAMBAH_AKUN, hashMap);

                return s;
            }
        }

        TambahProses tambahProses = new TambahProses();
        tambahProses.execute();
    }
}