package com.rosadi.haullur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rosadi.haullur.Keluarga.DataKeluargaActivity;
import com.rosadi.haullur.Laporan.LaporanActivity;
import com.rosadi.haullur.Penarikan.PenarikanActivity;
import com.rosadi.haullur._util.Konfigurasi;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, 0);
        if (preferences == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

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
                startActivity(new Intent(MainActivity.this, PenarikanActivity.class));
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
}