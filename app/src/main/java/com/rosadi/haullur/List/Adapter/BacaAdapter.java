package com.rosadi.haullur.List.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Kelas.Artikel.ListAlmarhumActivity;
import com.rosadi.haullur.Kelas.Baca.BacaActivity;
import com.rosadi.haullur.Kelas.DrawerMenu.Haul.ProgramHaulActivity;
import com.rosadi.haullur.List.Model.Almarhums;
import com.rosadi.haullur.List.Model.Baca;
import com.rosadi.haullur.MainActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BacaAdapter extends RecyclerView.Adapter<BacaAdapter.ViewHolder> {

    Context context;
    List<Baca> bacaList;
    SharedPreferences preferences;

    public BacaAdapter(Context context, List<Baca> bacaList, SharedPreferences preferences) {
        this.context = context;
        this.bacaList = bacaList;
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public BacaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (context.getClass().getSimpleName().equals("BacaActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_baca, parent, false);
        } else if (context.getClass().getSimpleName().equals("ListAlmarhumActivity")) {
            view = LayoutInflater.from(context).inflate(R.layout.list_baca_tok, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BacaAdapter.ViewHolder holder, int position) {
        Baca baca = bacaList.get(position);

        holder.namaTV.setText(baca.getNama());
        holder.rtTV.setText("RT " + baca.getRt());
        holder.jumlahAlmarhumTV.setText(baca.getJumlahAlmarhum());

        if (context.getClass().getSimpleName().equals("BacaActivity")) {
            if (baca.getDibaca().equals("1")) {
                holder.dibacaCB.setChecked(true);
            } else {
                holder.dibacaCB.setChecked(false);
            }

            if (Objects.equals(preferences.getString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, null), "0")) {
                holder.dibacaCB.setEnabled(false);
            } else {
                holder.dibacaCB.setEnabled(true);
            }

            holder.dibacaCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Objects.equals(preferences.getString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, null), "0")) {
                        if (holder.dibacaCB.isChecked()) {
                            tandaiTelahDibaca(baca.getId());
                        } else if (!holder.dibacaCB.isChecked()){
                            hapusTandaDibaca(baca.getId());
                        }
                    } else {
                        holder.dibacaCB.setEnabled(false);
                    }
                }
            });
        }

        List<Almarhums> almarhumsList = baca.getAlmarhumsList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, linearLayoutManager.getOrientation());
        holder.recyclerView.addItemDecoration(dividerItemDecoration);
        BacaAlmarhumAdapter bacaAlmarhumAdapter = new BacaAlmarhumAdapter(context, almarhumsList);
        holder.recyclerView.setAdapter(bacaAlmarhumAdapter);

        holder.headerRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.listAlmarhum.getVisibility() == View.VISIBLE) {
                    holder.listAlmarhum.setVisibility(View.GONE);
                } else {
                    holder.listAlmarhum.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.sembunyikanRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listAlmarhum.setVisibility(View.GONE);
                if (context.getClass().getSimpleName().equals("BacaActivity")) {
                    ((BacaActivity) context).scrollToPosition(holder.getAdapterPosition());
                } else if (context.getClass().getSimpleName().equals("ListAlmarhumActivity")) {
                    ((ListAlmarhumActivity) context).scrollToPosition(holder.getAdapterPosition());
                }
            }
        });
    }

    private void hapusTandaDibaca(String id) {
        class ProsesUpdate extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                ((BacaActivity) context).loadRingkasan();

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_TANDA_DIBACA, hashMap);

                return s;
            }
        }

        ProsesUpdate prosesUpdate = new ProsesUpdate();
        prosesUpdate.execute();
    }

    private void tandaiTelahDibaca(String id) {
        class ProsesUpdate extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                ((BacaActivity) context).loadRingkasan();

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_TANDAI_DIBACA, hashMap);

                return s;
            }
        }

        ProsesUpdate prosesUpdate = new ProsesUpdate();
        prosesUpdate.execute();

    }

    @Override
    public int getItemCount() {
        return bacaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView namaTV, rtTV, jumlahAlmarhumTV;
        CheckBox dibacaCB;
        RelativeLayout headerRL, sembunyikanRL;
        LinearLayout listAlmarhum;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            namaTV = itemView.findViewById(R.id.nama);
            rtTV = itemView.findViewById(R.id.rt);
            jumlahAlmarhumTV = itemView.findViewById(R.id.jumlah_almarhum);
            dibacaCB = itemView.findViewById(R.id.dibaca);

            headerRL = itemView.findViewById(R.id.header);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            listAlmarhum = itemView.findViewById(R.id.list_almarhum);
            sembunyikanRL = itemView.findViewById(R.id.sembunyikan);
        }
    }
}
