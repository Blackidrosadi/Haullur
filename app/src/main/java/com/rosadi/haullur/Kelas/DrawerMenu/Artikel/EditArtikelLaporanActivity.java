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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.rosadi.haullur.Kelas.Almarhum.DataKeluargaActivity;
import com.rosadi.haullur.Kelas.Almarhum.DataKeluargaDetailActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.FileUtil;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EditArtikelLaporanActivity extends AppCompatActivity {

    String id, fotoTamnel, judul, tanggal, lokasi, deskripsi, dilihat, idHaul, foto, foto2;
    Bitmap bitmapFotoTamnel, bitmapFoto, bitmapFoto2;
    Bitmap bitmapFotoTamnelOld = null, bitmapFotoOld = null, bitmapFoto2Old = null;

    private static final int KODE_FOTO_TAMNEL = 12;
    private static final int KODE_FOTO_PERTAMA = 1;
    private static final int KODE_FOTO_KEDUA = 2;

    ImageView imageViewTamnel, imageView, imageView2;
    EditText editTextJudul, editTextLokasi, editTextDeskripsi;
    RelativeLayout tambahFoto, hapus;
    LinearLayout simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_artikel_laporan);

        imageViewTamnel = findViewById(R.id.foto_tamnel);
        editTextJudul = findViewById(R.id.judul);
        editTextLokasi = findViewById(R.id.lokasi);
        editTextDeskripsi = findViewById(R.id.deskripsi);
        tambahFoto = findViewById(R.id.tambah_foto);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
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

        if (!foto.equals("")) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .asBitmap()
                    .load(Konfigurasi.URL + "images/artikel/" + foto)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                            bitmapFoto = resource;
                            bitmapFotoOld = resource;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

            if (!foto2.equals("")) {
                imageView2.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .asBitmap()
                        .load(Konfigurasi.URL + "images/artikel/" + foto2)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imageView2.setImageBitmap(resource);
                                bitmapFoto2 = resource;
                                bitmapFoto2Old = resource;
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
                tambahFoto.setVisibility(View.GONE);
            } else {
                imageView2.setVisibility(View.GONE);
                tambahFoto.setVisibility(View.VISIBLE);
            }
        } else {
            imageView.setVisibility(View.GONE);
            tambahFoto.setVisibility(View.VISIBLE);
        }

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
                if (bitmapFoto == null && bitmapFoto2 == null) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), KODE_FOTO_PERTAMA);
                } else if (bitmapFoto != null) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), KODE_FOTO_KEDUA);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmapFoto != null) {
                    openDialogUbahFoto(KODE_FOTO_PERTAMA);
                }
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmapFoto2 != null) {
                    openDialogUbahFoto(KODE_FOTO_KEDUA);
                }
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
                        && bitmapFotoTamnel == bitmapFotoTamnelOld && bitmapFoto == bitmapFotoOld && bitmapFoto2 == bitmapFoto2Old) {
                    Toast.makeText(EditArtikelLaporanActivity.this, "Tidak ada data yang dirubah!", Toast.LENGTH_SHORT).show();
                } else if (editTextJudul.getText().toString().isEmpty()) {
                    Toast.makeText(EditArtikelLaporanActivity.this, "Isikan judul artikel!", Toast.LENGTH_SHORT).show();
                } else if (editTextLokasi.getText().toString().isEmpty()) {
                    Toast.makeText(EditArtikelLaporanActivity.this, "Isikan lokasi!", Toast.LENGTH_SHORT).show();
                } else if (editTextDeskripsi.getText().toString().isEmpty()) {
                    Toast.makeText(EditArtikelLaporanActivity.this, "Isikan deskripsi artikel!", Toast.LENGTH_SHORT).show();
                } else {
                    updateArtikelLaporan();
                }
            }
        });
    }

    private void openDialogHapusArtikel() {
        Dialog dialog = new Dialog(EditArtikelLaporanActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("Hapus Artikel Laporan");
        teks.setText("Menghapus artikel laporan juga akan menghilangkan laporan yang ada pada beranda, apakah Anda ingin menghapusnya ?");
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
                progressDialog = ProgressDialog.show(EditArtikelLaporanActivity.this, "Informasi", "Proses menghapus...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah, Artikel laporan berhasil dihapus")) {
                    Intent i = new Intent(EditArtikelLaporanActivity.this, ArtikelActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                Toast.makeText(EditArtikelLaporanActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);
                hashMap.put("old_tamnel_name", fotoTamnel);
                hashMap.put("old_foto_name", foto);
                hashMap.put("old_foto2_name", foto2);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_ARTIKEL_LAPORAN, hashMap);

                return s;
            }
        }

        HapusProses hapusProses = new HapusProses();
        hapusProses.execute();
    }

    private void updateArtikelLaporan() {
        class ProsesUpdate extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(EditArtikelLaporanActivity.this, "Informasi", "Proses memperbarui...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah berhasil memperbarui artikel")) {
                    EditArtikelLaporanActivity.this.finish();
                    Intent i = new Intent(EditArtikelLaporanActivity.this, ArtikelActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

                Toast.makeText(EditArtikelLaporanActivity.this, s, Toast.LENGTH_SHORT).show();

                System.out.println("artikel error : " + s);
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
                hashMap.put(Konfigurasi.KEY_FOTO, getStringImage(bitmapFoto));
                hashMap.put(Konfigurasi.KEY_FOTO_2, getStringImage(bitmapFoto2));

                hashMap.put("old_tamnel_name", fotoTamnel);
                hashMap.put("old_foto_name", foto);
                hashMap.put("old_foto2_name", foto2);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_UPDATE_ARTIKEL_LAPORAN, hashMap);
                return s;
            }
        }

        ProsesUpdate prosesUpdate = new ProsesUpdate();
        prosesUpdate.execute();
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

    private void openDialogUbahFoto(int kode) {
        Dialog dialog = new Dialog(EditArtikelLaporanActivity.this);
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
                    if (bitmapFoto2 != null) {
                        bitmapFoto = bitmapFoto2;
                        bitmapFoto2 = null;

                        imageView.setImageBitmap(bitmapFoto);
                        imageView2.setVisibility(View.GONE);
                        tambahFoto.setVisibility(View.VISIBLE);
                    } else {
                        bitmapFoto = null;
                        imageView.setVisibility(View.GONE);
                        tambahFoto.setVisibility(View.VISIBLE);
                    }
                } else if (kode == KODE_FOTO_KEDUA) {
                    bitmapFoto2 = null;
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
                bitmapFotoTamnel = new Compressor(this).compressToBitmap(fotoAsli);

                imageViewTamnel.setImageBitmap(bitmapFotoTamnel);
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
                bitmapFoto = new Compressor(this).compressToBitmap(fotoAsli);

                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmapFoto);
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
                bitmapFoto2 = new Compressor(this).compressToBitmap(fotoAsli);

                imageView2.setVisibility(View.VISIBLE);
                imageView2.setImageBitmap(bitmapFoto2);

                tambahFoto.setVisibility(View.GONE);
            } catch (IOException e) {
                Toast.makeText(this, "Gagal membaca data foto yang dipilih!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}