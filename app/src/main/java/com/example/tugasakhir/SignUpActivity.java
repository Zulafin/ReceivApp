package com.example.tugasakhir;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private EditText Name, Username, Password;
    private Button btnRegister, btnLogin;
    private ProgressBar progressBar;
    private ImageButton passwordToggle;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Name = findViewById(R.id.name);
        Username = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        passwordToggle = findViewById(R.id.password_toggle);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnlogin);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        passwordToggle.setOnClickListener(v -> {
            int inputType = Password.getInputType();
            if ((inputType & 0x80) == 0x80) {
                Password.setInputType(inputType & ~0x80);
                passwordToggle.setImageResource(R.drawable.baseline_remove_red_eye_24);
            } else {
                Password.setInputType(inputType | 0x80);
                passwordToggle.setImageResource(R.drawable.baseline_visibility_off_24);
            }
            Password.setSelection(Password.getText().length());
        });

        btnLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
        btnRegister.setOnClickListener(v -> {
            String name = Name.getText().toString();
            String email = Username.getText().toString();
            String password = Password.getText().toString();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                register(name, email, password);
            } else {
                Toast.makeText(getApplicationContext(), "Silahkan Isi Semua Data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String name, String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                FirebaseUser firebaseUser = task.getResult().getUser();
                if (firebaseUser != null) {
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    firebaseUser.updateProfile(request).addOnCompleteListener(task1 -> {
                        progressBar.setVisibility(View.GONE);
                        if (task1.isSuccessful()) {
                            reload();
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal memperbarui profil pengguna", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Register Gagal", Toast.LENGTH_SHORT).show();
                }
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Register Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }
}