package com.rosadi.haullur.Kelas.Laporan.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Kelas.Laporan.LaporanDetailActivity;
import com.rosadi.haullur.List.Adapter.PengeluaranAdapter;
import com.rosadi.haullur.List.Model.Pengeluaran;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PengeluaranFragment extends Fragment {

    String idHaul;
    List<Pengeluaran> pengeluaranList = new ArrayList<>();
    PengeluaranAdapter pengeluaranAdapter;

    RecyclerView recyclerView;

    public PengeluaranFragment(String idHaul) {
        this.idHaul = idHaul;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengeluaran, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        pengeluaranAdapter = new PengeluaranAdapter(getActivity(), pengeluaranList);
        recyclerView.setAdapter(pengeluaranAdapter);

        loadDataPengeluaran();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_iya_tidak);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                TextView judul = dialog.findViewById(R.id.judul);
                TextView teks = dialog.findViewById(R.id.teks);
                TextView teksiya = dialog.findViewById(R.id.teksiya);

                int position = viewHolder.getAdapterPosition();
                Pengeluaran pengeluaran = pengeluaranList.get(position);

                judul.setText("Hapus Dana Pengeluaran");
                teks.setText("Apa Antum yakin ingin menghapus pengeluaran dana " + pengeluaran.getDeskripsi() + " dengan jumlah Rp" + rupiahFormat(pengeluaran.getJumlahUang()) + ",- ?");
                teksiya.setText("Hapus");

                dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hapusDanaPengeluaran(pengeluaran.getId());

                        pengeluaranList.remove(position);
                        pengeluaranAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        pengeluaranAdapter.notifyDataSetChanged();
                    }
                });

                dialog.show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void hapusDanaPengeluaran(String id) {
        class HapusProses extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Informasi", "Proses menghapus...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                ((LaporanDetailActivity) getActivity()).loadTotalDana();

                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_ID, id);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_DANA_PENGELUARAN, hashMap);

                return s;
            }
        }

        HapusProses hapusProses = new HapusProses();
        hapusProses.execute();
    }

    private void loadDataPengeluaran() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pengeluaranList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        Pengeluaran pengeluaran = new Pengeluaran();
                        pengeluaran.setId(object.getString(Konfigurasi.KEY_ID));
                        pengeluaran.setIdHaul(object.getString(Konfigurasi.KEY_ID_HAUL));
                        pengeluaran.setDeskripsi(object.getString(Konfigurasi.KEY_DESKRIPSI));
                        pengeluaran.setJumlahUang(object.getString(Konfigurasi.KEY_JUMLAH_UANG));
                        pengeluaranList.add(pengeluaran);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    pengeluaranAdapter = new PengeluaranAdapter(getActivity(), pengeluaranList);
                    recyclerView.setAdapter(pengeluaranAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_LAPORAN_PENGELUARAN_DANA, idHaul);
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
    }

    public String rupiahFormat(String jumlah) {
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