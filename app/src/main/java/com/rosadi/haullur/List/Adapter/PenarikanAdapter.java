package com.rosadi.haullur.List.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.List.Model.Penarikan;
import com.rosadi.haullur.Kelas.Penarikan.PenarikanActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PenarikanAdapter extends RecyclerView.Adapter<PenarikanAdapter.ViewHolder> {

    Context context;
    List<Penarikan> penarikanList;

    public PenarikanAdapter(Context context, List<Penarikan> penarikanList) {
        this.context = context;
        this.penarikanList = penarikanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (context.getClass().getSimpleName().equals("PenarikanActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_keluarga_penarikan, parent, false);
        } else if (context.getClass().getSimpleName().equals("LaporanRincianActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_keluarga_penarikan_tanpa_wa, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Penarikan penarikan = penarikanList.get(position);

        holder.nama.setText(penarikan.getNama());
        holder.total_almarhums.setText(penarikan.getJumlahAlmarhum());

        Locale local = new Locale("id", "id");
        String replaceable = String.format("[Rp,.\\s]",
                NumberFormat.getCurrencyInstance()
                        .getCurrency()
                        .getSymbol(local));
        String cleanString = penarikan.getJumlahUang().replaceAll(replaceable, "");

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

        holder.jumlah.setText("Rp" + clean + ",-");

        if (context.getClass().getSimpleName().equals("PenarikanActivity")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialogEdit(penarikan.getId(), penarikan.getNama(), penarikan.getJumlahUang());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return penarikanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama, jumlah, total_almarhums;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            jumlah = itemView.findViewById(R.id.jumlah);
            total_almarhums = itemView.findViewById(R.id.total_almarhums);
        }
    }

    private void openDialogEdit(String id, String nama, String jumlahUang) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ubah_dana_penarikan);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        TextView namaTV = dialog.findViewById(R.id.nama);
        namaTV.setText("Penarikan Dana Keluarga " + nama);
        EditText editTextJumlah = dialog.findViewById(R.id.jumlah);
        editTextJumlah.setText(rupiahFormat(jumlahUang));
        setCurrency(editTextJumlah);

        dialog.findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextJumlah.getText().length() == 0) {
                    Toast.makeText(context, "Masukkan jumlah!", Toast.LENGTH_SHORT).show();
                } else {
                    int jumlah = Integer.parseInt(editTextJumlah.getText().toString().replace(".", ""));

                    updatePenarikan(id, jumlah);
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
                Dialog dialogYN = new Dialog(context);
                dialogYN.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogYN.setContentView(R.layout.dialog_iya_tidak);
                dialogYN.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialogYN.setCancelable(false);

                TextView judul = dialogYN.findViewById(R.id.judul);
                TextView teks = dialogYN.findViewById(R.id.teks);
                TextView teksiya = dialogYN.findViewById(R.id.teksiya);

                judul.setText("Hapus Data Penarikan");
                teks.setText("Apakah anda yakin ingin menghapus data penarikan ini? Anda masih bisa melakukan proses penarikan lagi.");
                teksiya.setText("Hapus");

                dialogYN.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hapusPenarikan(id);
                        dialogYN.dismiss();
                        dialog.dismiss();
                    }
                });

                dialogYN.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogYN.dismiss();
                    }
                });

                dialogYN.show();
            }
        });

        dialog.show();
    }

    private void hapusPenarikan(String id) {
        class ProsesHapus extends AsyncTask<Void, Void, String> {

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
                ((PenarikanActivity) context).loadPenarikanByPetugas();
                ((PenarikanActivity) context).loadTotalPenarikanByPetugas();

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_DANA_PENARIKAN, hashMap);

                return s;
            }
        }

        ProsesHapus prosesHapus = new ProsesHapus();
        prosesHapus.execute();
    }

    private void updatePenarikan(String id, int jumlah) {
        class ProsesUpdate extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(context, "Informasi", "Proses mengubah...", false, false);
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
                hashMap.put(Konfigurasi.KEY_ID, id);
                hashMap.put(Konfigurasi.KEY_JUMLAH_UANG, String.valueOf(jumlah));

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_UPDATE_DANA_PENARIKAN, hashMap);

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
}
