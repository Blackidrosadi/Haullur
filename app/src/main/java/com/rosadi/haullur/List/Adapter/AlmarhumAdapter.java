package com.rosadi.haullur.List.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Keluarga.DataKeluargaActivity;
import com.rosadi.haullur.Keluarga.DataKeluargaDetailActivity;
import com.rosadi.haullur.List.Model.Almarhum;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlmarhumAdapter extends RecyclerView.Adapter<AlmarhumAdapter.ViewHolder> {

    Context context;
    List<Almarhum> almarhumList;

    public AlmarhumAdapter(Context context, List<Almarhum> almarhumList) {
        this.context = context;
        this.almarhumList = almarhumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_almarhum, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Almarhum almarhum = almarhumList.get(position);

        holder.nomor.setText(almarhum.getNomor());
        holder.nama.setText(almarhum.getNama());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_ubah_almarhum);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                EditText namaEd = dialog.findViewById(R.id.nama);
                namaEd.setText(almarhum.getNama());

                dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (namaEd.getText().toString().isEmpty()) {
                            Toast.makeText(context, "Masukkan nama!!", Toast.LENGTH_SHORT).show();
                        } else if (namaEd.getText().toString().length() < 4) {
                            Toast.makeText(context, "Masukkan nama minimal 4 karakter!!", Toast.LENGTH_SHORT).show();
                        } else {
                            ubahAlmarhum(almarhum.getId(), namaEd.getText().toString());
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

                dialog.findViewById(R.id.hapus).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog_info = new Dialog(context);
                        dialog_info.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_info.setContentView(R.layout.dialog_iya_tidak);
                        dialog_info.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        dialog_info.setCancelable(false);

                        TextView judul = dialog_info.findViewById(R.id.judul);
                        TextView teks = dialog_info.findViewById(R.id.teks);
                        TextView teksiya = dialog_info.findViewById(R.id.teksiya);

                        judul.setText("Hapus Data Almarhum");
                        teks.setText("Apakah Anda ingin menghapus almarhum / almarhumah ini ?");
                        teksiya.setText("Hapus");

                        dialog_info.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hapusAlmarhum(almarhum.getId(), dialog, dialog_info);
                            }
                        });

                        dialog_info.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog_info.dismiss();
                            }
                        });

                        dialog_info.show();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return almarhumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nomor, nama;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nomor = itemView.findViewById(R.id.nomor);
            nama = itemView.findViewById(R.id.nama);
        }
    }

    private void ubahAlmarhum(String id, String nama) {
        class EditProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "Informasi", "Proses memperbarui...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah data almarhum berhasil diperbarui")) {
                    ((DataKeluargaDetailActivity) context).loadAlmarhums();
                }

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);
                hashMap.put(Konfigurasi.KEY_NAMA, nama);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_EDIT_ALMARHUM, hashMap);

                return s;
            }
        }

        EditProses editProses = new EditProses();
        editProses.execute();
    }

    private void hapusAlmarhum(String id, Dialog dialog, Dialog dialog_info) {
        class HapusProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "Informasi", "Proses menghapus...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                dialog_info.dismiss();

                ((DataKeluargaDetailActivity) context).loadAlmarhums();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_ALMARHUM, hashMap);

                return s;
            }
        }

        HapusProses hapusProses = new HapusProses();
        hapusProses.execute();
    }
}
