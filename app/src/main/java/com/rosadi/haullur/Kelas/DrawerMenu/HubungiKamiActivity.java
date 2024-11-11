package com.rosadi.haullur.Kelas.DrawerMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.rosadi.haullur.R;

public class HubungiKamiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubungi_kami);

        findViewById(R.id.kembali).setOnClickListener(view -> {
            onBackPressed();
        });

        findViewById(R.id.telepon).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:08980022272"));
            startActivity(intent);
        });

        findViewById(R.id.whatsapp).setOnClickListener(view -> {
            String telepon = "628980022272";
            String pesan = "Assalamu'alaikum...";

            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s", telepon, pesan)
                    )
            );
            startActivity(i);
        });

        findViewById(R.id.email).setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","officialfprg@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Halo haullur");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(emailIntent, "Kirim email..."));
        });

        findViewById(R.id.saiful).setOnClickListener(view -> {
            String telepon = "628980022272";
            String pesan = "Assalamu'alaikum...";

            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s", telepon, pesan)
                    )
            );
            startActivity(i);
        });

        findViewById(R.id.rohman).setOnClickListener(view -> {
            String telepon = "6289685720288";
            String pesan = "Assalamu'alaikum...";

            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s", telepon, pesan)
                    )
            );
            startActivity(i);
        });

        findViewById(R.id.imam).setOnClickListener(view -> {
            String telepon = "6287750354305";
            String pesan = "Assalamu'alaikum...";

            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s", telepon, pesan)
                    )
            );
            startActivity(i);
        });
    }
}