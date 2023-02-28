package com.rosadi.haullur.Keluarga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.rosadi.haullur.List.Adapter.KeluargaAdapter;
import com.rosadi.haullur.List.Model.Keluarga;
import com.rosadi.haullur.LoginActivity;
import com.rosadi.haullur.MainActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataKeluargaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public ProgressBar progressBar;

    List<Keluarga> keluargaList = new ArrayList<>();
    KeluargaAdapter keluargaAdapter;

    RequestQueue requestQueue;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_keluarga);

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        EditText cari = findViewById(R.id.cari);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.tambahkeluarga).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogTambahKeluarga();
            }
        });

        findViewById(R.id.carinya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cariKeluarga(cari.getText().toString());
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DataKeluargaActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        requestQueue = Volley.newRequestQueue(this);
        loadKeluarga();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    if (cari.getText().toString().isEmpty()) {
                        if (isLastItemDisplaying(recyclerView)) {
                            loadKeluarga();
                        }
                    }
                }
            });
        }

        keluargaAdapter = new KeluargaAdapter(DataKeluargaActivity.this, keluargaList);
        recyclerView.setAdapter(keluargaAdapter);

        cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    private void loadKeluarga() {
        requestQueue.add(getDataFromServer(Konfigurasi.URL_LOAD_KELUARGA_PAGINATION, page));
        page++;
    }

    private JsonArrayRequest getDataFromServer(String url, int page) {
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url + String.valueOf(page),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            Keluarga keluarga = new Keluarga();
                            JSONObject jsonObject = null;
                            try {
                                JSONObject object = response.getJSONObject(i);

                                keluarga.setId(object.getString(Konfigurasi.KEY_ID));
                                keluarga.setNama(object.getString(Konfigurasi.KEY_NAMA));
                                keluarga.setRt(object.getString(Konfigurasi.KEY_RT));
                                keluarga.setTelepon(object.getString(Konfigurasi.KEY_TELEPON));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            keluargaList.add(keluarga);
                        }

                        keluargaAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(DataKeluargaActivity.this, "Semua data sudah dimuat", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonArrayRequest;
    }

    private void cariKeluarga(String cari) {
        class CariKeluarga extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog = new ProgressDialog(DataKeluargaActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataKeluargaActivity.this, "Informasi", "Mencari keluarga...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                keluargaList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Keluarga keluarga = new Keluarga();
                        keluarga.setId(object.getString(Konfigurasi.KEY_ID));
                        keluarga.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        keluarga.setRt(object.getString(Konfigurasi.KEY_RT));
                        keluarga.setTelepon(object.getString(Konfigurasi.KEY_TELEPON));
                        keluargaList.add(keluarga);
                    }
                    keluargaAdapter.notifyDataSetChanged();

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DataKeluargaActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaAdapter = new KeluargaAdapter(DataKeluargaActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler handler = new RequestHandler();
                String s = handler.sendGetRequestParam(Konfigurasi.URL_CARI_KELUARGA, cari);
                return s;
            }
        }

        CariKeluarga cariKeluarga = new CariKeluarga();
        cariKeluarga.execute();
    }

    private void openDialogTambahKeluarga() {
        Dialog dialog = new Dialog(DataKeluargaActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_keluarga);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        EditText namaEd = dialog.findViewById(R.id.nama);
        Spinner rt = dialog.findViewById(R.id.spinner);
        EditText teleponEd = dialog.findViewById(R.id.telepon);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.list_rt, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        rt.setAdapter(adapter);

        dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (namaEd.getText().toString().isEmpty()) {
                    Toast.makeText(DataKeluargaActivity.this, "Masukkan nama!!", Toast.LENGTH_SHORT).show();
                } else if (namaEd.getText().toString().length() < 4) {
                    Toast.makeText(DataKeluargaActivity.this, "Masukkan nama minimal 4 karakter!!", Toast.LENGTH_SHORT).show();
                } else {
                    tambahKeluarga(namaEd.getText().toString(), rt.getSelectedItem().toString(), teleponEd.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void tambahKeluarga(String nama, String rt, String telepon) {
        class TambahProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DataKeluargaActivity.this, "Informasi", "Proses menambahkan...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                loadKeluargaBaruDitambahkan(nama, rt);

                Toast.makeText(DataKeluargaActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_RT, rt);
                hashMap.put(Konfigurasi.KEY_TELEPON, telepon);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TAMBAH_KELUARGA, hashMap);

                return s;
            }
        }

        TambahProses tambahProses = new TambahProses();
        tambahProses.execute();
    }

    private void loadKeluargaBaruDitambahkan(String nama, String rt) {
        class TambahProses extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    JSONObject data = result.getJSONObject(0);

                    Intent i = new Intent(DataKeluargaActivity.this, DataKeluargaDetailActivity.class);
                    i.putExtra(Konfigurasi.KEY_ID, data.getString(Konfigurasi.KEY_ID));
                    i.putExtra(Konfigurasi.KEY_NAMA, data.getString(Konfigurasi.KEY_NAMA));
                    i.putExtra(Konfigurasi.KEY_RT, data.getString(Konfigurasi.KEY_RT));
                    i.putExtra(Konfigurasi.KEY_TELEPON, data.getString(Konfigurasi.KEY_TELEPON));
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_NAMA, nama);
                hashMap.put(Konfigurasi.KEY_RT, rt);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_LOAD_KELUARGA_BARU_DITAMBAHKAN, hashMap);

                return s;
            }
        }

        TambahProses tambahProses = new TambahProses();
        tambahProses.execute();
    }
}