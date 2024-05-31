package com.rosadi.haullur.Kelas.DrawerMenu.Artikel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import id.zelory.compressor.Compressor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rosadi.haullur.R;
import com.rosadi.haullur._util.FileUtil;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class TambahArtikelBiasaActivity extends AppCompatActivity {

    EditText judul, lokasi, deskripsi;
    ImageView foto_tamnel;
    LinearLayout simpan;
    RelativeLayout pilihFoto;

    private static final int PICK_IMAGE_REQUEST = 696;

    File fotoAsli;
    Bitmap fotoKompress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_artikel_biasa);

        judul = findViewById(R.id.judul);
        lokasi = findViewById(R.id.lokasi);
        deskripsi = findViewById(R.id.deskripsi);
        pilihFoto = findViewById(R.id.pilih_foto);
        foto_tamnel = findViewById(R.id.foto_tamnel);
        simpan = findViewById(R.id.simpan);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        foto_tamnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (judul.getText().toString().isEmpty()) {
                    Toast.makeText(TambahArtikelBiasaActivity.this, "Isikan judul artikel!", Toast.LENGTH_SHORT).show();
                } else if (lokasi.getText().toString().isEmpty()) {
                    Toast.makeText(TambahArtikelBiasaActivity.this, "Isikan lokasi!", Toast.LENGTH_SHORT).show();
                } else if (deskripsi.getText().toString().isEmpty()) {
                    Toast.makeText(TambahArtikelBiasaActivity.this, "Isikan deskripsi artikel!", Toast.LENGTH_SHORT).show();
                } else {
                    tambahArtikel();
                }
            }
        });
    }

    private void tambahArtikel() {
        class ProsesTambah extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(TambahArtikelBiasaActivity.this, "Informasi", "Proses menambahkan...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah berhasil menambahkan artikel")) {
                    TambahArtikelBiasaActivity.this.finish();
                    Intent i = new Intent(TambahArtikelBiasaActivity.this, ArtikelActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                Toast.makeText(TambahArtikelBiasaActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                Date date = Calendar.getInstance().getTime();
                Locale local = new Locale("id", "id");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", local);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_FOTO_TAMNEL, getStringImage(fotoKompress));
                hashMap.put(Konfigurasi.KEY_JUDUL, judul.getText().toString());
                hashMap.put(Konfigurasi.KEY_LOKASI, lokasi.getText().toString());
                hashMap.put(Konfigurasi.KEY_TANGGAL, ""+simpleDateFormat.format(date));
                hashMap.put(Konfigurasi.KEY_DESKRIPSI, deskripsi.getText().toString());

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TAMBAH_ARTIKEL, hashMap);
                return s;
            }
        }

        ProsesTambah prosesTambah = new ProsesTambah();
        prosesTambah.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Gagal mengambil foto!", Toast.LENGTH_SHORT).show();
            }

            try {
                fotoAsli = FileUtil.from(this, data.getData());
                fotoKompress = new Compressor(this).compressToBitmap(fotoAsli);
                foto_tamnel.setImageBitmap(fotoKompress);
            } catch (IOException e) {
                Toast.makeText(this, "Gagal membaca data foto yang dipilih!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } else {
            return "";
        }
    }

    private String getReadableFileSize(long size) {
        if (size < 0) {
            return "0";
        }

        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}