package com.rosadi.haullur.Kelas.DrawerMenu.Artikel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.zelory.compressor.Compressor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Kelas.DrawerMenu.Haul.ProgramHaulActivity;
import com.rosadi.haullur.Kelas.Laporan.LaporanActivity;
import com.rosadi.haullur.List.Adapter.HaulAdapter;
import com.rosadi.haullur.List.Adapter.PilihHaulAdapter;
import com.rosadi.haullur.List.Model.Haul;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.FileUtil;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.Util;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TambahArtikelLaporanActivity extends AppCompatActivity {


    RelativeLayout tambahFoto;
    ImageView imageViewTamnel, imageView, imageView2;
    LinearLayout simpan;

    Bitmap foto_tamnel = null, foto = null, foto2 = null;
    String jumlahKeluarga, jumlahAlmarhums;

    private static final int KODE_FOTO_TAMNEL = 12;
    private static final int KODE_FOTO_PERTAMA = 1;
    private static final int KODE_FOTO_KEDUA = 2;

    RecyclerView recyclerViewListHaul;
    List<Haul> haulList = new ArrayList<>();
    PilihHaulAdapter pilihHaulAdapter;

    public CheckBox checkBoxTemplate;
    public LinearLayout pilihHaul, templateLaporan, artikelForm;
    public Dialog dialogPilihanHaul;
    public String idHaul = "", totalDanaHaul = "";
    public RelativeLayout pilihanHaul;
    public TextView deskripsiHaul, tanggalHaul;
    public EditText judul, lokasi, deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_artikel_laporan);

        pilihHaul = findViewById(R.id.pilih_haul);
        pilihanHaul = findViewById(R.id.pilihan_haul);
        deskripsiHaul = findViewById(R.id.deskripsi_haul);
        tanggalHaul = findViewById(R.id.tanggal_haul);
        templateLaporan = findViewById(R.id.template_laporan);
        checkBoxTemplate = findViewById(R.id.checkbox);
        artikelForm = findViewById(R.id.artikelForm);
        imageViewTamnel = findViewById(R.id.foto_tamnel);
        judul = findViewById(R.id.judul);
        lokasi = findViewById(R.id.lokasi);
        deskripsi = findViewById(R.id.deskripsi);
        tambahFoto = findViewById(R.id.tambah_foto);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        simpan = findViewById(R.id.simpan);

        templateLaporan.setVisibility(View.GONE);
        artikelForm.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pilihHaul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogListHaul();
            }
        });

        pilihanHaul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogListHaul();
            }
        });

        checkBoxTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!idHaul.equals("")) {
                    if (checkBoxTemplate.isChecked()) {
                        loadJumlahKeluargaDanJumlahAlmarhums();
                    } else if (!checkBoxTemplate.isChecked()) {
                        judul.setText("");
                        lokasi.setText("");
                        deskripsi.setText("");
                    }
                } else {
                    Toast.makeText(TambahArtikelLaporanActivity.this, "Pilih program haul terlebih dahulu untuk menggunakan template!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageViewTamnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), KODE_FOTO_TAMNEL);
            }
        });

        tambahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foto == null && foto2 == null) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), KODE_FOTO_PERTAMA);
                } else if (foto != null) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), KODE_FOTO_KEDUA);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foto != null) {
                    openDialogUbahFoto(KODE_FOTO_PERTAMA);
                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foto2 != null) {
                    openDialogUbahFoto(KODE_FOTO_KEDUA);
                }
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foto_tamnel == null) {
                    Toast.makeText(TambahArtikelLaporanActivity.this, "Tambahkan foto artikel!", Toast.LENGTH_SHORT).show();
                } else if (judul.getText().toString().isEmpty()) {
                    Toast.makeText(TambahArtikelLaporanActivity.this, "Isikan judul artikel!", Toast.LENGTH_SHORT).show();
                } else if (lokasi.getText().toString().isEmpty()) {
                    Toast.makeText(TambahArtikelLaporanActivity.this, "Isikan lokasi!", Toast.LENGTH_SHORT).show();
                } else if (deskripsi.getText().toString().isEmpty()) {
                    Toast.makeText(TambahArtikelLaporanActivity.this, "Isikan deskripsi artikel!", Toast.LENGTH_SHORT).show();
                } else {
                    tambahArtikelLaporan();
                }
            }
        });
    }

    private void tambahArtikelLaporan() {
        class ProsesTambah extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(TambahArtikelLaporanActivity.this, "Informasi", "Proses menambahkan...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah berhasil menambahkan artikel")) {
                    TambahArtikelLaporanActivity.this.finish();
                    Intent i = new Intent(TambahArtikelLaporanActivity.this, ArtikelActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                Toast.makeText(TambahArtikelLaporanActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                Date date = Calendar.getInstance().getTime();
                Locale local = new Locale("id", "id");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", local);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_FOTO_TAMNEL, getStringImage(foto_tamnel));
                hashMap.put(Konfigurasi.KEY_JUDUL, judul.getText().toString());
                hashMap.put(Konfigurasi.KEY_LOKASI, lokasi.getText().toString());
                hashMap.put(Konfigurasi.KEY_TANGGAL, "" + simpleDateFormat.format(date));
                hashMap.put(Konfigurasi.KEY_DESKRIPSI, deskripsi.getText().toString());
                hashMap.put(Konfigurasi.KEY_ID_HAUL, idHaul);
                hashMap.put(Konfigurasi.KEY_FOTO, getStringImage(foto));
                hashMap.put(Konfigurasi.KEY_FOTO_2, getStringImage(foto2));

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TAMBAH_ARTIKEL_LAPORAN, hashMap);
                return s;
            }
        }

        ProsesTambah prosesTambah = new ProsesTambah();
        prosesTambah.execute();
    }

    public String getStringImage(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } else {
            return "";
        }
    }

    private void openDialogListHaul() {
        dialogPilihanHaul = new Dialog(TambahArtikelLaporanActivity.this);
        dialogPilihanHaul.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPilihanHaul.setContentView(R.layout.dialog_pilih_haul);
        dialogPilihanHaul.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogPilihanHaul.setCancelable(true);

        recyclerViewListHaul = dialogPilihanHaul.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewListHaul.setLayoutManager(linearLayoutManager);
        pilihHaulAdapter = new PilihHaulAdapter(this, haulList);
        recyclerViewListHaul.setAdapter(pilihHaulAdapter);

        loadHaul();

        dialogPilihanHaul.show();
    }

    private void openDialogUbahFoto(int kode) {
        Dialog dialog = new Dialog(TambahArtikelLaporanActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ubah_foto_laporan);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.ganti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), kode);
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.hapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kode == KODE_FOTO_PERTAMA) {
                    if (foto2 != null) {
                        foto = foto2;
                        foto2 = null;

                        imageView.setImageBitmap(foto);
                        imageView2.setVisibility(View.GONE);
                        tambahFoto.setVisibility(View.VISIBLE);
                    } else {
                        foto = null;
                        imageView.setVisibility(View.GONE);
                        tambahFoto.setVisibility(View.VISIBLE);
                    }
                } else if (kode == KODE_FOTO_KEDUA) {
                    foto2 = null;
                    imageView2.setVisibility(View.GONE);
                    tambahFoto.setVisibility(View.VISIBLE);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KODE_FOTO_TAMNEL && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Gagal mengambil foto!", Toast.LENGTH_SHORT).show();
            }

            try {
                File fotoAsli = FileUtil.from(this, data.getData());
                foto_tamnel = new Compressor(this).compressToBitmap(fotoAsli);

                imageViewTamnel.setImageBitmap(foto_tamnel);
            } catch (IOException e) {
                Toast.makeText(this, "Gagal membaca data foto yang dipilih!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (requestCode == KODE_FOTO_PERTAMA && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Gagal mengambil foto!", Toast.LENGTH_SHORT).show();
            }

            try {
                File fotoAsli = FileUtil.from(this, data.getData());
                foto = new Compressor(this).compressToBitmap(fotoAsli);

                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(foto);
            } catch (IOException e) {
                Toast.makeText(this, "Gagal membaca data foto yang dipilih!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        if (requestCode == KODE_FOTO_KEDUA && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Gagal mengambil foto!", Toast.LENGTH_SHORT).show();
            }

            try {
                File fotoAsli = FileUtil.from(this, data.getData());
                foto2 = new Compressor(this).compressToBitmap(fotoAsli);

                imageView2.setVisibility(View.VISIBLE);
                imageView2.setImageBitmap(foto2);

                tambahFoto.setVisibility(View.GONE);
            } catch (IOException e) {
                Toast.makeText(this, "Gagal membaca data foto yang dipilih!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void loadHaul() {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(TambahArtikelLaporanActivity.this, "Informasi", "Memuat daftar program haul...", false, false);
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

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TambahArtikelLaporanActivity.this);
                    recyclerViewListHaul.setLayoutManager(linearLayoutManager);
                    pilihHaulAdapter = new PilihHaulAdapter(TambahArtikelLaporanActivity.this, haulList);
                    recyclerViewListHaul.setAdapter(pilihHaulAdapter);

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

    private void loadJumlahKeluargaDanJumlahAlmarhums() {
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
                    JSONObject object = result.getJSONObject(0);
                    jumlahKeluarga = object.getString("jumlah_keluarga");
                    jumlahAlmarhums = object.getString("jumlah_almarhums");

                    judul.setText("Laporan " + deskripsiHaul.getText().toString() + " pada " + tanggalHaul.getText().toString().trim());
                    lokasi.setText("Musholla Baiturrahman");
                    deskripsi.setText("Alhamdulillahirabbilalamin, acara " + deskripsiHaul.getText().toString().trim() + " yang dilaksanakan " +
                            "pada hari " + tanggalHaul.getText().toString().trim() + " berjalan dengan lancar. Acara ini diikuti oleh " +
                            jumlahKeluarga + " keluarga dengan " + jumlahAlmarhums + " almarhum/almarhumah. Total dana " +
                            "yang telah dikumpulkan sebanyak Rp" + Util.rupiahFormat(totalDanaHaul) + ".\n\n" +
                            "Semoga almarhum - almarhumah husnul khotimah diampuni segala dosa, ditempatkan di tempat yang tinggi di sisi Allah " +
                            "dan keluarga yang ditinggalkan diberikan kesabaran.. Amin Allahuma Amiiiin.");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_JUMLAH_KELUARGA_DAN_ALMARHUMS, idHaul);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }
}