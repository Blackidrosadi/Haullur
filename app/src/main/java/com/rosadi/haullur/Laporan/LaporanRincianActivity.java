package com.rosadi.haullur.Laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rosadi.haullur.R;

import java.text.NumberFormat;
import java.util.Locale;

public class LaporanRincianActivity extends AppCompatActivity {

    String idHaul, idAkun, petugas, subtotal;
    TextView petugasTV, totalDanaTV;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_rincian);

        Intent i = getIntent();
        idHaul = i.getStringExtra("id_haul");
        idAkun = i.getStringExtra("id_akun");
        petugas = i.getStringExtra("nama");
        subtotal = i.getStringExtra("subtotal");

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        petugasTV = findViewById(R.id.petugas);
        totalDanaTV = findViewById(R.id.totaldana);

        petugasTV.setText(petugas);
        totalDanaTV.setText(rupiahFormat(subtotal) + ",-");
    }

    public String rupiahFormat(String jumlah) {
        Locale local = new Locale("id", "id");
        String replaceable = String.format("[Rp,.\\s]",
                NumberFormat.getCurrencyInstance()
                        .getCurrency()
                        .getSymbol(local));
        String cleanString = jumlah.replaceAll(replaceable, "");

        double parsed;
        try {
            parsed = Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            parsed = 0.00;
        }

        NumberFormat formatter = NumberFormat
                .getCurrencyInstance(local);
        formatter.setMaximumFractionDigits(0);
        formatter.setParseIntegerOnly(true);
        String formatted = formatter.format((parsed));

        String replace = String.format("[Rp\\s]",
                NumberFormat.getCurrencyInstance().getCurrency()
                        .getSymbol(local));
        String clean = formatted.replaceAll(replace, "");

        return clean;
    }
}