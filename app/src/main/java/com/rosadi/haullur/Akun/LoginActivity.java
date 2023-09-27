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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, MODE_PRIVATE);
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
                login(telepon.getText().toString(), sandi.getText().toString());
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

    private void login(String telepon, String sandi) {
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

                    String id = data.getString(Konfigurasi.KEY_ID);
                    String nama = data.getString(Konfigurasi.KEY_NAMA);
                    String email = data.getString(Konfigurasi.KEY_EMAIL);
                    String telepon = data.getString(Konfigurasi.KEY_TELEPON);
                    String sandi = data.getString(Konfigurasi.KEY_SANDI);
                    String level = data.getString(Konfigurasi.KEY_LEVEL);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Konfigurasi.KEY_USER_ID_PREFERENCE, id);
                    editor.putString(Konfigurasi.KEY_USER_NAMA_PREFERENCE, nama);
                    editor.putString(Konfigurasi.KEY_USER_EMAIL_PREFERENCE, email);
                    editor.putString(Konfigurasi.KEY_USER_TELEPON_PREFERENCE, telepon);
                    editor.putString(Konfigurasi.KEY_USER_SANDI_PREFERENCE, sandi);
                    editor.putString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, level);
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(LoginActivity.this, "Telepon atau Password salah!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_TELEPON, telepon);
                hashMap.put(Konfigurasi.KEY_SANDI, sandi);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_LOGIN, hashMap);

                return s;
            }
        }

        LoginProcess loginProcess = new LoginProcess();
        loginProcess.execute();
    }
}