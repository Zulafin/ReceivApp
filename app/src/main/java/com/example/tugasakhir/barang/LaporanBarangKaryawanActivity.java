package com.example.tugasakhir.barang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tugasakhir.R;
import com.example.tugasakhir.adapter.BarangAdapter;
import com.example.tugasakhir.model.ItemData;
import com.example.tugasakhir.EditActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.ArrayList;
import java.util.List;

public class LaporanBarangKaryawanActivity extends AppCompatActivity {

    private EditText textResi;
    private ImageButton imageButtonScan;
    private Button buttonSearch;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private List<ItemData> itemList;
    private BarangAdapter adapter;
    private static final int ZXING_SCANNER_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_karyawan);

        textResi = findViewById(R.id.TextResi);
        imageButtonScan = findViewById(R.id.imageButtonScan);
        buttonSearch = findViewById(R.id.buttonSearch);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_bar);

        itemList = new ArrayList<>();
        adapter = new BarangAdapter(this, itemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        getData();
        handleItemClicked();
        initListener();
    }

    private void getData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("riwayat_penerimaan/barang_karyawan");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemData item = snapshot.getValue(ItemData.class);
                    snapshot.getKey();
                    if (item != null) {
                        itemList.add(item);
                    }
                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LaporanBarangKaryawanActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initListener() {
        buttonSearch.setOnClickListener(view -> {
            String searchQuery = textResi.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                searchItemsByResi(searchQuery);
            } else {
                Toast.makeText(LaporanBarangKaryawanActivity.this, "Data Barang tidak ditemukan", Toast.LENGTH_LONG).show();
            }
        });

        imageButtonScan.setOnClickListener(view -> {
            IntentIntegrator integrator = new IntentIntegrator(LaporanBarangKaryawanActivity.this);
            integrator.setPrompt("Arahkan kamera ke Bar Code");
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(true);
            integrator.initiateScan();
        });
    }

    private void handleItemClicked() {
        adapter.setOnItemClickListener(new BarangAdapter.BarangAdapterListener() {
            @Override
            public void onEditClick(int position) {
                ItemData selectedItem = itemList.get(position);
                if (selectedItem.getItemId() != null) {
                    Intent editIntent = new Intent(LaporanBarangKaryawanActivity.this, EditActivity.class);
                    editIntent.putExtra("itemId", selectedItem.getItemId());
                    editIntent.putExtra("barangType", "barang_karyawan");
                    startActivity(editIntent);
                } else {
                    Toast.makeText(LaporanBarangKaryawanActivity.this, "Barang tidak dapat dimuat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick(int position) {
                ItemData selectedItem = itemList.get(position);
                DatabaseReference data = databaseReference.child(selectedItem.getItemId());
                data.removeValue().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(LaporanBarangKaryawanActivity.this, "Barang Telah Dihapus", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                textResi.setText(intentResult.getContents());
                Log.d("resultcode", intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void searchItemsByResi(String resi) {
        databaseReference.orderByChild("noResi").equalTo(resi).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemData item = snapshot.getValue(ItemData.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
                if (itemList.isEmpty()) {
                    Toast.makeText(LaporanBarangKaryawanActivity.this, "No matching items found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LaporanBarangKaryawanActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}