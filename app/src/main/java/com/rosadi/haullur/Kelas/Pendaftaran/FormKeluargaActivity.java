package com.rosadi.haullur.Kelas.Pendaftaran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.rosadi.haullur.R;

public class FormKeluargaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_keluarga);

        EditText nama = findViewById(R.id.nama);
        EditText telepon = findViewById(R.id.telepon);
        Spinner rt = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_rt, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        rt.setAdapter(adapter);

        findViewById(R.id.lanjutkan).setOnClickListener(view -> {
            if (nama.getText().toString().equals("") || nama.getText().length() == 0) {
                Toast.makeText(FormKeluargaActivity.this, "Masukkan nama keluarga!", Toast.LENGTH_SHORT).show();
            } else if (nama.getText().length() < 3) {
                Toast.makeText(FormKeluargaActivity.this, "Masukkan nama keluarga dengan lengkap. minimal 3 karakter.", Toast.LENGTH_SHORT).show();
            } else if (telepon.getText().toString().equals("") || telepon.getText().length() == 0) {
                Toast.makeText(FormKeluargaActivity.this, "Masukkan nomor WhatsApp!", Toast.LENGTH_SHORT).show();
            } else if (telepon.getText().length() < 10) {
                Toast.makeText(FormKeluargaActivity.this, "Masukkan nomor WhatsApp minimal 10 angka!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(FormKeluargaActivity.this, FormAlmarhumActivity.class);
                intent.putExtra("nama", nama.getText().toString().trim());
                intent.putExtra("telepon", teleponFormat(telepon.getText().toString().trim()));
                intent.putExtra("rt", rt.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }

    private String teleponFormat(String telepon) {
        if (telepon.startsWith("0")) {
            telepon = "62" + telepon.substring(1);
        } else if (telepon.startsWith("8")) {
            telepon = "62" + telepon;
        }

        return telepon;
    }
}