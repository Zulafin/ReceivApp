package com.example.tugasakhir;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tugasakhir.R;

public class CheckDataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        ImageView checkImageView = findViewById(R.id.checked);
        checkImageView.setImageResource(R.drawable.baseline_check_circle_24);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Intent intent = new Intent(CheckDataActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }
}