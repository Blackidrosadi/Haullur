package com.rosadi.haullur.Kelas.Pendaftaran;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rosadi.haullur.MainActivity;
import com.rosadi.haullur.R;

public class PendaftaranBerhasilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pendaftaran_berhasil);

        Intent i = getIntent();
        String nama = i.getStringExtra("nama");
        String rt = i.getStringExtra("rt");

        findViewById(R.id.konfirmasi).setOnClickListener(view -> {
            String telepon = "628980022272";
            String pesan = "Assalamu'alaikum pak Saiful... Izin konfirmasi bahwa pendaftaran keluarga " +
                    "pada aplikasi Haullur dengan nama keluarga " + nama + " berlokasi di RT " + rt +
                    " telah selesai. Mohon segera membagikan petugas penarik pada keluarga kami. Terima kasih.";

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s", telepon, pesan)
                    )
            );
            startActivity(intent);
        });

        findViewById(R.id.kembali).setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_iya_tidak);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            TextView judul = dialog.findViewById(R.id.judul);
            TextView teks = dialog.findViewById(R.id.teks);
            TextView teksiya = dialog.findViewById(R.id.teksiya);

            judul.setText("PERINGATAN!!");
            teks.setText("Pastikan Anda telah melakukan konfirmasi melalui WhatsApp agar pendaftaran bisa kami proses. " +
                    "Jika tidak maka keluarga tidak akan mendapatkan pembagian petugas. Apakah Anda ingin lanjut kembali ke beranda ?");
            teksiya.setText("Kembali");

            dialog.findViewById(R.id.iya).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent i = new Intent(PendaftaranBerhasilActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
    public void onBackPressed() {
    }
}