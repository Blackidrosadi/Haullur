package com.rosadi.haullur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.rosadi.haullur.Akun.AkunActivity;
import com.rosadi.haullur.Haul.ProgramHaulActivity;
import com.rosadi.haullur.Keluarga.DataKeluargaActivity;
import com.rosadi.haullur.Laporan.LaporanActivity;
import com.rosadi.haullur.List.Adapter.KeluargaAdapter;
import com.rosadi.haullur.List.Model.Keluarga;
import com.rosadi.haullur.Penarikan.PenarikanActivity;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

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

        findViewById(R.id.button_informasi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, PenarikanActivity.class));
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
}