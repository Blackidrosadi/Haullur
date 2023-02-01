package com.rosadi.haullur;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
            startActivity(new Intent(this, MainActivity.class));
        }

        EditText telepon = findViewById(R.id.telepon);
        EditText sandi = findViewById(R.id.sandi);

        findViewById(R.id.masuk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(telepon.getText().toString(), sandi.getText().toString());
            }
        });
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

                    System.out.println("namasayaaaaa : " + nama);

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(LoginActivity.this, "Telepon atau Password salah!!", Toast.LENGTH_LONG).show();
                }

//                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
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