package com.example.tugasakhir;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tugasakhir.MainActivity;
import com.example.tugasakhir.R;
import com.example.tugasakhir.profile.ResetPasswordActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText Username, Password;
    private Button btnLogin, btnSignUp;
    private TextView btnReset;
    private ImageButton passwordToggle;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Username = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        passwordToggle = findViewById(R.id.password_toggle);
        btnLogin = findViewById(R.id.login);
        btnSignUp = findViewById(R.id.btnRegister);
        btnReset = findViewById(R.id.updatepass);

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

        btnSignUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        btnLogin.setOnClickListener(v -> {
            String email = Username.getText().toString();
            String password = Password.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                login(email, password);
            } else {
                Toast.makeText(getApplicationContext(), "Silahkan Isi Semua data!", Toast.LENGTH_SHORT).show();
            }
        });

        btnReset.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class)));
    }

    private void login(String email, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Loading");
        builder.setMessage("Silahkan Tunggu");
        builder.setCancelable(false);
        AlertDialog progressDialog = builder.create();
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    reload();
                } else {
                    showLoginFailedMessage();
                }
            } else {
                showLoginFailedMessage();
            }
        });
    }

    private void showLoginFailedMessage() {
        Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_SHORT).show();
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
