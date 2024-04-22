package com.example.tugasakhir.profile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import com.example.tugasakhir.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userEmail = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();

        profileImage = findViewById(R.id.profileImage);
        Button resetPasswordButton = findViewById(R.id.reset_pass);

        TextView emailTextView = findViewById(R.id.emailTextView);
        emailTextView.setText(userEmail);

        resetPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });
    }
}