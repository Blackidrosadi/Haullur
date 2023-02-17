package com.rosadi.haullur;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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
                        Toast.makeText(MainActivity.this, "Haloooooooooooooooo", Toast.LENGTH_SHORT).show();
                        asdas
                        return true;
                }

                return true;
            }
        });

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