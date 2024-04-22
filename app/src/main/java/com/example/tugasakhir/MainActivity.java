package com.example.tugasakhir;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.tugasakhir.R;
import com.example.tugasakhir.barang.PenerimaanBarangActivity;
import com.example.tugasakhir.barang.RiwayatPenerimaanBarangActivity;
import com.example.tugasakhir.barang.LaporanBarangKaryawanActivity;
import com.example.tugasakhir.barang.LaporanBarangPerusahaanActivity;
import com.example.tugasakhir.LoginActivity;
import com.example.tugasakhir.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    public TextView userEmail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cvInputBarang = findViewById(R.id.cvinputbarang);
        CardView cvRiwayat = findViewById(R.id.cvriwayat);
        CardView cvLaporanPerusahaan = findViewById(R.id.cvlaporan);
        CardView cvLaporanKaryawan = findViewById(R.id.cvlaporankaryawan);
        userEmail = findViewById(R.id.user_email);
        drawerLayout = findViewById(R.id.drawer_layout);
        mAuth = FirebaseAuth.getInstance();

        String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        userEmail.setText(email);

        cvInputBarang.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PenerimaanBarangActivity.class);
            startActivity(intent);
        });

        cvRiwayat.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RiwayatPenerimaanBarangActivity.class);
            startActivity(intent);
        });

        cvLaporanPerusahaan.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LaporanBarangPerusahaanActivity.class);
            startActivity(intent);
        });

        cvLaporanKaryawan.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LaporanBarangKaryawanActivity.class);
            startActivity(intent);
        });

    }

    public void ClickMenu(View view) {openDrawer(drawerLayout);}

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void name(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        logoutMenu(MainActivity.this);
    }

    private void logoutMenu(MainActivity mainActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to Logout ? ");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}