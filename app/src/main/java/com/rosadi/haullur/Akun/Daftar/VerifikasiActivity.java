package com.rosadi.haullur.Akun.Daftar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rosadi.haullur.Kelas.Penarikan.PenarikanActivity;
import com.rosadi.haullur.List.Adapter.KeluargaByAkunAdapter;
import com.rosadi.haullur.R;
import com.rosadi.haullur._util.Konfigurasi;
import com.rosadi.haullur._util.volley.RequestHandler;

import java.util.concurrent.TimeUnit;

public class VerifikasiActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText teleponEt, kodeET;
    LinearLayout verifikasi;

    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);

        auth = FirebaseAuth.getInstance();

        teleponEt = findViewById(R.id.telepon);
        verifikasi = findViewById(R.id.verifikasi);

        verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teleponEt.getText().toString().isEmpty()) {
                    Toast.makeText(VerifikasiActivity.this, "Masukkan nomor telepon!", Toast.LENGTH_SHORT).show();
                } else if (teleponEt.getText().toString().length() < 10) {
                    Toast.makeText(VerifikasiActivity.this, "Nomor telepon yang Anda masukkan tidak valid!", Toast.LENGTH_SHORT).show();
                } else {
                    cekTeleponTerdaftar(teleponEt.getText().toString());
                }
            }
        });
    }

    private void cekTeleponTerdaftar(String telepon) {
        class LoadData extends AsyncTask<Void, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(VerifikasiActivity.this, "Informasi", "Memeriksa nomor telepon...", false, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                if (s.trim().equals("1")) {
                    Toast.makeText(VerifikasiActivity.this, "Nomor telepon sudah terdaftar!", Toast.LENGTH_SHORT).show();
                } else {
                    String teleponNya = "+62" + telepon;
                    openDialogVerifikasiKode(teleponNya);
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_CEK_NOMOR_TELEPON, telepon);
                return s;
            }
        }

        LoadData loadData = new LoadData();
        loadData.execute();
    }

    private void openDialogVerifikasiKode(String telepon) {
        Dialog dialog = new Dialog(VerifikasiActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_verifikasi_kode);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        kodeET = dialog.findViewById(R.id.kode);

        kirimKodeVerifikasi(telepon);

        dialog.findViewById(R.id.verifikasi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifikasiKodeNya(kodeET.getText().toString());
            }
        });

        dialog.findViewById(R.id.ganti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void kirimKodeVerifikasi(String telepon) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(telepon)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String kode = phoneAuthCredential.getSmsCode();
            if (kode != null) {
                kodeET.setText(kode);
                verifikasiKodeNya(kode);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifikasiActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifikasiKodeNya(String kode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, kode);

        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                } else {
                    Toast.makeText(VerifikasiActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}