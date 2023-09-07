package com.rosadi.haullur.Laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.rosadi.haullur.Haul.ProgramHaulActivity;
import com.rosadi.haullur.List.Adapter.HaulAdapter;
import com.rosadi.haullur.List.Model.Haul;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LaporanActivity extends AppCompatActivity {

    List<Haul> haulList = new ArrayList<>();
    HaulAdapter haulAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LaporanActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        haulAdapter = new HaulAdapter(LaporanActivity.this, haulList);
        recyclerView.setAdapter(haulAdapter);

//        loadHaul();
    }

//    private void loadHaul() {
//        class LoadData extends AsyncTask<Void, Void, String> {
//
//            ProgressDialog progressDialog;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                progressDialog = ProgressDialog.show(LaporanActivity.this, "Informasi", "Memuat data program haul...", false, false);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                progressDialog.dismiss();
//                haulList.clear();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
//                    for (int i = 0; i < result.length(); i++) {
//                        JSONObject object = result.getJSONObject(i);
//
//                        Haul haul = new Haul();
//                        haul.setId(object.getString(Konfigurasi.KEY_ID));
//                        haul.setDeskripsi(object.getString(Konfigurasi.KEY_DESKRIPSI));
//                        haul.setTanggal(object.getString(Konfigurasi.KEY_TANGGAL));
//                        haul.setStatus(object.getString(Konfigurasi.KEY_STATUS));
//                        haulList.add(haul);
//                    }
//
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProgramHaulActivity.this);
//                    recyclerView.setLayoutManager(linearLayoutManager);
//                    haulAdapter = new HaulAdapter(ProgramHaulActivity.this, haulList);
//                    recyclerView.setAdapter(haulAdapter);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                RequestHandler rh = new RequestHandler();
//                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_PROGRAM_HAUL);
//                return s;
//            }
//        }
//
//        LoadData loadData = new LoadData();
//        loadData.execute();
//    }
}