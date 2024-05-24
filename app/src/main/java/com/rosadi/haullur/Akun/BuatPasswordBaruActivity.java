package com.rosadi.haullur.Akun;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rosadi.haullur.MainActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.util.HashMap;

public class BuatPasswordBaruActivity extends AppCompatActivity {

    SharedPreferences preferences;

    String id, nama, email, telepon, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_password_baru);

        preferences = getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, 0);

        Intent i = getIntent();
        id = i.getStringExtra(Konfigurasi.KEY_ID);
        nama = i.getStringExtra(Konfigurasi.KEY_NAMA);
        email = i.getStringExtra(Konfigurasi.KEY_EMAIL);
        telepon = i.getStringExtra(Konfigurasi.KEY_TELEPON);
        level = i.getStringExtra(Konfigurasi.KEY_LEVEL);

        EditText sandiET = findViewById(R.id.sandi);
        EditText konfirmasiSandiET = findViewById(R.id.konfirmasi_sandi);
        findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sandiET.getText().toString().isEmpty() || sandiET.getText().toString().length() == 0) {
                    Toast.makeText(BuatPasswordBaruActivity.this, "Masukkan kata sandi!", Toast.LENGTH_SHORT).show();
                } else if (sandiET.getText().toString().length() < 6) {
                    Toast.makeText(BuatPasswordBaruActivity.this, "Masukkan kata sandi minimal 6 karakter!", Toast.LENGTH_SHORT).show();
                } else if (konfirmasiSandiET.getText().toString().isEmpty() || konfirmasiSandiET.getText().toString().length() == 0) {
                    Toast.makeText(BuatPasswordBaruActivity.this, "Masukkan konfirmasi kata sandi!", Toast.LENGTH_SHORT).show();
                } else if (!konfirmasiSandiET.getText().toString().equals(sandiET.getText().toString())) {
                    Toast.makeText(BuatPasswordBaruActivity.this, "Konfirmasi kata sandi tidak sama!", Toast.LENGTH_SHORT).show();
                } else {
                    updatePassword(konfirmasiSandiET.getText().toString());
                }
            }
        });
    }

    private void updatePassword(String sandi) {
        class ProsesUpdate extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(BuatPasswordBaruActivity.this, "Informasi", "Proses mengupdate...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah sandi berhasil diperbarui")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Konfigurasi.KEY_USER_ID_PREFERENCE, id);
                    editor.putString(Konfigurasi.KEY_USER_NAMA_PREFERENCE, nama);
                    editor.putString(Konfigurasi.KEY_USER_EMAIL_PREFERENCE, email);
                    editor.putString(Konfigurasi.KEY_USER_TELEPON_PREFERENCE, telepon);
                    editor.putString(Konfigurasi.KEY_USER_SANDI_PREFERENCE, sandi);
                    editor.putString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, level);
                    editor.apply();

                    Intent i = new Intent(BuatPasswordBaruActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                Toast.makeText(BuatPasswordBaruActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);
                hashMap.put(Konfigurasi.KEY_SANDI, sandi);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_UPDATE_SANDI, hashMap);

                return s;
            }
        }

        ProsesUpdate update = new ProsesUpdate();
        update.execute();
    }
}