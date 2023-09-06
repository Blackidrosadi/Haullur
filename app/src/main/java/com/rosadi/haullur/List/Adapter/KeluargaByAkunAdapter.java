package com.rosadi.haullur.List.Adapter;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Akun.DetailAkunActivity;
import com.rosadi.haullur.Keluarga.DataKeluargaActivity;
import com.rosadi.haullur.Keluarga.DataKeluargaDetailActivity;
import com.rosadi.haullur.List.Model.Keluarga;
import com.rosadi.haullur.Penarikan.PenarikanActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KeluargaByAkunAdapter extends RecyclerView.Adapter<KeluargaByAkunAdapter.ViewHolder> {

    Context context;
    List<Keluarga> keluargaList;

    public KeluargaByAkunAdapter(Context context, List<Keluarga> keluargaList) {
        this.context = context;
        this.keluargaList = keluargaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (context.getClass().getSimpleName().equals("DetailAkunActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_keluarga, parent, false);
        } else if (context.getClass().getSimpleName().equals("PenarikanActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_keluarga_lite, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Keluarga keluarga = keluargaList.get(position);

        holder.nama.setText(keluarga.getNama());
        holder.rt.setText("RT " + keluarga.getRt());
        holder.jumlah.setText(keluarga.getJumlah());

        if (context.getClass().getSimpleName().equals("DetailAkunActivity")) {
            holder.telepon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (keluarga.getTelepon().isEmpty()) {
                        Toast.makeText(context, "WhatsApp belum ditambahkan!", Toast.LENGTH_SHORT).show();
                    } else {
                        PackageManager packageManager = context.getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);

                        try {
                            String pesan = "Haul jemuah legi, Wayah e penarikan";
                            String url = "https://api.whatsapp.com/send?phone=" + keluarga.getTelepon() + "&text=" + URLEncoder.encode(pesan, "UTF-8");
                            i.setPackage("com.whatsapp");
                            i.setData(Uri.parse(url));
                            if (i.resolveActivity(packageManager) != null) {
                                context.startActivity(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else if (context.getClass().getSimpleName().equals("PenarikanActivity")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_penarikan);
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);

                    TextView nama = dialog.findViewById(R.id.nama);
                    nama.setText("Penarikan Dana Keluarga " + keluarga.getNama());
                    EditText editTextJumlah = dialog.findViewById(R.id.jumlah);
                    setCurrency(editTextJumlah);

                    dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (editTextJumlah.getText().length() == 0) {
                                Toast.makeText(context, "Masukkan jumlah!", Toast.LENGTH_SHORT).show();
                            } else {
                                int jumlah = Integer.parseInt(editTextJumlah.getText().toString().replace(".", ""));

                                transaksiPenarikan(keluarga.getId(), jumlah);
                                dialog.dismiss();
                                ((PenarikanActivity) context).dialogTambah.dismiss();
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
            });
        }
    }

    private void transaksiPenarikan(String idKeluarga, int jumlah) {
        class ProsesTambah extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "Informasi", "Proses menambahkan...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                ((PenarikanActivity) context).loadPenarikanByPetugas();
                ((PenarikanActivity) context).loadTotalPenarikanByPetugas();

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID_HAUL, ((PenarikanActivity) context).idHaul);
                hashMap.put(Konfigurasi.KEY_ID_KELUARGA, idKeluarga);
                hashMap.put(Konfigurasi.KEY_JUMLAH_UANG, String.valueOf(jumlah));
                hashMap.put(Konfigurasi.KEY_DESKRIPSI, "");
                hashMap.put(Konfigurasi.KEY_ID_AKUN, ((PenarikanActivity) context).idAkun);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TRANSAKSI_PENARIKAN, hashMap);

                return s;
            }
        }

        ProsesTambah prosesTambah = new ProsesTambah();
        prosesTambah.execute();
    }

    private void setCurrency(EditText edt) {
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

    @Override
    public int getItemCount() {
        return keluargaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, rt, jumlah;
        RelativeLayout telepon;

        RelativeLayout bg, bgjumlah;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            rt = itemView.findViewById(R.id.rt);
            telepon = itemView.findViewById(R.id.telepon);
            jumlah = itemView.findViewById(R.id.total_almarhums);

            bg = itemView.findViewById(R.id.bg);
            bgjumlah = itemView.findViewById(R.id.item1);
        }
    }
}
