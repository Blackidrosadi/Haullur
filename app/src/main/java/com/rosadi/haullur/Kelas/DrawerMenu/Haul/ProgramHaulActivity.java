package com.rosadi.haullur.Kelas.DrawerMenu.Haul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.List.Adapter.HaulAdapter;
import com.rosadi.haullur.List.Model.Haul;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProgramHaulActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    List<Haul> haulList = new ArrayList<>();
    HaulAdapter haulAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_haul);

        recyclerView = findViewById(R.id.recycler_view);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.tambah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekProgramHaul();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProgramHaulActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        haulAdapter = new HaulAdapter(ProgramHaulActivity.this, haulList);
        recyclerView.setAdapter(haulAdapter);

        loadHaul();
    }

    private void cekProgramHaul() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ProgramHaulActivity.this, "Informasi", "Memeriksa program haul...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.trim().equals("1")) {
                    Toast.makeText(ProgramHaulActivity.this, "Selesaikan haul sebelumnya sebelum menambah program haul baru!", Toast.LENGTH_SHORT).show();
                } else {
                    dialogTambahProgram();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_CEK_PROGRAM_HAUL);
                return s;
            }


        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void dialogTambahProgram() {
        Dialog dialog = new Dialog(ProgramHaulActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_program);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView tanggalTV = dialog.findViewById(R.id.tanggal);
        EditText deskripsiET = dialog.findViewById(R.id.deskripsi);

        Calendar c = Calendar.getInstance();
        int tahun = c.get(Calendar.YEAR);
        int bulan = c.get(Calendar.MONTH);
        int tanggal = c.get(Calendar.DAY_OF_MONTH);

        dialog.findViewById(R.id.pilih_tanggal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ProgramHaulActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date date = simpleDateFormat.parse(i2 + "-" + (i1 + 1) + "-" + i);
                                    Locale local = new Locale("id", "id");
                                    simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", local);
                                    tanggalTV.setText(""+simpleDateFormat.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        tahun, bulan, tanggal
                );
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tanggalTV.getText().toString().equals("Pilih Tanggal")) {
                    Toast.makeText(ProgramHaulActivity.this, "Tanggal program haul belum dipilih!", Toast.LENGTH_SHORT).show();
                } else {
                    if (deskripsiET.getText().toString().equals("")) {
                            tambahProgramHaul(dialog, tanggalTV.getText().toString(), "Haul Jemuah Legi");
                    } else {
                        tambahProgramHaul(dialog, tanggalTV.getText().toString(), deskripsiET.getText().toString());
                    }
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

    public void loadHaul() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ProgramHaulActivity.this, "Informasi", "Memuat data program haul...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                haulList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
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

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProgramHaulActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    haulAdapter = new HaulAdapter(ProgramHaulActivity.this, haulList);
                    recyclerView.setAdapter(haulAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_PROGRAM_HAUL);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void tambahProgramHaul(Dialog dialog, String tanggal, String deskripsi) {
        class TambahProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(ProgramHaulActivity.this, "Informasi", "Proses menambahkan...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                loadHaul();

                Toast.makeText(ProgramHaulActivity.this, s, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_TANGGAL, tanggal);
                hashMap.put(Konfigurasi.KEY_DESKRIPSI, deskripsi);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TAMBAH_PROGRAM_HAUL, hashMap);

                return s;
            }
        }

        TambahProses tambahProses = new TambahProses();
        tambahProses.execute();
    }
}