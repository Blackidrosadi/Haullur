package com.rosadi.haullur.Akun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Akun.Daftar.RegisterActivity;
import com.rosadi.haullur.MainActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String id, nama, email, telepon, sandi, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, 0);
        if (preferences.getString(Konfigurasi.KEY_USER_ID_PREFERENCE, null) != null) {
            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            LoginActivity.this.finish();
        }

        EditText telepon = findViewById(R.id.telepon);
        EditText sandi = findViewById(R.id.sandi);

        findViewById(R.id.masuk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (telepon.getText().toString().equals("") && sandi.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Masukkan telepon dan kata sandi!", Toast.LENGTH_SHORT).show();
                } else if (telepon.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Masukkan nomor telepon!", Toast.LENGTH_SHORT).show();
                } else if (sandi.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Masukkan kata sandi!", Toast.LENGTH_SHORT).show();
                } else {
                    login(telepon.getText().toString(), sandi.getText().toString());
                }
            }
        });

        TextView daftarTV = findViewById(R.id.daftar);
        String daftarS = "Belum memiliki akun ? Daftar disini.";
        SpannableString span = new SpannableString(daftarS);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(getResources().getColor(R.color.accent));
                ds.setUnderlineText(false);
                ds.setFakeBoldText(true);
            }
        };

        span.setSpan(clickableSpan, 22, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        daftarTV.setText(span);
        daftarTV.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void login(String teleponNya, String sandiNya) {
        class LoginProcess extends AsyncTask<Void, Void, String> {

            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(LoginActivity.this, "Informasi", "Proses masuk...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    JSONObject data = result.getJSONObject(0);

                    id = data.getString(Konfigurasi.KEY_ID);
                    nama = data.getString(Konfigurasi.KEY_NAMA);
                    email = data.getString(Konfigurasi.KEY_EMAIL);
                    telepon = data.getString(Konfigurasi.KEY_TELEPON);
                    sandi = data.getString(Konfigurasi.KEY_SANDI);
                    level = data.getString(Konfigurasi.KEY_LEVEL);

                    if (sandi.equals("123")) {
                        Intent i = new Intent(LoginActivity.this, BuatPasswordBaruActivity.class);
                        i.putExtra(Konfigurasi.KEY_ID, id);
                        i.putExtra(Konfigurasi.KEY_NAMA, nama);
                        i.putExtra(Konfigurasi.KEY_EMAIL, email);
                        i.putExtra(Konfigurasi.KEY_TELEPON, telepon);
                        i.putExtra(Konfigurasi.KEY_LEVEL, level);
                        startActivity(i);
                        LoginActivity.this.finish();
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Konfigurasi.KEY_USER_ID_PREFERENCE, id);
                        editor.putString(Konfigurasi.KEY_USER_NAMA_PREFERENCE, nama);
                        editor.putString(Konfigurasi.KEY_USER_EMAIL_PREFERENCE, email);
                        editor.putString(Konfigurasi.KEY_USER_TELEPON_PREFERENCE, telepon);
                        editor.putString(Konfigurasi.KEY_USER_SANDI_PREFERENCE, sandi);
                        editor.putString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, level);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(LoginActivity.this, "Telepon atau Password salah!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_TELEPON, teleponNya);
                hashMap.put(Konfigurasi.KEY_SANDI, sandiNya);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_LOGIN, hashMap);

                return s;
            }
        }

        LoginProcess loginProcess = new LoginProcess();
        loginProcess.execute();
    }
}