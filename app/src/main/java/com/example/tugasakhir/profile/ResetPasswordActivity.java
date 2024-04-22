package com.example.tugasakhir.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tugasakhir.LoginActivity;
import com.example.tugasakhir.R;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText txtEmail;
    private Button btnResetPassword, btnLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        txtEmail = findViewById(R.id.email);
        btnResetPassword = findViewById(R.id.btnreset);
        btnLogin = findViewById(R.id.btnlogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
        btnResetPassword.setOnClickListener(v -> {
            String email = txtEmail.getText().toString().trim();

            if (!email.isEmpty()) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Instruksi reset password telah dikirim ke email Anda.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Gagal mengirim instruksi reset password. Pastikan email Anda benar.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Masukkan alamat email Anda.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

