package com.rosadi.haullur.Akun.Daftar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Akun.LoginActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

public class RegisterActivity extends AppCompatActivity {

    EditText nama, email, teleponEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama = findViewById(R.id.nama);
        teleponEt = findViewById(R.id.telepon);
        email = findViewById(R.id.email);

        findViewById(R.id.daftar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nama.getText().toString().equals("") || nama.getText().toString().length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Masukkan nama!", Toast.LENGTH_SHORT).show();
                } else if (nama.getText().toString().length() < 4) {
                    Toast.makeText(RegisterActivity.this, "Masukkan nama lebih dari 3 karakter!", Toast.LENGTH_SHORT).show();
                } else if (nama.getText().toString().length() > 25) {
                    Toast.makeText(RegisterActivity.this, "Masukkan nama tidak lebih dari 25 karakter!", Toast.LENGTH_SHORT).show();
                } else if (teleponEt.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Masukkan nomor telepon!", Toast.LENGTH_SHORT).show();
                } else if (teleponEt.getText().toString().length() < 10) {
                    Toast.makeText(RegisterActivity.this, "Nomor telepon yang Anda masukkan tidak valid!", Toast.LENGTH_SHORT).show();
                } else if (teleponEt.getText().toString().length() > 15) {
                    Toast.makeText(RegisterActivity.this, "Nomor telepon maksimal 15 Angka!", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Toast.makeText(RegisterActivity.this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
                } else {
                    cekTeleponTerdaftar(teleponEt.getText().toString());
                }
            }
        });

        TextView daftarTV = findViewById(R.id.masuk);
        String daftarS = "Sudah mempunyai akun ? Masuk disini.";
        SpannableString span = new SpannableString(daftarS);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                RegisterActivity.this.finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(getResources().getColor(R.color.accent));
                ds.setUnderlineText(false);
                ds.setFakeBoldText(true);
            }
        };

        span.setSpan(clickableSpan, 23, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        daftarTV.setText(span);
        daftarTV.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void cekTeleponTerdaftar(String telepon) {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(RegisterActivity.this, "Informasi", "Memeriksa nomor telepon...", false, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.trim().equals("1")) {
                    Toast.makeText(RegisterActivity.this, "Nomor telepon sudah terdaftar!", Toast.LENGTH_SHORT).show();
                } else {
//                    String teleponNya = "+62" + telepon;
//                    openDialogVerifikasiKode(teleponNya);

                    Intent i = new Intent(RegisterActivity.this, BuatPasswordActivity.class);
                    i.putExtra("nama", nama.getText().toString());
                    i.putExtra("email", email.getText().toString());
                    i.putExtra("telepon", teleponEt.getText().toString());
                    startActivity(i);
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_CEK_NOMOR_TELEPON, telepon);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }
}