package com.ampi.registrasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ampi.registrasi.service.SQLiteHelper;
import com.ampi.registrasi.utility.Utilitas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "InputActivity";

    private Button btnList, btnReg, btnUpload, btnBack;
    private ImageView imgAvatar;
    private TextInputEditText inputReg, inputName, inputJabatan;
    private Utilitas utilitas;
    FirebaseFirestore db;

    final int REQUEST_CODE_GALLERY = 999;
    public static SQLiteHelper sqLiteHelper;
    String nama, jabatan, image, noReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        initView();
        sqLiteHelper = new SQLiteHelper(this);
        initFirebase();
    }

    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
    }

    private void savetoFireStore() {
        noReg = Utilitas.generateRandom();

        String imageVal = image == null ? "" : image;
        String jabatanval = jabatan == null ? "" : jabatan;

        Map<String, Object> user = new HashMap<>();
        user.put("nama", nama);
        user.put("jabatan", jabatanval);
        user.put("image", imageVal);
        user.put("status", false);

        db.collection("tamu").document(noReg)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.e(TAG, "documentSnapshot added with ID: " + noReg);
                    Utilitas.showCustomDialog(this, "Data Tersimpan", "Data Tamu Tersimpan dengan ID : " + noReg, "Tutup");
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error writing document", e);
                        Toast.makeText(getApplicationContext(), "Gagal menyimpan data tamu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initView() {
        btnList = findViewById(R.id.btnList);
        btnReg = findViewById(R.id.btnReg);
        btnUpload = findViewById(R.id.btnUpload);
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        inputName = findViewById(R.id.namaAnggota);
        inputJabatan = findViewById(R.id.jabatan);

        btnList.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpload:
                Log.e(TAG, "onClick: UPLOAD");
                uploadImage();
                break;
            case R.id.btnReg:
                Log.e(TAG, "onClick: REG");
                postRegistration();
                break;
            case R.id.btnList:
                Log.e(TAG, "onClick: LIST");
                toListActivity();
            case R.id.btnBack:
                Log.e(TAG, "onClick: Back");
                toBack();
                break;
        }
    }

    private void uploadImage() {
        getPermission();
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(
                InputActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgAvatar.setImageBitmap(bitmap);
                image = Utilitas.imageBitmapToString(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void postRegistration() {
        try {
            nama = inputName.getText().toString().trim();
            jabatan = inputJabatan.getText().toString().trim();

            if (nama.isEmpty()) {
                //jika nama tamu belum di masukan
                Toasty.error(this, "Mohon input nama anda", Toast.LENGTH_SHORT, true).show();
                inputName.requestFocus();
            } else {
                //simpan ke firestore jika nama tamu sudah dimasukan
                savetoFireStore();
                inputName.setText("");
                inputJabatan.setText("");
                imgAvatar.setImageResource(R.mipmap.ic_launcher);
            }

            Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toListActivity() {
        Intent intent = new Intent(InputActivity.this, AnggotaList.class);
        startActivity(intent);
    }

    private void toBack(){
        finish();
    }


}