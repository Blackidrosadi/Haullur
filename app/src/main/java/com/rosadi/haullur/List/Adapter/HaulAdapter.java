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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Kelas.Almarhum.DataKeluargaDetailActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.Akun.DetailAkunActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.Haul.ProgramHaulActivity;
import com.rosadi.haullur.Kelas.Laporan.LaporanDetailActivity;
import com.rosadi.haullur.List.Model.Haul;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HaulAdapter extends RecyclerView.Adapter<HaulAdapter.ViewHolder> {

    Context context;
    List<Haul> haulList;

    public HaulAdapter(Context context, List<Haul> haulList) {
        this.context = context;
        this.haulList = haulList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (context.getClass().getSimpleName().equals("ProgramHaulActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_haul, parent, false);
        } else if (context.getClass().getSimpleName().equals("LaporanActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_haul_simple, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Haul haul = haulList.get(position);

        if (context.getClass().getSimpleName().equals("ProgramHaulActivity")) {
            if (haul.getStatus().equals("1")) {
                holder.status.setText("Sedang berjalan");
                holder.status.setTextColor(context.getResources().getColor(R.color.yellow));

                holder.program.setText(haul.getDeskripsi());
                holder.tanggal.setText(haul.getTanggal());
                holder.selesaikan.setEnabled(true);
                holder.selesaikan_teks.setText("Selesaikan Haul");
                holder.selesaikan_teks.setTextColor(context.getResources().getColor(R.color.white));

                holder.selesaikan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialogSelesaikanHaul(haul.getId());
                    }
                });
            } else {
                holder.status.setText("Selesai");
                holder.status.setTextColor(context.getResources().getColor(R.color.accent));

                holder.program.setText(haul.getDeskripsi());
                holder.tanggal.setText(haul.getTanggal());
                holder.selesaikan.setEnabled(false);
                holder.selesaikan_teks.setText("Haul Telah Selesai");
                holder.selesaikan_teks.setTextColor(context.getResources().getColor(R.color.white_50));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, LaporanDetailActivity.class);
                    i.putExtra("id", haul.getId());
                    i.putExtra("tanggal", haul.getTanggal());
                    i.putExtra("total", haul.getTotal());
                    context.startActivity(i);
                }
            });
        } else if (context.getClass().getSimpleName().equals("LaporanActivity")) {
            if (haul.getStatus().equals("1")) {
                holder.status.setVisibility(View.VISIBLE);
            } else {
                holder.status.setVisibility(View.GONE);
            }

            holder.tanggal.setText(haul.getTanggal());
            holder.total.setText("Rp" + rupiahFormat(haul.getTotal()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, LaporanDetailActivity.class);
                    i.putExtra("id", haul.getId());
                    i.putExtra("tanggal", haul.getTanggal());
                    i.putExtra("total", haul.getTotal());
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return haulList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView program, tanggal, status, total, selesaikan_teks;
        RelativeLayout selesaikan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            program = itemView.findViewById(R.id.haul);
            tanggal = itemView.findViewById(R.id.tanggal);
            status = itemView.findViewById(R.id.status);
            total = itemView.findViewById(R.id.total);
            selesaikan = itemView.findViewById(R.id.selesaikan);
            selesaikan_teks = itemView.findViewById(R.id.teks_selesaikan);
        }
    }

    private void openDialogSelesaikanHaul(String id) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_iya_tidak);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView judul = dialog.findViewById(R.id.judul);
        TextView teks = dialog.findViewById(R.id.teks);
        TextView teksiya = dialog.findViewById(R.id.teksiya);

        judul.setText("Selesaikan Haul");
        teks.setText("Harap cek laporan haul terlebih dahulu dikarenakan jika haul selesai maka petugas tidak dapat menambah dan merubah laporan haul, apakah Anda ingin menyelesaikan ?");
        teksiya.setText("Selesaikan");

        dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selesaikanHaul(id, dialog);
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

    private void selesaikanHaul(String id, Dialog dialog) {
        class ProsesUpdate extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "Informasi", "Proses menyelesaikan program haul...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.equals("Alhamdulillah program haul berhasil diselesaikan")) {
                    ((ProgramHaulActivity) context).loadHaul();
                    dialog.dismiss();
                }

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_SELESAIKAN_HAUL, hashMap);

                return s;
            }
        }

        ProsesUpdate prosesUpdate = new ProsesUpdate();
        prosesUpdate.execute();
    }

    private String rupiahFormat(String jumlahUang) {
        Locale local = new Locale("id", "id");
        String replaceable = String.format("[Rp,.\\s]",
                NumberFormat.getCurrencyInstance()
                        .getCurrency()
                        .getSymbol(local));
        String cleanString = jumlahUang.replaceAll(replaceable,
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

        return clean;
    }
}
