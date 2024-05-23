package com.rosadi.haullur.Kelas.Akun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Akun.LoginActivity;
import com.rosadi.haullur.Kelas.Penarikan.PenarikanActivity;
import com.rosadi.haullur.List.Adapter.KeluargaAdapter;
import com.rosadi.haullur.List.Adapter.KeluargaByAkunAdapter;
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
import java.util.Locale;

public class DetailAkunActivity extends AppCompatActivity {

    String idAkun, nama, email, telepon, sandi, level;
    ArrayList<Keluarga> keluargaList = new ArrayList<>();
    KeluargaAdapter keluargaAdapter;
    KeluargaByAkunAdapter keluargaByAkunAdapter;

    TextView namaTV, emailTV, teleponTV, jumlahPenugasan, jumlahPenarikan, totalPenarikan;
    RecyclerView recyclerView;
    PopupMenu popupMenu;

    public String idKeluarga = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_akun);

        recyclerView = findViewById(R.id.recycler_view);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent i = getIntent();
        idAkun = i.getStringExtra(Konfigurasi.KEY_ID);
        nama = i.getStringExtra(Konfigurasi.KEY_NAMA);
        email = i.getStringExtra(Konfigurasi.KEY_EMAIL);
        telepon = i.getStringExtra(Konfigurasi.KEY_TELEPON);
        sandi = i.getStringExtra(Konfigurasi.KEY_SANDI);
        level = i.getStringExtra(Konfigurasi.KEY_LEVEL);

        namaTV = findViewById(R.id.nama);
        emailTV = findViewById(R.id.email);
        teleponTV = findViewById(R.id.telepon);

        namaTV.setText(nama);
        if (email.equals("")) {
            findViewById(R.id.ic_email).setVisibility(View.GONE);
            emailTV.setVisibility(View.GONE);
        } else {
            emailTV.setText(email);
        }
        teleponTV.setText(telepon);

        jumlahPenugasan = findViewById(R.id.jumlah_penugasan);
        jumlahPenarikan = findViewById(R.id.jumlah_penarikan);
        totalPenarikan = findViewById(R.id.total);
        jumlahPenarikan.setText("0 Penarikan");
        totalPenarikan.setText("0");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailAkunActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        keluargaByAkunAdapter = new KeluargaByAkunAdapter(DetailAkunActivity.this, keluargaList);
        recyclerView.setAdapter(keluargaByAkunAdapter);

        loadKeluargaByPetugas();
        loadPenarikanByUser();

        RelativeLayout pengaturan = findViewById(R.id.pengaturan);
        popupMenu = new PopupMenu(DetailAkunActivity.this, pengaturan);
        popupMenu.getMenuInflater().inflate(R.menu.menu_akun, popupMenu.getMenu());
        if (level.equals("1")) {
            popupMenu.getMenu().getItem(1).setTitle("Hapus dari Admin");
        }

        pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.pesan:
                                kirimPesanWhatsApp();
                                return true;

                            case R.id.admin:
                                if (level.equals("1")) {
                                    openDialogJadikanAdmin(2);
                                } else {
                                    openDialogJadikanAdmin(1);
                                }
                                return true;

                            case R.id.reset:
                                if (sandi.equals("123")) {
                                    Toast.makeText(DetailAkunActivity.this, "Password telah direset!", Toast.LENGTH_SHORT).show();
                                } else {
                                    openDialogResetPassword();
                                }
                                return true;

                            case R.id.hapus:
                                if (level.equals("1")) {
                                    Toast.makeText(DetailAkunActivity.this, "Tidak bisa menghapus akun admin!", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (keluargaList.size() > 0) {
                                        Toast.makeText(DetailAkunActivity.this, "Tidak dapat menghapus akun karena akun masih ada tugas penarikan!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        openDialogHapusAkun();
                                    }
                                }
                                return true;
                        }

                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        findViewById(R.id.tambah).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(DetailAkunActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_tambah_penugasan);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);
                EditText cari = dialog.findViewById(R.id.cari);

                cari.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        cariKeluargaFree(recyclerView, charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailAkunActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                keluargaAdapter = new KeluargaAdapter(DetailAkunActivity.this, keluargaList);
                recyclerView.setAdapter(keluargaAdapter);

                loadKeluargaFree(recyclerView);

                dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (idKeluarga.equals("")) {
                            Toast.makeText(DetailAkunActivity.this, "Pilih keluarga untuk menambahkan!", Toast.LENGTH_SHORT).show();
                        } else {
                            tambahPenugasan();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        idKeluarga = "";

                        loadKeluargaByPetugas();
                    }
                });

                dialog.show();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Dialog dialog = new Dialog(DetailAkunActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_iya_tidak);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                TextView judul = dialog.findViewById(R.id.judul);
                TextView teks = dialog.findViewById(R.id.teks);
                TextView teksiya = dialog.findViewById(R.id.teksiya);

                int position = viewHolder.getAdapterPosition();
                Keluarga keluarga = keluargaList.get(position);

                judul.setText("Hapus Tugas Penarikan");
                teks.setText("Apa Antum yakin ingin menghapus tugas penarikan ke keluarga " + keluarga.getNama() + " ?");
                teksiya.setText("Hapus");

                dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hapusPenugasan(keluarga.getId());

                        keluargaList.remove(position);
                        jumlahPenugasan.setText(keluargaList.size() + " Keluarga");
                        keluargaByAkunAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        keluargaByAkunAdapter.notifyDataSetChanged();
                    }
                });

                dialog.show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadPenarikanByUser() {
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
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    JSONObject data = result.getJSONObject(0);

                    String jumlah = data.getString(Konfigurasi.KEY_JUMLAH);
                    String total = data.getString(Konfigurasi.KEY_TOTAL);

                    jumlahPenarikan.setText(jumlah + " Penarikan");
                    totalPenarikan.setText(rupiahFormat(total));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_PENARIKAN_AKUN, idAkun);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void openDialogHapusAkun() {
        Dialog dialog = new Dialog(DetailAkunActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("Hapus Akun");
        teks.setText("Apakah anda yakin ingin menghapus akun " + nama + " ?");
        teksiya.setText("Iya");

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                prosesHapusAkun();
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

    private void prosesHapusAkun() {
        class HapusProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DetailAkunActivity.this, "Informasi", "Proses menghapus...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                Toast.makeText(DetailAkunActivity.this, s, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DetailAkunActivity.this, AkunActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                DetailAkunActivity.this.finish();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, idAkun);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_AKUN, hashMap);

                return s;
            }
        }

        HapusProses hapusProses = new HapusProses();
        hapusProses.execute();
    }

    private void openDialogResetPassword() {
        Dialog dialog = new Dialog(DetailAkunActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("Reset Password Akun");
        teks.setText("Apakah anda yakin ingin mereset password " + nama + " ?");
        teksiya.setText("Iya");

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                prosesResetSandi();
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

    private void prosesResetSandi() {
        class ProsesUpdate extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DetailAkunActivity.this, "Informasi", "Proses mereset...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah sandi berhasil direset")) {
                    sandi = "123";
                }

                Toast.makeText(DetailAkunActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, idAkun);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_RESET_SANDI, hashMap);

                return s;
            }
        }

        ProsesUpdate prosesUpdate = new ProsesUpdate();
        prosesUpdate.execute();
    }

    private void openDialogJadikanAdmin(int levelBaru) {
        Dialog dialog = new Dialog(DetailAkunActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        if (level.equals("1")) {
            judul.setText("Hapus dari status Admin");
            teks.setText("Apakah anda yakin ingin menjadikan " + nama + " sebagai petugas biasa ?");
            teksiya.setText("Iya");
        } else {
            judul.setText("Jadikan sebagai Admin");
            teks.setText("Apakah anda yakin ingin menjadikan " + nama + " sebagai admin ?");
            teksiya.setText("Iya");
        }

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                prosesJadikanAdmin(levelBaru);
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

    private void prosesJadikanAdmin(int levelBaru) {
        class ProsesUpdate extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DetailAkunActivity.this, "Informasi", "Proses mengubah...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah akun berhasil diperbarui")) {
                    level = String.valueOf(levelBaru);
                }
                Toast.makeText(DetailAkunActivity.this, s, Toast.LENGTH_SHORT).show();

                if (level.equals("1")) {
                    popupMenu.getMenu().getItem(1).setTitle("Hapus dari Admin");
                } else {
                    popupMenu.getMenu().getItem(1).setTitle("Jadikan Admin");
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, idAkun);
                hashMap.put(Konfigurasi.KEY_LEVEL, String.valueOf(levelBaru));

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_JADIKAN_ADMIN, hashMap);

                return s;
            }
        }

        ProsesUpdate prosesUpdate = new ProsesUpdate();
        prosesUpdate.execute();
    }

    private void kirimPesanWhatsApp() {
        if (telepon.isEmpty()) {
            Toast.makeText(this, "Nomor WhatsApp belum ditambahkan!", Toast.LENGTH_SHORT).show();
        } else {
            String teleponNya = "+62" + telepon;
            String pesanNya = "Assalamu'alaikum...";

            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s", teleponNya, pesanNya)
                    )
            );
            this.startActivity(i);
        }
    }

    private void hapusPenugasan(String id) {
        class HapusProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DetailAkunActivity.this, "Informasi", "Proses menghapus...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                Toast.makeText(DetailAkunActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_PENUGASAN, hashMap);

                return s;
            }
        }

        HapusProses hapusProses = new HapusProses();
        hapusProses.execute();
    }

    private void cariKeluargaFree(RecyclerView recyclerView, String cari) {
        class CariData extends AsyncTask<Void, Void, String> {

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

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailAkunActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaAdapter = new KeluargaAdapter(DetailAkunActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler handler = new RequestHandler();
                String s = handler.sendGetRequestParam(Konfigurasi.URL_CARI_KELUARGA_FREE, cari);
                return s;
            }
        }

        CariData cariData = new CariData();
        cariData.execute();
    }

    private void loadKeluargaFree(RecyclerView recyclerView) {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DetailAkunActivity.this, "Informasi", "Memuat data keluarga...", false, false);
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

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailAkunActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaAdapter = new KeluargaAdapter(DetailAkunActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_LOAD_KELUARGA_FREE);
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
    }

    private void tambahPenugasan() {
        class TambahProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(DetailAkunActivity.this, "Informasi", "Proses menambahkan...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                loadKeluargaByPetugas();

                Toast.makeText(DetailAkunActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID_KELUARGA, idKeluarga);
                hashMap.put(Konfigurasi.KEY_ID_AKUN, idAkun);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TAMBAH_PENUGASAN, hashMap);

                return s;
            }
        }

        TambahProses tambahProses = new TambahProses();
        tambahProses.execute();
    }

    private void loadKeluargaByPetugas() {
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

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailAkunActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    keluargaByAkunAdapter = new KeluargaByAkunAdapter(DetailAkunActivity.this, keluargaList);
                    recyclerView.setAdapter(keluargaByAkunAdapter);

                    jumlahPenugasan.setText(keluargaList.size() + " Keluarga");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_KELUARGA_BY_PETUGAS, idAkun);
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
    }

    private String rupiahFormat(String jumlah) {
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
}