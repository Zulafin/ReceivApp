package com.example.tugasakhir.barang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RiwayatPenerimaanBarangActivity extends AppCompatActivity {

    private List<ItemData> itemList;
    private DatabaseReference databaseReference;
    private BarangAdapter adapter;
    private ProgressBar progressBar;
    private String todayDate;
    private static final int EDIT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_barang);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Calendar calendar = Calendar.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();

        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        todayDate = sdf.format(calendar.getTime());

        getDataKaryawan(todayDate);
        getDataPerusahaan(todayDate);

        adapter = new BarangAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        handleItemClicked();
    }

    private void getDataKaryawan(String todayDate) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("riwayat_penerimaan").child("barang_karyawan");
        Query todayKaryawanRef = databaseReference.orderByChild("tanggalMasuk").equalTo(todayDate);

        todayKaryawanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemData item = snapshot.getValue(ItemData.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RiwayatPenerimaanBarangActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getDataPerusahaan(String todayDate) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("riwayat_penerimaan").child("barang_perusahaan");
        Query todayKaryawanRef = databaseReference.orderByChild("tanggalMasuk").equalTo(todayDate);

        todayKaryawanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemData item = snapshot.getValue(ItemData.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RiwayatPenerimaanBarangActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void handleItemClicked() {
        adapter.setOnItemClickListener(new BarangAdapter.BarangAdapterListener() {
            @Override
            public void onEditClick(int position) {
                ItemData selectedItem = itemList.get(position);
                if (selectedItem.getItemId() != null && selectedItem.getBarangType() != null) {
                    Intent editIntent = new Intent(RiwayatPenerimaanBarangActivity.this, EditActivity.class);
                    editIntent.putExtra("itemId", selectedItem.getItemId());
                    editIntent.putExtra("barangType", selectedItem.getBarangType());
                    startActivity(editIntent);
                } else {
                    Toast.makeText(RiwayatPenerimaanBarangActivity.this, "Barang tidak dapat dimuat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick(int position) {
                ItemData selectedItem = itemList.get(position);
                String type = selectedItem.getBarangType();

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("riwayat_penerimaan")
                        .child(type)
                        .child(selectedItem.getItemId());

                ref.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RiwayatPenerimaanBarangActivity.this, "Barang Telah Dihapus", Toast.LENGTH_SHORT).show();
                        getDataKaryawan(todayDate);
                        getDataPerusahaan(todayDate);
                        itemList.clear();
                    }
                });
            }
        });
    }
}