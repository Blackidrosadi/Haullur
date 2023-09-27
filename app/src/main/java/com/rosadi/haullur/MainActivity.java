package com.rosadi.haullur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.rosadi.haullur.Akun.LoginActivity;
import com.rosadi.haullur.Kelas.Akun.AkunActivity;
import com.rosadi.haullur.Kelas.Akun.Haul.ProgramHaulActivity;
import com.rosadi.haullur.Kelas.Almarhum.DataKeluargaActivity;
import com.rosadi.haullur.Kelas.Baca.BacaActivity;
import com.rosadi.haullur.Kelas.Laporan.LaporanActivity;
import com.rosadi.haullur.Kelas.Penarikan.PenarikanActivity;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String idHaulAktif = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, 0);
        if (preferences == null) {
            Intent intent = new Intent(this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            MainActivity.this.finish();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu_haul:
                        startActivity(new Intent(MainActivity.this, ProgramHaulActivity.class));
                        return true;

                    case R.id.nav_menu_akun:
                        startActivity(new Intent(MainActivity.this, AkunActivity.class));
                        return true;

                    case R.id.nav_log_out:
                        openDialogLogout();
                }

                return true;
            }
        });

        TextView namaDrawer = navigationView.getHeaderView(0).findViewById(R.id.nama);
        namaDrawer.setText(preferences.getString(Konfigurasi.KEY_USER_NAMA_PREFERENCE, null));
        TextView emailDrawer = navigationView.getHeaderView(0).findViewById(R.id.email);
        if (preferences.getString(Konfigurasi.KEY_USER_EMAIL_PREFERENCE, null).equals("")) {
            emailDrawer.setText(preferences.getString(Konfigurasi.KEY_USER_TELEPON_PREFERENCE, null));
        } else {
            emailDrawer.setText(preferences.getString(Konfigurasi.KEY_USER_EMAIL_PREFERENCE, null));
        }

        RelativeLayout buttonMenu = findViewById(R.id.button_menu);
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        TextView nama = findViewById(R.id.nama);
        nama.setText(preferences.getString(Konfigurasi.KEY_USER_NAMA_PREFERENCE, null));

        loadProgramHaulAktif();

        findViewById(R.id.button_baca).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProgramHaulAktif();
                if (idHaulAktif.equals("")) {
                    Toast.makeText(MainActivity.this, "Tidak ada program haul yang aktif, silakan hubungi admin untuk mengaktifkan haul jemuah legi.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, BacaActivity.class);
                    i.putExtra("id_haul", idHaulAktif);
                    startActivity(i);
                }
            }
        });
        findViewById(R.id.button_penarikan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekProgramHaul();
            }
        });
        findViewById(R.id.button_dataalmarhum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DataKeluargaActivity.class));
            }
        });
        findViewById(R.id.button_laporan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LaporanActivity.class));
            }
        });
    }

    private void openDialogLogout() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("Logout");
        teks.setText("Apakah anda yakin ingin logout ?");
        teksiya.setText("Logout");

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                preferences.edit().clear().commit();

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

    private void cekProgramHaul() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(MainActivity.this, "Informasi", "Memeriksa program haul...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.trim().equals("1")) {
                    startActivity(new Intent(MainActivity.this, PenarikanActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Tidak ada program haul yang aktif, silakan hubungi admin untuk mengaktifkan haul jemuah legi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_CEK_PROGRAM_HAUL);
                return s;
            }


        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void loadProgramHaulAktif() {
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

                    idHaulAktif = data.getString(Konfigurasi.KEY_ID);
                    String deskripsi = data.getString(Konfigurasi.KEY_DESKRIPSI);
                    String tanggal = data.getString(Konfigurasi.KEY_TANGGAL);

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
}