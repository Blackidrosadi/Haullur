package com.rosadi.haullur.Kelas.DrawerMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rosadi.haullur.R;

public class TentangAplikasiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang_aplikasi);

        findViewById(R.id.kembali).setOnClickListener(view -> {
            onBackPressed();
        });
    }
}