package com.rosadi.haullur.Laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rosadi.haullur.Haul.ProgramHaulActivity;
import com.rosadi.haullur.List.Adapter.HaulAdapter;
import com.rosadi.haullur.List.Model.Haul;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LaporanActivity extends AppCompatActivity {

    List<Haul> haulList = new ArrayList<>();
    HaulAdapter haulAdapter;
    RecyclerView recyclerView;

    TextView totaldanaTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        totaldanaTV = findViewById(R.id.totaldana);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LaporanActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        haulAdapter = new HaulAdapter(LaporanActivity.this, haulList);
        recyclerView.setAdapter(haulAdapter);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHaul();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        loadHaul();
    }



    private void loadHaul() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(LaporanActivity.this, "Informasi", "Memuat laporan haul...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                haulList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String totaldana = jsonObject.getString("total_dana");

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance()
                                    .getCurrency()
                                    .getSymbol(local));
                    String cleanString = totaldana.replaceAll(replaceable, "");

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

                    if (clean.equals("0")) {
                        totaldanaTV.setText("0,-");
                    } else {
                        totaldanaTV.setText(clean + ",-");
                    }

                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Haul haul = new Haul();
                        haul.setId(object.getString(Konfigurasi.KEY_ID));
                        haul.setDeskripsi(object.getString(Konfigurasi.KEY_DESKRIPSI));
                        haul.setTanggal(object.getString(Konfigurasi.KEY_TANGGAL));
                        haul.setStatus(object.getString(Konfigurasi.KEY_STATUS));
                        haul.setTotal(object.getString(Konfigurasi.KEY_TOTAL));
                        haulList.add(haul);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LaporanActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    haulAdapter = new HaulAdapter(LaporanActivity.this, haulList);
                    recyclerView.setAdapter(haulAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_LAPORAN_HAUL);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }
}