package com.rosadi.haullur.List.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rosadi.haullur.Kelas.Pendaftaran.FormAlmarhumActivity;
import com.rosadi.haullur.List.Model.Almarhum;
import com.rosadi.haullur.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlmarhumPrefAdapter extends RecyclerView.Adapter<AlmarhumPrefAdapter.ViewHolder> {

    Context context;
    List<Almarhum> almarhumList;

    public AlmarhumPrefAdapter(Context context, List<Almarhum> almarhumList) {
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
        holder.nomor.setText(String.valueOf(position+1));
        holder.nama.setText(almarhum.getNama());

        holder.itemView.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_iya_tidak);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            TextView judul = dialog.findViewById(R.id.judul);
            TextView teks = dialog.findViewById(R.id.teks);
            TextView teksiya = dialog.findViewById(R.id.teksiya);

            judul.setText("PERINGATAN!!");
            teks.setText("Apakah anda yakin ingin menghapus almarhum " + almarhum.getNama() + " ?");
            teksiya.setText("Hapus");

            dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    ((FormAlmarhumActivity) context).hapusAlmarhum(almarhum);
                }
            });

            dialog.findViewById(R.id.batal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return almarhumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nomor, nama;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nomor = itemView.findViewById(R.id.nomor);
            nama = itemView.findViewById(R.id.nama);
        }
    }
}
