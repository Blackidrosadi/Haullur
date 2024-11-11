package com.rosadi.haullur.Kelas.Laporan.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.rosadi.haullur.List.Adapter.HaulDetailAdapter;
import com.rosadi.haullur.List.Adapter.HaulDetailDanaLainnyaAdapter;
import com.rosadi.haullur.List.Model.DanaLainnya;
import com.rosadi.haullur.List.Model.HaulDetail;
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


public class PemasukanFragment extends Fragment {

    SharedPreferences preferences;

    String idHaul;
    List<HaulDetail> haulDetailList = new ArrayList<>();
    HaulDetailAdapter haulDetailAdapter;

    List<DanaLainnya> danaLainnyaList = new ArrayList<>();
    HaulDetailDanaLainnyaAdapter haulDetailDanaLainnyaAdapter;

    RecyclerView recyclerViewPetugas, recyclerViewLainnya;

    public PemasukanFragment(String idHaul) {
        this.idHaul = idHaul;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pemasukan, container, false);

        preferences = getActivity().getSharedPreferences(Konfigurasi.KEY_USER_PREFERENCE, 0);

        recyclerViewPetugas = view.findViewById(R.id.recycler_view_petugas);
        recyclerViewLainnya = view.findViewById(R.id.recycler_view_lainnya);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewPetugas.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewPetugas.getContext(), linearLayoutManager.getOrientation());
        recyclerViewPetugas.addItemDecoration(dividerItemDecoration);
        haulDetailAdapter = new HaulDetailAdapter(getActivity(), haulDetailList, idHaul);
        recyclerViewPetugas.setAdapter(haulDetailAdapter);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLainnya.setLayoutManager(linearLayoutManager);
        recyclerViewLainnya.addItemDecoration(dividerItemDecoration);
        haulDetailDanaLainnyaAdapter = new HaulDetailDanaLainnyaAdapter(getActivity(), danaLainnyaList);
        recyclerViewLainnya.setAdapter(haulDetailDanaLainnyaAdapter);

        loadDataPenarikanPetugas();
        loadDataDanaLainnya();

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
                DanaLainnya danaLainnya = danaLainnyaList.get(position);

                judul.setText("Hapus Dana Pengeluaran");
                teks.setText("Apa Antum yakin ingin menghapus pemasukan dana dengan jumlah Rp" + rupiahFormat(danaLainnya.getJumlahUang()) + ",- ?");
                teksiya.setText("Hapus");

                dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hapusDanaPemasukkanLainnya(danaLainnya.getId());

                        danaLainnyaList.remove(position);
                        haulDetailDanaLainnyaAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        haulDetailDanaLainnyaAdapter.notifyDataSetChanged();
                    }
                });

                dialog.show();
            }
        });

        if (!preferences.getString(Konfigurasi.KEY_USER_LEVEL_PREFERENCE, null).equals("0")) {
            itemTouchHelper.attachToRecyclerView(recyclerViewLainnya);
        }

        return view;
    }

    private void hapusDanaPemasukkanLainnya(String id) {
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
                String s = rh.sendPostRequest(Konfigurasi.URL_HAPUS_DANA_PEMASUKAN_LAINNYA, hashMap);

                return s;
            }
        }

        HapusProses hapusProses = new HapusProses();
        hapusProses.execute();
    }

    private void loadDataDanaLainnya() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                danaLainnyaList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        DanaLainnya danaLainnya = new DanaLainnya();
                        danaLainnya.setId(object.getString(Konfigurasi.KEY_ID));
                        danaLainnya.setIdHaul(object.getString(Konfigurasi.KEY_ID_HAUL));
                        danaLainnya.setIdKeluarga(object.getString(Konfigurasi.KEY_ID_KELUARGA));
                        danaLainnya.setJumlahUang(object.getString(Konfigurasi.KEY_JUMLAH_UANG));
                        danaLainnya.setDeskripsi(object.getString(Konfigurasi.KEY_DESKRIPSI));
                        danaLainnya.setIdAkun(object.getString(Konfigurasi.KEY_ID_AKUN));
                        danaLainnya.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        danaLainnya.setRt(object.getString(Konfigurasi.KEY_RT));
                        danaLainnya.setJumlahAlmarhum(object.getString(Konfigurasi.KEY_JUMLAH_ALMARHUM));
                        danaLainnyaList.add(danaLainnya);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerViewLainnya.setLayoutManager(linearLayoutManager);
                    haulDetailDanaLainnyaAdapter = new HaulDetailDanaLainnyaAdapter(getActivity(), danaLainnyaList);
                    recyclerViewLainnya.setAdapter(haulDetailDanaLainnyaAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_LAPORAN_DANA_LAINNYA, idHaul);
                return s;
            }
        }

        LoadData LoadData = new LoadData();
        LoadData.execute();
    }

    private void loadDataPenarikanPetugas() {
        class LoadData extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                haulDetailList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(Konfigurasi.KEY_JSON_ARRAY_RESULT);
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);

                        HaulDetail haulDetail = new HaulDetail();
                        haulDetail.setIdAkun(object.getString(Konfigurasi.KEY_ID_AKUN));
                        haulDetail.setNama(object.getString(Konfigurasi.KEY_NAMA));
                        haulDetail.setSubtotal(object.getString(Konfigurasi.KEY_SUBTOTAL));
                        haulDetailList.add(haulDetail);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerViewPetugas.setLayoutManager(linearLayoutManager);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewPetugas.getContext(), linearLayoutManager.getOrientation());
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_primary));
                    recyclerViewPetugas.addItemDecoration(dividerItemDecoration);
                    haulDetailAdapter = new HaulDetailAdapter(getActivity(), haulDetailList, idHaul);
                    recyclerViewPetugas.setAdapter(haulDetailAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_LOAD_LAPORAN_DETAIL, idHaul);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
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