package com.example.tugasakhir;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import com.bumptech.glide.Glide;
import com.example.tugasakhir.R;
import com.example.tugasakhir.model.ItemData;
import com.example.tugasakhir.CheckDataActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class EditActivity extends AppCompatActivity {

    private RadioGroup radioGroupPilihanBarang;
    private RadioButton radioButtonBarangKaryawan, radioButtonBarangPerusahaan;
    private EditText editTextTanggalMasuk, editTextPengirim, editTextPenerima, editTextDateReceived, editTextStatus;
    public TextView NoResi;
    private Calendar calendar;
    private ImageView imageViewFotoBarang;
    private DatabaseReference databaseReference;
    private Button btnUploadFoto, btnSave, btnScanQR;
    private ProgressBar progressBar;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri imageUri;
    private String itemId;
    private String type;
    private boolean isBarangPerusahaan;
    private String imageUrl;
    private final String mPermission = Manifest.permission.CAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        radioGroupPilihanBarang = findViewById(R.id.radioGroupPilihanBarang);
        radioButtonBarangKaryawan = findViewById(R.id.radioButtonBarangKaryawan);
        radioButtonBarangPerusahaan = findViewById(R.id.radioButtonBarangPerusahaan);
        NoResi = findViewById(R.id.TextResi);
        editTextTanggalMasuk = findViewById(R.id.editTextTanggalMasuk);
        editTextPengirim = findViewById(R.id.editTextPengirim);
        editTextPenerima = findViewById(R.id.editTextPenerima);
        editTextDateReceived = findViewById(R.id.editTextDateReceived);
        editTextStatus = findViewById(R.id.editTextStatus);
        imageViewFotoBarang = findViewById(R.id.imageViewFotoBarang);
        progressBar = findViewById(R.id.progress_bar);
        btnScanQR = findViewById(R.id.btnScanQR);
        btnUploadFoto = findViewById(R.id.btnUploadFoto);
        btnSave = findViewById(R.id.btnSave);

        getBundle();

        calendar = Calendar.getInstance();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("riwayat_penerimaan");

        initData();
        initListener();
    }

    private void getBundle() {
        Intent intent = getIntent();
        itemId = intent.getStringExtra("itemId");
        type = intent.getStringExtra("barangType");
        isBarangPerusahaan = intent.getBooleanExtra("isBarangPerusahaan", false);
    }

    private void initData() {
        progressBar.setVisibility(View.VISIBLE);

        if (itemId != null && type != null) {
            DatabaseReference ref = databaseReference.child(type).child(itemId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ItemData data = dataSnapshot.getValue(ItemData.class);
                    if (data != null) {
                        progressBar.setVisibility(View.GONE);
                        NoResi.setText(data.getNoResi());
                        editTextPengirim.setText(data.getNamaPengirim());
                        editTextPenerima.setText(data.getNamaPenerima());
                        editTextTanggalMasuk.setText(data.getTanggalMasuk());
                        editTextDateReceived.setText(data.getReceivedDate());
                        editTextDateReceived.setEnabled(!isBarangPerusahaan);
                        editTextStatus.setText(data.getStatus());
                        editTextStatus.setEnabled(!isBarangPerusahaan);

                        imageUrl = data.getImageUrl();

                        if (Objects.equals(type, "barang_karyawan")) {
                            radioButtonBarangKaryawan.setChecked(true);
                            radioButtonBarangPerusahaan.setChecked(false);
                        } else if (Objects.equals(type, "barang_perusahaan")) {
                            radioButtonBarangKaryawan.setChecked(false);
                            radioButtonBarangPerusahaan.setChecked(true);
                        }

                        if (isBarangPerusahaan) {
                            editTextDateReceived.setVisibility(View.GONE);
                            editTextStatus.setVisibility(View.GONE);
                        }

                        Glide.with(EditActivity.this)
                                .load(data.getImageUrl())
                                .into(imageViewFotoBarang);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    private void initListener() {
        editTextTanggalMasuk.setOnClickListener(v -> showDatePickerDialog("masuk"));
        editTextDateReceived.setOnClickListener(v -> showDatePickerDialog("terima"));
        btnScanQR.setOnClickListener(v -> Scanner());

        btnUploadFoto.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pilih Sumber Foto");
            builder.setItems(new CharSequence[]{"Kamera", "Galeri"}, (dialog, which) -> {
                if (which == 0) {
                    //If the API Level is 23 or above, Check the Runtime Permission
                    if (checkSelfPermission(mPermission) != PackageManager.PERMISSION_GRANTED) {
                        //Permission not granted. request permission
                        ActivityCompat.requestPermissions(EditActivity.this, new String[]{mPermission}, REQUEST_IMAGE_CAPTURE);
                    } else {
                        //Permission granted
                        dispatchTakePictureIntent();
                    }
                } else {
                    openImageChooser();
                }
            });
            builder.show();
        });

        btnSave.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (imageUrl != null) {
                saveData(imageUrl);
            } else {
                uploadImageToFirebaseStorage(imageUri);
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File
            File photoFile = null;
            try {
                photoFile = createImageFile(this.getApplicationContext());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                imageUri = photoUri;
            }
        }
    }

    private File createImageFile(Context context) throws IOException {
        //Create Image Name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmsss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //noinspection UnnecessaryLocalVariable
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        return imageFile;
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (imageUri != null) {
                imageViewFotoBarang.setImageURI(imageUri);
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewFotoBarang.setImageURI(imageUri);
        }
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                NoResi.setText(intentResult.getContents());
                Log.d("resultcode", intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showDatePickerDialog(String type) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    if (Objects.equals(type, "masuk")) {
                        updateDateLabel();
                    } else if (Objects.equals(type, "terima")) {
                        editTextDateReceived();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void updateDateLabel() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editTextTanggalMasuk.setText(sdf.format(calendar.getTime()));
    }

    private void editTextDateReceived() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editTextDateReceived.setText(sdf.format(calendar.getTime()));
    }

    private void saveData(String imageUrl) {
        int selectedRadioButtonId = radioGroupPilihanBarang.getCheckedRadioButtonId();
        String selectedRadioButtonText = "";
        String databaseFolder = "";

        if (selectedRadioButtonId == radioButtonBarangKaryawan.getId()) {
            selectedRadioButtonText = radioButtonBarangKaryawan.getText().toString();
            databaseFolder = "barang_karyawan";
        } else if (selectedRadioButtonId == radioButtonBarangPerusahaan.getId()) {
            selectedRadioButtonText = radioButtonBarangPerusahaan.getText().toString();
            databaseFolder = "barang_perusahaan";
        }

        String tanggalMasuk = editTextTanggalMasuk.getText().toString();
        String resi = NoResi.getText().toString();
        String namaPenerima = editTextPenerima.getText().toString();
        String namaPengirim = editTextPengirim.getText().toString();
        String receivedDate = editTextDateReceived.getText().toString();
        String status = editTextStatus.getText().toString();

        if (TextUtils.isEmpty(selectedRadioButtonText) || TextUtils.isEmpty(resi)
                || TextUtils.isEmpty(namaPenerima) || TextUtils.isEmpty(namaPengirim)
                || TextUtils.isEmpty(receivedDate) || TextUtils.isEmpty(status)
        ) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Harap isi semua input", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("riwayat_penerimaan").child(databaseFolder);

            ItemData data = new ItemData(
                    itemId,
                    selectedRadioButtonText,
                    resi,
                    tanggalMasuk,
                    namaPenerima,
                    namaPengirim,
                    imageUrl,
                    receivedDate,
                    status,
                    databaseFolder
            );
            databaseReference.child(itemId).setValue(data);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Barang berhasil di Update", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(EditActivity.this, CheckDataActivity.class);
            startActivity(intent);
            finishAffinity();
        }
    }

    private void uploadImageToFirebaseStorage(Uri uri) {
        int selectedRadioButtonId = radioGroupPilihanBarang.getCheckedRadioButtonId();
        String selectedRadioButtonText = "";

        if (selectedRadioButtonId == radioButtonBarangKaryawan.getId()) {
            selectedRadioButtonText = radioButtonBarangKaryawan.getText().toString();
        } else if (selectedRadioButtonId == radioButtonBarangPerusahaan.getId()) {
            selectedRadioButtonText = radioButtonBarangPerusahaan.getText().toString();
        }

        String tanggalMasuk = editTextTanggalMasuk.getText().toString();
        String resi = NoResi.getText().toString();
        String namaPenerima = editTextPenerima.getText().toString();
        String namaPengirim = editTextPengirim.getText().toString();
        String receivedDate = editTextDateReceived.getText().toString();
        String status = editTextStatus.getText().toString();
        if (TextUtils.isEmpty(selectedRadioButtonText) || TextUtils.isEmpty(resi)
                || TextUtils.isEmpty(namaPenerima) || TextUtils.isEmpty(namaPengirim)
                || TextUtils.isEmpty(tanggalMasuk) || TextUtils.isEmpty(receivedDate) || TextUtils.isEmpty(status)
        ) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Harap isi semua input", Toast.LENGTH_SHORT).show();
        } else {
            String filename = UUID.randomUUID().toString();
            StorageReference ref = FirebaseStorage.getInstance().getReference("/barang-images/" + filename);

            if (uri != null) {
                ref.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    ref.getDownloadUrl().addOnSuccessListener(downloadedUri -> {
                        imageUrl = downloadedUri.toString();
                        saveData(imageUrl);
                        Toast.makeText(this, "Gambar berhasil di upload", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(exception -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Gambar gagal di upload!", Toast.LENGTH_SHORT).show();
                    });
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Gambar gagal dimuat!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Scanner() {
        IntentIntegrator integrator = new IntentIntegrator(EditActivity.this);
        integrator.setPrompt("Arahkan kamera ke QR Code atau Bar Code");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                dispatchTakePictureIntent();
            } else {
                //Permission denied
                Toast.makeText(this, "Permission Ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }
}