package com.rosadi.haullur.Laporan.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rosadi.haullur.List.Adapter.HaulDetailAdapter;
import com.rosadi.haullur.List.Adapter.HaulDetailDanaLainnyaAdapter;
import com.rosadi.haullur.List.Adapter.PenarikanAdapter;
import com.rosadi.haullur.List.Model.DanaLainnya;
import com.rosadi.haullur.List.Model.HaulDetail;
import com.rosadi.haullur.List.Model.Penarikan;
import com.rosadi.haullur.Penarikan.PenarikanActivity;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PemasukanFragment extends Fragment {

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

        recyclerViewPetugas = view.findViewById(R.id.recycler_view_petugas);
        recyclerViewLainnya = view.findViewById(R.id.recycler_view_lainnya);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewPetugas.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewPetugas.getContext(), linearLayoutManager.getOrientation());
        recyclerViewPetugas.addItemDecoration(dividerItemDecoration);
        haulDetailAdapter = new HaulDetailAdapter(getActivity(), haulDetailList);
        recyclerViewPetugas.setAdapter(haulDetailAdapter);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLainnya.setLayoutManager(linearLayoutManager);
        recyclerViewLainnya.addItemDecoration(dividerItemDecoration);
        haulDetailDanaLainnyaAdapter = new HaulDetailDanaLainnyaAdapter(getActivity(), danaLainnyaList);
        recyclerViewLainnya.setAdapter(haulDetailDanaLainnyaAdapter);

        loadDataPenarikanPetugas();
        loadDataDanaLainnya();

        return view;
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
                    haulDetailAdapter = new HaulDetailAdapter(getActivity(), haulDetailList);
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
}