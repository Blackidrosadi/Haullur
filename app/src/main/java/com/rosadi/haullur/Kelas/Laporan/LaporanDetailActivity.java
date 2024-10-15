package com.rosadi.haullur.Kelas.Laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Akun.LoginActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.Artikel.ArtikelActivity;
import com.rosadi.haullur.Kelas.Laporan.Fragment.PemasukanFragment;
import com.rosadi.haullur.Kelas.Laporan.Fragment.PengeluaranFragment;
import com.rosadi.haullur.List.Adapter.KeluargaAdapter;
import com.rosadi.haullur.List.Model.Keluarga;
import com.rosadi.haullur.MainActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LaporanDetailActivity extends AppCompatActivity {

    SharedPreferences preferences;

    TextView tanggalTV, totalTV, textPemasukanTV, textPengeluaranTV;
    View barPemasukan, barPengeluaran;
    RelativeLayout tabPemasukan, tabPengeluaran;

    public Dialog dialogPilihKeluarga;
    public String idKeluarga = "";
    public RelativeLayout pilihKeluargaRL, pilihanKeluargaRL;
    public TextView totalAlmarhumTV, namaTV, rtTV;

    String idHaul;
    List<Keluarga> keluargaList = new ArrayList<>();
    KeluargaAdapter keluargaAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_detail);

        preferences = getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, 0);
        if (preferences == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            LaporanDetailActivity.this.finish();
        }

        Intent i = getIntent();
        idHaul = i.getStringExtra("id");

        tanggalTV = findViewById(R.id.tanggal);
        totalTV = findViewById(R.id.totaldana);
        textPemasukanTV = findViewById(R.id.text_pemasukan);
        textPengeluaranTV = findViewById(R.id.text_pengeluaran);
        barPemasukan = findViewById(R.id.bar_pemasukan);
        barPengeluaran = findViewById(R.id.bar_pengeluaran);
        tabPemasukan = findViewById(R.id.tab_pemasukan);
        tabPengeluaran = findViewById(R.id.tab_pengeluaran);

        tanggalTV.setText(i.getStringExtra("tanggal"));
        totalTV.setText(rupiahFormat(i.getStringExtra("total")) + ",-");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new PemasukanFragment(idHaul))
                .commit();

        textPemasukanTV.setTextColor(getResources().getColor(R.color.white));
        barPemasukan.setVisibility(View.VISIBLE);
        tabPemasukan.setBackground(getResources().getDrawable(R.drawable.bg_tab_rounded_left_primary_dark));

        textPengeluaranTV.setTextColor(getResources().getColor(R.color.white_50));
        barPengeluaran.setVisibility(View.GONE);
        tabPengeluaran.setBackground(null);

        tabPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new PemasukanFragment(idHaul))
                        .commit();

                textPemasukanTV.setTextColor(getResources().getColor(R.color.white));
                barPemasukan.setVisibility(View.VISIBLE);
                tabPemasukan.setBackground(getResources().getDrawable(R.drawable.bg_tab_rounded_left_primary_dark));

                textPengeluaranTV.setTextColor(getResources().getColor(R.color.white_50));
                barPengeluaran.setVisibility(View.GONE);
                tabPengeluaran.setBackground(null);
            }
        });

        tabPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new PengeluaranFragment(idHaul))
                        .commit();

                textPemasukanTV.setTextColor(getResources().getColor(R.color.white_50));
                barPemasukan.setVisibility(View.GONE);
                tabPemasukan.setBackground(null);

                textPengeluaranTV.setTextColor(getResources().getColor(R.color.white));
                barPengeluaran.setVisibility(View.VISIBLE);
                tabPengeluaran.setBackground(getResources().getDrawable(R.drawable.bg_tab_rounded_right_primary_dark));
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.tambah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(preferences.getString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, null), "0")) {
                    Toast.makeText(LaporanDetailActivity.this, "Selain admin dan petugas tidak bisa mengakses menu ini!", Toast.LENGTH_SHORT).show();
                } else {
                    openDialogTransaksi();
                }
            }
        });
    }

    private void openDialogTransaksi() {
        Dialog dialog = new Dialog(LaporanDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_laporan_pilih_transaksi);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.danalainnya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogDanaLainnya();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.pengeluaran).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogPengeluaran();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogPengeluaran() {
        Dialog dialog = new Dialog(LaporanDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pengeluaran_dana);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        EditText deskripsi = dialog.findViewById(R.id.deskripsi);
        EditText jumlahET = dialog.findViewById(R.id.jumlah);
        setRupiahFormat(jumlahET);

        dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deskripsi.getText().toString().trim().equals("")) {
                    Toast.makeText(LaporanDetailActivity.this, "Masukkan Deskripsi!", Toast.LENGTH_SHORT).show();
                } else if (jumlahET.getText().toString().equals("") || jumlahET.getText().toString().equals("0")) {
                    Toast.makeText(LaporanDetailActivity.this, "Masukkan jumlah dana!", Toast.LENGTH_SHORT).show();
                } else {
                    int jumlah = Integer.parseInt(jumlahET.getText().toString().replace(".", ""));
                    prosesTambahPengeluaranDana(dialog, deskripsi.getText().toString(), jumlah);
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

    private void openDialogDanaLainnya() {
        Dialog dialog = new Dialog(LaporanDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tambah_dana_lainnya);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        pilihKeluargaRL = dialog.findViewById(R.id.pilih_keluarga);
        LinearLayout deskripsiLL = dialog.findViewById(R.id.deskripsiLL);

        pilihanKeluargaRL = dialog.findViewById(R.id.pilihan_keluarga);
        totalAlmarhumTV = dialog.findViewById(R.id.total_almarhums);
        namaTV = dialog.findViewById(R.id.nama);
        rtTV = dialog.findViewById(R.id.rt);

        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.radio_dari_keluarga) {
                    pilihKeluargaRL.setVisibility(View.VISIBLE);
                    deskripsiLL.setVisibility(View.GONE);
                } else if (id == R.id.radio_sumbangan) {
                    pilihKeluargaRL.setVisibility(View.GONE);
                    deskripsiLL.setVisibility(View.VISIBLE);

                    idKeluarga = "";
                    pilihanKeluargaRL.setVisibility(View.GONE);
                }
            }
        });

        dialog.findViewById(R.id.pilih_keluarga).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogPilihKeluarga();
            }
        });

        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idKeluarga = "";
                pilihanKeluargaRL.setVisibility(View.GONE);

                pilihKeluargaRL.setVisibility(View.VISIBLE);
            }
        });

        EditText jumlahET = dialog.findViewById(R.id.jumlah);
        setRupiahFormat(jumlahET);
        EditText deskripsi = dialog.findViewById(R.id.deskripsi);

        dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_dari_keluarga) {
                    if (idKeluarga.equals("")) {
                        Toast.makeText(LaporanDetailActivity.this, "Pilih keluarga!", Toast.LENGTH_SHORT).show();
                    } else if (jumlahET.getText().toString().equals("") || jumlahET.getText().toString().equals("0")) {
                        Toast.makeText(LaporanDetailActivity.this, "Masukkan jumlah dana!", Toast.LENGTH_SHORT).show();
                    } else {
                        int jumlah = Integer.parseInt(jumlahET.getText().toString().replace(".", ""));
                        prosesTambahDanaLainnya(dialog, "", jumlah);
                        dialog.dismiss();
                    }
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_sumbangan) {
                    if (deskripsi.getText().length() == 0 || deskripsi.getText().toString().trim().equals("")) {
                        Toast.makeText(LaporanDetailActivity.this, "Masukkan deskripsi!", Toast.LENGTH_SHORT).show();
                    } else if (deskripsi.getText().length() > 200) {
                        Toast.makeText(LaporanDetailActivity.this, "Deskripsi maksimal 200 karakter!", Toast.LENGTH_SHORT).show();
                    } else if (jumlahET.getText().toString().equals("") || jumlahET.getText().toString().equals("0")) {
                        Toast.makeText(LaporanDetailActivity.this, "Masukkan jumlah dana!", Toast.LENGTH_SHORT).show();
                    } else {
                        idKeluarga = "";
                        int jumlah = Integer.parseInt(jumlahET.getText().toString().replace(".", ""));
                        prosesTambahDanaLainnya(dialog, deskripsi.getText().toString(), jumlah);
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(LaporanDetailActivity.this, "Tipe tambahan dana belum dipilih!", Toast.LENGTH_SHORT).show();
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

    private void prosesTambahDanaLainnya(Dialog dialog, String deskripsi, int jumlah) {
        class ProsesTambah extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(LaporanDetailActivity.this, "Informasi", "Proses menambahkan dana...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                progressDialog.dismiss();

                Toast.makeText(LaporanDetailActivity.this, s, Toast.LENGTH_SHORT).show();

                loadTotalDana();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new PemasukanFragment(idHaul))
                        .commit();

                textPemasukanTV.setTextColor(getResources().getColor(R.color.white));
                barPemasukan.setVisibility(View.VISIBLE);
                tabPemasukan.setBackground(getResources().getDrawable(R.drawable.bg_tab_rounded_left_primary_dark));

                textPengeluaranTV.setTextColor(getResources().getColor(R.color.white_50));
                barPengeluaran.setVisibility(View.GONE);
                tabPengeluaran.setBackground(null);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID_HAUL, idHaul);
                hashMap.put(Konfigurasi.KEY_JUMLAH_UANG, String.valueOf(jumlah));
                if (deskripsi.equals("")) {
                    hashMap.put(Konfigurasi.KEY_ID_KELUARGA, idKeluarga);
                    hashMap.put(Konfigurasi.KEY_DESKRIPSI, "");
                } else {
                    hashMap.put(Konfigurasi.KEY_ID_KELUARGA, "");
                    hashMap.put(Konfigurasi.KEY_DESKRIPSI, deskripsi);
                }
                hashMap.put(Konfigurasi.KEY_ID_AKUN, "0");

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TRANSAKSI_TAMBAH_DANA_LAINNYA, hashMap);

                return s;
            }
        }

        ProsesTambah prosesTambah = new ProsesTambah();
        prosesTambah.execute();
    }

    private void prosesTambahPengeluaranDana(Dialog dialog, String deskripsi, int jumlah) {
        class ProsesTambah extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(LaporanDetailActivity.this, "Informasi", "Proses transaksi pengeluaran dana...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                progressDialog.dismiss();

                Toast.makeText(LaporanDetailActivity.this, s, Toast.LENGTH_SHORT).show();

                loadTotalDana();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new PengeluaranFragment(idHaul))
                        .commit();

                textPemasukanTV.setTextColor(getResources().getColor(R.color.white_50));
                barPemasukan.setVisibility(View.GONE);
                tabPemasukan.setBackground(null);

                textPengeluaranTV.setTextColor(getResources().getColor(R.color.white));
                barPengeluaran.setVisibility(View.VISIBLE);
                tabPengeluaran.setBackground(getResources().getDrawable(R.drawable.bg_tab_rounded_right_primary_dark));
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID_HAUL, idHaul);
                hashMap.put(Konfigurasi.KEY_DESKRIPSI, deskripsi);
                hashMap.put(Konfigurasi.KEY_JUMLAH_UANG, String.valueOf(jumlah));

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TRANSAKSI_PENGELUARAN_DANA, hashMap);

                return s;
            }
        }

        ProsesTambah prosesTambah = new ProsesTambah();
        prosesTambah.execute();
    }

    public void loadTotalDana() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String totaldanaNya = jsonObject.getString("total_dana");
                    totalTV.setText(rupiahFormat(totaldanaNya) + ",-");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_LAPORAN_HAUL_TOTAL_DANA, idHaul);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void openDialogPilihKeluarga() {
        dialogPilihKeluarga = new Dialog(LaporanDetailActivity.this);
        dialogPilihKeluarga.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPilihKeluarga.setContentView(R.layout.dialog_pilih_keluarga);
        dialogPilihKeluarga.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogPilihKeluarga.setCancelable(false);

        RecyclerView recyclerView = dialogPilihKeluarga.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LaporanDetailActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        keluargaAdaper = new KeluargaAdapter(LaporanDetailActivity.this, keluargaList);
        recyclerView.setAdapter(keluargaAdaper);

        loadPilihanKeluarga(recyclerView);

        EditText cariET = dialogPilihKeluarga.findViewById(R.id.cari);
        cariET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cariPilihanKeluarga(recyclerView, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialogPilihKeluarga.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPilihKeluarga.dismiss();
            }
        });

        dialogPilihKeluarga.show();
    }

    private void cariPilihanKeluarga(RecyclerView recyclerView, String cari) {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
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
                        keluarga.setjumlahAlmarhum(object.getString(Konfigurasi.KEY_JUMLAH_ALMARHUM));
                        keluargaList.add(keluarga);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LaporanDetailActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaAdaper = new KeluargaAdapter(LaporanDetailActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaAdaper);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_CARI_KELUARGA_LIMIT_30, cari);
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
    }

    private void loadPilihanKeluarga(RecyclerView recyclerView) {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(LaporanDetailActivity.this, "Informasi", "Memuat data keluarga...", false, false);
                progressDialog.setCancelable(true);
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
                        keluarga.setjumlahAlmarhum(object.getString(Konfigurasi.KEY_JUMLAH_ALMARHUM));
                        keluargaList.add(keluarga);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LaporanDetailActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaAdaper = new KeluargaAdapter(LaporanDetailActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaAdaper);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_KELUARGA_LIMIT_30);
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
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

    private void setRupiahFormat(EditText edt) {
        edt.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    edt.removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance()
                                    .getCurrency()
                                    .getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable,
                            "");

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

                    current = formatted;
                    edt.setText(clean);
                    edt.setSelection(clean.length());
                    edt.addTextChangedListener(this);
                }
            }
        });
    }
}