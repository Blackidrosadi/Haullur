package com.rosadi.haullur.Akun.Daftar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rosadi.haullur.Akun.LoginActivity;
import com.rosadi.haullur.R;

public class RegisterActivity extends AppCompatActivity {

    EditText nama, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);

        findViewById(R.id.daftar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nama.getText().toString().equals("") || nama.getText().toString().length() == 0) {
                    Toast.makeText(RegisterActivity.this, "Masukkan nama!", Toast.LENGTH_SHORT).show();
                } else if (nama.getText().toString().length() < 4) {
                    Toast.makeText(RegisterActivity.this, "Masukkan nama lebih dari 3 karakter!", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Toast.makeText(RegisterActivity.this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(RegisterActivity.this, VerifikasiActivity.class);
                    i.putExtra("nama", nama.getText().toString());
                    i.putExtra("email", email.getText().toString());
                    startActivity(i);
                }
            }
        });

        TextView daftarTV = findViewById(R.id.masuk);
        String daftarS = "Sudah mempunyai akun ? Masuk disini.";
        SpannableString span = new SpannableString(daftarS);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                RegisterActivity.this.finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(getResources().getColor(R.color.accent));
                ds.setUnderlineText(false);
                ds.setFakeBoldText(true);
            }
        };

        span.setSpan(clickableSpan, 23, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        daftarTV.setText(span);
        daftarTV.setMovementMethod(LinkMovementMethod.getInstance());
    }
}