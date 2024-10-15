package com.rosadi.haullur.Kelas.DrawerMenu.Artikel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import id.zelory.compressor.Compressor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.FileUtil;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class EditArtikelBiasaActivity extends AppCompatActivity {

    ImageView imageViewTamnel;
    EditText editTextJudul, editTextLokasi, editTextDeskripsi;
    LinearLayout simpan;
    RelativeLayout pilihFoto, hapus;

    String id, fotoTamnel, judul, tanggal, lokasi, deskripsi, dilihat, idHaul, foto, foto2;
    Bitmap bitmapFotoTamnel, bitmapFotoTamnelOld = null;

    private static final int PICK_IMAGE_REQUEST = 696;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_artikel_biasa);

        imageViewTamnel = findViewById(R.id.foto_tamnel);
        editTextJudul = findViewById(R.id.judul);
        editTextLokasi = findViewById(R.id.lokasi);
        editTextDeskripsi = findViewById(R.id.deskripsi);
        pilihFoto = findViewById(R.id.pilih_foto);
        hapus = findViewById(R.id.hapus);
        simpan = findViewById(R.id.simpan);

        findViewById(R.id.kembali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        id = intent.getStringExtra(Konfigurasi.KEY_ID);
        fotoTamnel = intent.getStringExtra(Konfigurasi.KEY_FOTO_TAMNEL);
        judul = intent.getStringExtra(Konfigurasi.KEY_JUDUL);
        tanggal = intent.getStringExtra(Konfigurasi.KEY_TANGGAL);
        lokasi = intent.getStringExtra(Konfigurasi.KEY_LOKASI);
        deskripsi = intent.getStringExtra(Konfigurasi.KEY_DESKRIPSI);
        dilihat = intent.getStringExtra(Konfigurasi.KEY_DILIHAT);
        idHaul = intent.getStringExtra(Konfigurasi.KEY_ID_HAUL);
        foto = intent.getStringExtra(Konfigurasi.KEY_FOTO);
        foto2 = intent.getStringExtra(Konfigurasi.KEY_FOTO_2);

        Glide.with(this)
                .asBitmap()
                .load(Konfigurasi.URL + "images/thumbnail/" + fotoTamnel)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageViewTamnel.setImageBitmap(resource);
                        bitmapFotoTamnel = resource;
                        bitmapFotoTamnelOld = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

        editTextJudul.setText(judul);
        editTextLokasi.setText(lokasi);
        editTextDeskripsi.setText(deskripsi);

        imageViewTamnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogHapusArtikel();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextJudul.getText().toString().equals(judul) && editTextLokasi.getText().toString().equals(lokasi) && editTextDeskripsi.getText().toString().equals(deskripsi)
                        && bitmapFotoTamnel == bitmapFotoTamnelOld) {
                    Toast.makeText(EditArtikelBiasaActivity.this, "Tidak ada data yang dirubah!", Toast.LENGTH_SHORT).show();
                } else if (editTextJudul.getText().toString().isEmpty()) {
                    Toast.makeText(EditArtikelBiasaActivity.this, "Isikan judul artikel!", Toast.LENGTH_SHORT).show();
                } else if (editTextLokasi.getText().toString().isEmpty()) {
                    Toast.makeText(EditArtikelBiasaActivity.this, "Isikan lokasi!", Toast.LENGTH_SHORT).show();
                } else if (editTextDeskripsi.getText().toString().isEmpty()) {
                    Toast.makeText(EditArtikelBiasaActivity.this, "Isikan deskripsi artikel!", Toast.LENGTH_SHORT).show();
                } else {
                    updateArtikel();
                }
            }
        });
    }

    private void updateArtikel() {
        class ProsesUpdate extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(EditArtikelBiasaActivity.this, "Informasi", "Proses memperbarui...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah berhasil memperbarui artikel")) {
                    EditArtikelBiasaActivity.this.finish();
                    Intent i = new Intent(EditArtikelBiasaActivity.this, ArtikelActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                Toast.makeText(EditArtikelBiasaActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);
                if (bitmapFotoTamnel == bitmapFotoTamnelOld) {
                    hashMap.put(Konfigurasi.KEY_FOTO_TAMNEL, "tidak-berubah");
                } else {
                    hashMap.put(Konfigurasi.KEY_FOTO_TAMNEL, getStringImage(bitmapFotoTamnel));
                }
                hashMap.put(Konfigurasi.KEY_JUDUL, editTextJudul.getText().toString());
                hashMap.put(Konfigurasi.KEY_LOKASI, editTextLokasi.getText().toString());
                hashMap.put(Konfigurasi.KEY_DESKRIPSI, editTextDeskripsi.getText().toString());

                hashMap.put("old_tamnel_name", fotoTamnel);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_UPDATE_ARTIKEL, hashMap);
                return s;
            }
        }

        ProsesUpdate prosesUpdate = new ProsesUpdate();
        prosesUpdate.execute();
    }

    private void openDialogHapusArtikel() {
        Dialog dialog = new Dialog(EditArtikelBiasaActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("Hapus Artikel");
        teks.setText("Menghapus artikel juga akan menghilangkan artikel yang ada pada beranda secara permanen, apakah Anda ingin menghapusnya ?");
        teksiya.setText("Hapus");

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusArtikelLaporan();
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

    private void hapusArtikelLaporan() {
        class HapusProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(EditArtikelBiasaActivity.this, "Informasi", "Proses menghapus...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah, Artikel berhasil dihapus")) {
                    Intent i = new Intent(EditArtikelBiasaActivity.this, ArtikelActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                Toast.makeText(EditArtikelBiasaActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);
                hashMap.put("old_tamnel_name", fotoTamnel);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_ARTIKEL, hashMap);

                return s;
            }
        }

        HapusProses hapusProses = new HapusProses();
        hapusProses.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Gagal mengambil foto!", Toast.LENGTH_SHORT).show();
            }

            try {
                File fotoAsli = FileUtil.from(this, data.getData());
                bitmapFotoTamnel = new Compressor(this).compressToBitmap(fotoAsli);

                imageViewTamnel.setImageBitmap(bitmapFotoTamnel);
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
}